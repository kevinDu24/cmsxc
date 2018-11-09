package cn.com.leadu.cmsxc.appuser.service.impl;

import cn.com.leadu.cmsxc.appbusiness.service.VehicleTaskSearchHistoryService;
import cn.com.leadu.cmsxc.appbusiness.service.VehicleTaskService;
import cn.com.leadu.cmsxc.appuser.service.*;
import cn.com.leadu.cmsxc.appuser.util.constant.enums.*;
import cn.com.leadu.cmsxc.appuser.validator.sysuser.vo.VehicleVo;
import cn.com.leadu.cmsxc.common.constant.Constants;
import cn.com.leadu.cmsxc.common.constant.enums.*;
import cn.com.leadu.cmsxc.common.util.ArrayUtil;
import cn.com.leadu.cmsxc.common.util.CommonUtil;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.appbusiness.repository.VehicleTaskOperateHistoryRepository;
import cn.com.leadu.cmsxc.data.appbusiness.repository.VehicleTaskSearchHistoryRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.*;
import cn.com.leadu.cmsxc.data.system.repository.SystemUserRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTask;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskOperateHistory;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskRecovery;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskSearchHistory;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.ClueDetail;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.VehicleTaskResultVo;
import cn.com.leadu.cmsxc.pojo.appuser.entity.*;
import cn.com.leadu.cmsxc.pojo.appuser.vo.RewardForVehicleVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.applydetail.*;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import com.google.common.collect.Maps;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanzhenxia on 2018/1/17.
 *
 * 车辆相关信息service实现类
 */
@Service
public class VehicleServiceImpl implements VehicleService{
    private static final Logger logger = LoggerFactory.getLogger(VehicleServiceImpl.class);
    @Autowired
    private VehicleTaskSearchHistoryService vehicleTaskSearchHistoryService;
    @Autowired
    private VehicleTaskSearchHistoryRepository taskSearchHistoryRepository;
    @Autowired
    private VehicleTaskOperateHistoryRepository taskOperatorHistoryRepository;
    @Autowired
    private VehicleTaskService vehicleTaskService;
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private SystemUserScoreRepository systemUserScoreRepository;
    @Autowired
    private SystemUserRepository systemUserRepository;
    @Autowired
    private ClueInforRepository clueInforRepository;
    @Autowired
    private GpsAppInfoRepository gpsAppInfoRepository;
    @Autowired
    private AuthorizationHistoryRepository authorizationHistoryRepository;
    @Autowired
    private VehiclePhotoPathRepository vehiclePhotoPathRepository;
    @Autowired
    private ClueService clueService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private RecoveryTaskService recoveryTaskService;

    /**
     * 判断是否可以搜索到车辆，未授权的，已过期的，没有收车公司的可以查看到
     * 判断是否需要扣除积分，如果是自己收车公司的过期的任务或是自己查看过的任务，不需要扣除积分，否则需要扣除积分
     *
     * @param userId  用户id
     * @param plate 车牌号
     * @param taskStatus 任务状态
     * @return
     */
    public ResponseEntity<RestResponse> checkSearchBefore(String userId , String plate, String taskStatus){
        // 去除车牌号中的表情符号
        plate = CommonUtil.filterEmoji(plate);
        // 查询用户信息，获取用户收车公司id
        SystemUser sysUser = systemUserService.selectSystemUserByUserId(userId);
        if(sysUser == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","当前登录用户手机号为空"),
                    HttpStatus.OK);
        }
        // 根据车牌号取得车辆任务工单状态为正常的车辆信息,并且收车公司派单表中为已过期，且不是已授权和已完成的
       VehicleTask task = vehicleTaskService.selectByPlateAndTaskStatus(plate,taskStatus, new Date());
        if(task == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未查询到车辆信息"),
                    HttpStatus.OK);
        }
        // 查询该车辆有效的授权状态
        VehicleAuthorization vehicleAuthorization = authorizationService.selectByTaskId(task.getId());
        if(vehicleAuthorization != null && (AuthorizationStatusEnums.AUTHORIZETED.getType().equals(vehicleAuthorization.getAuthorizationStatus())
                || AuthorizationStatusEnums.FINISH.getType().equals(vehicleAuthorization.getAuthorizationStatus()))){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未查询到车辆信息"),
                    HttpStatus.OK);
        }
        // 存放返回值
        Map map = Maps.newHashMap();
        // 如果此车辆存在车辆任务工单中，并且状态为正常，查看是否有查询历史
        if(task != null){
            // 根据任务id和收车公司id获取我的派单信息
            VehicleTaskRecovery vehicleTaskRecovery = recoveryTaskService.findRecoveryTaskByTaskIdAndRecoveryCompanyId(String.valueOf(task.getId()), sysUser.getRecoveryCompanyId());
            // 如果存在，表明是自己收车公司的任务，返回已查看，不用扣除积分
            if(vehicleTaskRecovery != null){
                map.put("checkMarkFlag","1");// 返回已查看
            }else {
                VehicleTaskSearchHistory history = vehicleTaskSearchHistoryService.selectByPlateAndUserId(String.valueOf(task.getId()), userId);
                // 如果存在查询历史，返回1：已查看
                if (history != null) {
                    map.put("checkMarkFlag", "1");// 返回已查看
                    // 如果不存在查询历史，返回0：未查看
                } else {
                    map.put("checkMarkFlag", "0");// 返回未查看
                }
            }
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,map,""),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未查询到车辆信息"),
                HttpStatus.OK);
    }

    /**
     * 判断当前数据工单状态是否更改为“正常”之外的情况
     * 1，悬赏列表 查看详情
     * 2.已命中线索查看详情
     * 3，查看历史查看详情
     * @param plate 车牌号
     * @return
     */
    public ResponseEntity<RestResponse> checkTaskStatus(String plate){
        // 查根据车牌号查询状态为正常的工单信息
        VehicleTask tasks = vehicleTaskService.selectByPlateAndTaskStatus(plate,TaskStatusEnums.NORMAL.getType());
        if(tasks == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该任务状态有变更，请刷新重试"),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"",""),
                HttpStatus.OK);
    }

    /**
     * 判断是否需要扣除积分，如果是自己收车公司的过期的任务或是自己查看过的任务，不需要扣除积分，否则需要扣除积分
     * 1,悬赏列表查看详情check
     * 2.授权列表查看详情check
     * 3,已命中线索查看详情check
     * @param userId  用户id
     * @param plate 车牌号
     * @return
     */
    public ResponseEntity<RestResponse> checkSearchBeforeForList(String userId , String plate , String taskStatus){
        // 查询用户信息，获取用户收车公司id
        SystemUser sysUser = systemUserService.selectSystemUserByUserId(userId);
        VehicleTask task = vehicleTaskService.selectByPlateAndTaskStatus(plate, taskStatus);
        if(task == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该任务已失效！"),
                    HttpStatus.OK);
        }
        // 存放返回值
        Map map = Maps.newHashMap();
        // 如果此车辆存在车辆任务工单中，判断此任务是否为当前用户收车公司的任务
        if(task != null){
            // 根据任务id和收车公司id获取我的派单信息
            VehicleTaskRecovery vehicleTaskRecovery = recoveryTaskService.findRecoveryTaskByTaskIdAndRecoveryCompanyId(String.valueOf(task.getId()), sysUser.getRecoveryCompanyId());
            // 如果存在，表明是自己收车公司的任务，返回已查看，不用扣除积分
            if(vehicleTaskRecovery != null){
                map.put("checkMarkFlag","1");// 返回已查看
            }else {
                VehicleTaskSearchHistory history = vehicleTaskSearchHistoryService.selectByPlateAndUserId(String.valueOf(task.getId()), userId);
                // 如果存在查询历史，返回1：已查看
                if (history != null) {
                    map.put("checkMarkFlag", "1");// 返回已查看
                    // 如果不存在查询历史，返回0：未查看
                } else {
                    map.put("checkMarkFlag", "0");// 返回未查看
                }
            }
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,map,""),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未查询到车辆信息"),
                HttpStatus.OK);
    }


    /**
     * 取工单状态为正常的车辆任务工单数量，并且收车公司派单表中为已过期，且不是已授权和已完成的
     * @return
     */
    public ResponseEntity<RestResponse> getNormalCount(){
        // 查找车辆数量
       int normalCount = vehicleTaskService.selectCountAll(new Date());
        // 存放返回值
        Map map = Maps.newHashMap();
        map.put("vehicleCount",normalCount);// 返回车辆数目
        return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,map,""),
                    HttpStatus.OK);
    }

    /**
     * 判断积分是否充足
     *
     * @param userId 用户id
     * @return
     */
    public ResponseEntity<RestResponse> searchUserScore(String userId){
        // 查询用户信息，修改用户积分
        SystemUser sysUser = systemUserService.selectSystemUserByUserId(userId);
        if(sysUser.getTotalScore() < 30){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","您的积分不足，快去上传线索获取积分吧！"),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"",""),
                HttpStatus.OK);
    }
    /**
     * 根据价格区间，gps有无，线索有无，违章有无等条件，搜索车辆信息
     *
     * @param vehicleTaskVo 画面参数
     * @param userId 用户id
     * @return
     */
    public ResponseEntity<RestResponse> getVehicleTaskList(String userId, cn.com.leadu.cmsxc.pojo.appuser.vo.VehicleTaskVo vehicleTaskVo){
        // 根据画面设置的条件，查询车辆状态为正常车辆信息
        List<RewardForVehicleVo> vehicleTaskList = vehicleTaskService.selectByTaskStatusAndMore(userId, TaskStatusEnums.NORMAL.getType(), vehicleTaskVo);
        if(vehicleTaskList != null && !vehicleTaskList.isEmpty()){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,vehicleTaskList,""),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","数据加载完毕"),
                HttpStatus.OK);
    }

    /**
     * 根据车牌号，查看车辆详情
     *
     * 1,悬赏列表查看详情接口
     * 2.授权列表查看详情接口
     * 3.已命中线索 查看详情接口
     * 4.查看历史 查看详情接口
     * 5.搜索车牌号 查看详情接口
     * @param userId 用户id
     * @param vehicleVo 画面参数
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> searchVehicleDetail(String userId , VehicleVo vehicleVo){
        if(StringUtil.isNull(userId)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","当前登录用户手机号为空"),
                    HttpStatus.OK);
        }
        Date nowDate = new Date();
        // 根据车牌号码查找工单状态为正常的车辆信息
        VehicleTask task = vehicleTaskService.selectByPlateAndTaskStatus(vehicleVo.getPlate(), vehicleVo.getTaskStatus());
        // 查询用户信息，修改用户积分
        SystemUser sysUser = systemUserService.selectSystemUserByUserId(userId);
        VehicleTaskResultVo vo;
        if(task != null ){
            // 车辆详情信息
            vo = new VehicleTaskResultVo(task);
            // 根据委托公司用户名查询下载连接
            Example example = new Example(GpsAppInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("leaseCompanyUserName", task.getLeaseCompanyUserName());
            List<GpsAppInfo> gpsAppInfoList = gpsAppInfoRepository.selectByExampleList(example);
            if (ArrayUtil.isNotNullAndLengthNotZero(gpsAppInfoList)){
                GpsAppInfo gpsAppInfo = gpsAppInfoList.get(0);
                String client = httpServletRequest.getHeader("client");
                vo.setAppDownLoadUrl(ClientTypeEnums.IOS.getCode().equals(client) ? gpsAppInfo.getIosUrl() : ClientTypeEnums.ANDROID.getCode().equals(client) ? gpsAppInfo.getAndroidUrl() : "" );
            }
            // 车辆详情页面判定该车是否可以申请以及是否是自己申请的
            VehicleAuthorization vehicleAuthorization = authorizationService.selectByTaskId(task.getId());
            // 未授权
            vo.setApplyAuthStatus("0");
            // 该任务为授权状态
            if(vehicleAuthorization != null){
                // 取得该授权用户id
                String palteAuthUser = vehicleAuthorization.getUserId();
                if (userId.equals(palteAuthUser)) {
                    //自己已授权过
                    vo.setApplyAuthStatus("1");
                } else {
                    //他人已授权过
                    vo.setApplyAuthStatus("2");
                }
            }
            //根据任务工单id和用户id获取该用户最新授权信息
            VehicleAuthorization authorizationByUser = authorizationService.selectByTaskIdAndUserId(task.getId(),userId);
            if(authorizationByUser != null && "0".equals(vo.getApplyAuthStatus())){
                if(AuthorizationStatusEnums.APPLYING.getType().equals(authorizationByUser.getAuthorizationStatus())){
                    // 申请审批中
                    vo.setApplyAuthStatus("3");
                }
            }
            //车辆详情页面返回授权状态和备注，并获取委托方电话
            if(StringUtil.isNotNull(vehicleVo.getAuthorizationId())){
                VehicleAuthorization authorizationById = authorizationService.selectByAuthorizationId(vehicleVo.getAuthorizationId());
                if(authorizationById != null){
                    //TODO/******************兼容老版本开始****************************/
                    // 授权id
                    vo.setAuthorizationId(authorizationById.getId());
                    // 审批备注
                    vo.setApproveRemark(authorizationById.getApprovalRemark());
                    // 申请备注
                    vo.setRemark(authorizationById.getRemark());
                    // 申请开始时间
                    vo.setApplyStartDate(authorizationById.getApplyStartDate());
                    // 申请结束时间
                    vo.setApplyEndDate(authorizationById.getApplyEndDate());
                    // 授权证书url
                    vo.setAuthorizationPaperUrl(authorizationById.getAuthorizationPaperUrl());
                    // 授权状态
                    vo.setAuthorizationStatus(authorizationById.getAuthorizationStatus());
                    // 操作时间
                    vo.setOperateDate(authorizationById.getOperateDate());
                    // 如果授权状态为已授权 或 已过期
                    if(AuthorizationStatusEnums.AUTHORIZETED.getType().equals(authorizationById.getAuthorizationStatus())
                            || AuthorizationStatusEnums.AUTHORIZATIONOUTTIME.getType().equals(authorizationById.getAuthorizationStatus())){
                        // 授权开始时间
                        vo.setAuthStartDate(authorizationById.getOperateDate());
                        // 授权失效时间
                        vo.setAuthOutTimeDate(authorizationById.getAuthorizationOutTimeDate());
                    }else if(AuthorizationStatusEnums.LEASECANCEL.getType().equals(authorizationById.getAuthorizationStatus())){
                        vo.setCancelPaperUrl(authorizationById.getCancelPaperUrl());
                        vo.setCancelReason(authorizationById.getCancelReason());
                    }
                    //TODO/******************兼容老版本结束****************************/

                    //获取申请基本信息
                    List<AuthInfoVo> authInfoVoList = buildAuthInfo(vehicleVo.getAuthorizationId(), authorizationById);

                    vo.setAuthInfo(authInfoVoList);
                }

            }
            // 根据委托公司用户名查询委托公司一级管理员联系电话
            SystemUser leaseUser = systemUserService.selectSystemUserByUserId(task.getLeaseCompanyUserName());
            vo.setLeasePhone(leaseUser.getUserPhone());
            // 查询车辆所有线索信息
            List<ClueInfo>  clueList = clueService.selectByPlate(vehicleVo.getPlate(),task.getVehicleIdentifyNum());
            if(clueList != null && !clueList.isEmpty()){
                for(ClueInfo clue : clueList){
                    // 返回用车辆线索详情
                    ClueDetail clueDetail = new ClueDetail();
                    // 线索地址
                    clueDetail.setAppAddr(clue.getAppAddr());
                    // 线索上传时间
                    clueDetail.setUploadDate(clue.getUploadDate());
                    vo.getClueDetails().add(clueDetail);
                }
            }
            // 从前未查看过，第一次查看
            if("0".equals(vehicleVo.getCheckFlag())){
                // 登录积分流水表，扣除30积分
                SystemUserScore systemUserScore = new SystemUserScore();
                // 用户id
                systemUserScore.setUserId(userId);
                // 积分代码
                systemUserScore.setScoreCode(ScoreCodeEnums.VIEW.getType());
                // 积分值
                systemUserScore.setScoreValue(-30);
                // 备注
                systemUserScore.setRemark(ScoreCodeEnums.VIEW.getValue());
                systemUserScore.setScoreTime(nowDate);
                systemUserScore.setCreator(userId);
                systemUserScore.setUpdater(userId);
                systemUserScore.setUpdateTime(nowDate);
                systemUserScore.setCreateTime(nowDate);
                systemUserScoreRepository.insertOne(systemUserScore);
                // 用户总积分
                sysUser.setTotalScore(sysUser.getTotalScore()- 30);
                sysUser.setUpdateTime(nowDate);
                sysUser.setUpdater(userId);
                systemUserRepository.updateByPrimaryKey(sysUser);
            }
            // 根据任务id和用户id查询查看日志表，判断此用户是否查看过此车辆
            VehicleTaskSearchHistory vehicleTaskSearchHistory = vehicleTaskSearchHistoryService.selectByPlateAndUserId(String.valueOf(task.getId()), sysUser.getUserId());
            if(vehicleTaskSearchHistory == null){
                // 登录工单查询历史表
                VehicleTaskSearchHistory history = new VehicleTaskSearchHistory();
                // 任务id
                history.setTaskId(task.getId());
                // 车牌号
                history.setPlate(vehicleVo.getPlate());
                // 用户id
                history.setUserId(userId);
                // 查询时间
                history.setSearchTime(nowDate);
                history.setUpdater(userId);
                history.setCreator(userId);
                history.setUpdateTime(nowDate);
                history.setCreateTime(nowDate);
                taskSearchHistoryRepository.insertOne(history);
            }else{
                // 查看时间
                vehicleTaskSearchHistory.setSearchTime(nowDate);
                vehicleTaskSearchHistory.setTaskId(task.getId());
                vehicleTaskSearchHistory.setUpdateTime(nowDate);
                // 更新查看日志表
                taskSearchHistoryRepository.updateByPrimaryKey(vehicleTaskSearchHistory);
            }
            // 登录工单操作日志表
            VehicleTaskOperateHistory operatorHistory = new VehicleTaskOperateHistory();
            // 任务id
            operatorHistory.setTaskId(task.getId());
            // 用户id
            operatorHistory.setUserId(userId);
            // 操作时间
            operatorHistory.setOperateTime(nowDate);
            // 操作内容
            operatorHistory.setOperateContent("查看悬赏详情");
            // 备注
            if("0".equals(vehicleVo.getCheckFlag())){
                operatorHistory.setRemark("查看悬赏详情，扣除30积分");
            }else{
                operatorHistory.setRemark("查看悬赏详情");
            }
            operatorHistory.setCreator(userId);
            operatorHistory.setCreateTime(nowDate);
            operatorHistory.setUpdater(userId);
            operatorHistory.setUpdateTime(nowDate);
            taskOperatorHistoryRepository.insertOne(operatorHistory);

            // 更新已命中未查看的线索的checkFlag为1：已查看
            if(StringUtil.isNotNull(vehicleVo.getClueCheckFlag()) && ClueCheckFlag.NOTCHECK.getType().equals(vehicleVo.getClueCheckFlag())){
                // 取得此用户所有有关此车辆的命中线索信息
                List<ClueInfo> clueInfos = clueService.selectByUserIdAndPlateAndTargetFlag(vehicleVo.getPlate(), userId, TargetFlag.TARGET.getType());
                if(clueInfos != null && !clueInfos.isEmpty()){
                    for(ClueInfo clueInfo : clueInfos) {
                        // 是否查看过标识
                        clueInfo.setCheckFlag(ClueCheckFlag.CHECK.getType());
                        clueInfo.setUpdater(userId);
                        clueInfo.setUpdateTime(nowDate);
                        // 更新我的线索表
                        clueInforRepository.updateByPrimaryKey(clueInfo);
                    }
                }
            }
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,vo,"查询成功"),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未查询到车辆信息"),
                HttpStatus.OK);
    }
    /**
     * 获取申请基本信息
     *
     * @param authorizationId 授权id
     * @param authorization 授权信息
     * @return
     */
    public List<AuthInfoVo> buildAuthInfo(String authorizationId, VehicleAuthorization authorization) {
        List<AuthInfoVo> authInfoVoList = new ArrayList<>();
        //从授权历史表中查询其他节点信息
        Example example = new Example(AuthorizationHistory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("authorizationId", authorizationId);
        example.setOrderByClause(" create_time desc ");
        List<AuthorizationHistory> list = authorizationHistoryRepository.selectByExampleList(example);
        if(ArrayUtil.isNotNullAndLengthNotZero(list)){
            //循环授权日志结果
            for(AuthorizationHistory item : list){
                AuthInfoVo authInfoVoTemp = new AuthInfoVo();
                if(OperateContentEnums.CANCEL.getType().equals(item.getOperateContent())){// 取消申请
                    CancelApplyVo cancelApplyVo = new CancelApplyVo();
                    // 取消时间
                    cancelApplyVo.setCancelTime(item.getOperateTime());
                    // 取消申请信息
                    authInfoVoTemp.setCancelApply(cancelApplyVo);
                    authInfoVoTemp.setType(2);
                    authInfoVoList.add(authInfoVoTemp);
                }else if(OperateContentEnums.AUTHORIZETED.getType().equals(item.getOperateContent())){ //  获得授权
                    //创建获得授权结果实体类
                    ObtainAuthVo obtainAuthVo = new ObtainAuthVo();
                    // 审核时间
                    obtainAuthVo.setPassTime(item.getOperateTime());
                    // 审核备注
                    obtainAuthVo.setApproveRemark(item.getRemark());
                    // 授权开始时间
                    obtainAuthVo.setAuthStartDate(item.getOperateTime());
                    // 授权失效时间
                    obtainAuthVo.setAuthOutTimeDate(authorization.getAuthorizationOutTimeDate());
                    // 授权信息
                    authInfoVoTemp.setObtainAuth(obtainAuthVo);
                    authInfoVoTemp.setType(4);
                    authInfoVoList.add(authInfoVoTemp);
                }else if(OperateContentEnums.REFUSE.getType().equals(item.getOperateContent())){ // 拒绝申请
                    RefuseAuthVo refuseAuthVo = new RefuseAuthVo();
                    // 审批备注
                    refuseAuthVo.setApproveRemark(item.getRemark());
                    // 审批时间
                    refuseAuthVo.setRefuseTime(item.getOperateTime());
                    // 拒绝申请信息
                    authInfoVoTemp.setRefuseAuth(refuseAuthVo);
                    authInfoVoTemp.setType(3);
                    authInfoVoList.add(authInfoVoTemp);
                }else if(OperateContentEnums.DELAY.getType().equals(item.getOperateContent())){ //授权延期( 有可能会多次授权 )
                    //创建授权延期结果实体类
                    AuthDelayVo authDelayVo = new AuthDelayVo();
                    // 延期时间
                    authDelayVo.setDelayTime(item.getOperateTime());
                    // 延期备注
                    authDelayVo.setDelayRemark(item.getRemark());
                    // 授权开始时间
                    authDelayVo.setAuthStartDate(authorization.getApplyEndDate());
                    // 授权失效时间
                    authDelayVo.setAuthOutTimeDate(authorization.getAuthorizationOutTimeDate());
                    // 授权延期信息
                    authInfoVoTemp.setAuthDelay(authDelayVo);
                    authInfoVoTemp.setType(5);
                    authInfoVoList.add(authInfoVoTemp);
                }else if(OperateContentEnums.AUTHORIZATIONOUTTIME.getType().equals(item.getOperateContent())){ //授权失效
                    AuthExpiryVo authExpiryVo = new AuthExpiryVo();
                    // 授权失效时间
                    authExpiryVo.setExpiryTime(item.getOperateTime());
                    // 授权失效信息
                    authInfoVoTemp.setAuthExpiry(authExpiryVo);
                    authInfoVoTemp.setType(6);
                    authInfoVoList.add(authInfoVoTemp);
                }else if(OperateContentEnums.LEASECANCEL.getType().equals(item.getOperateContent())){ // 委托方取消
                    LeaseCancelTaskVo leaseCancelTaskVo = new LeaseCancelTaskVo();
                    // 委托方取消时间
                    leaseCancelTaskVo.setLeaseCancelTime(item.getOperateTime());
                    // 委托方取消
                    authInfoVoTemp.setLeaseCancelTask(leaseCancelTaskVo);
                    authInfoVoTemp.setType(8);
                    authInfoVoList.add(authInfoVoTemp);
                }else if(OperateContentEnums.FINISH.getType().equals(item.getOperateContent())){ // 收车完成
                    FinishTaskVo finishTaskVo = new FinishTaskVo();
                    // 完成时间
                    finishTaskVo.setFinishTime(item.getOperateTime());
                    // 任务完成
                    authInfoVoTemp.setFinishTask(finishTaskVo);
                    authInfoVoTemp.setType(7);
                    authInfoVoList.add(authInfoVoTemp);
                }
            }
        }
        AuthInfoVo authInfoVo = new AuthInfoVo();
        //申请授权
        ApplyAuthVo applyAuthVo = new ApplyAuthVo();
        // 申请开始时间
        applyAuthVo.setApplyTime(authorization.getApplyStartDate());
        // 申请人姓名
        applyAuthVo.setName(authorization.getApplicantName());
        // 申请人电话
        applyAuthVo.setPhoneNum(authorization.getApplicantPhone());
        // 申请人证件号
        applyAuthVo.setIdentityNum(authorization.getApplicantIdentityNum());
        // 申请备注
        applyAuthVo.setRemark(authorization.getRemark());
        // 申请定位位置
        applyAuthVo.setAddress(authorization.getAddress());
        // 获取申请时的附件信息
        List<VehiclePhotoPath> vehiclePhotoPathList = vehiclePhotoPathRepository.selectByVehiclePhotoUUID(authorization.getPhotoUuid());
        List<String> otherPhotos = new ArrayList();
        //获取附件信息
        for(VehiclePhotoPath vehiclePhotoPath : vehiclePhotoPathList){
            if(ApplyAuthFileTypeEnum.VEDIO.getCode().equals(vehiclePhotoPath.getType())){
                // 申请视频信息
                applyAuthVo.setVideo(vehiclePhotoPath.getPhotoUrl());
            } else if(ApplyAuthFileTypeEnum.FRONT_PHOTO.getCode().equals(vehiclePhotoPath.getType())){
                // 车辆正面照
                applyAuthVo.setFrontPhoto(vehiclePhotoPath.getPhotoUrl());
            } else if(ApplyAuthFileTypeEnum.EXTERIOR_PHOTO.getCode().equals(vehiclePhotoPath.getType())){
                // 车辆外观照
                applyAuthVo.setExteriorPhoto(vehiclePhotoPath.getPhotoUrl());
            } else if(ApplyAuthFileTypeEnum.OTHER_PHOTO.getCode().equals(vehiclePhotoPath.getType())){
                // 其他图片
                otherPhotos.add(vehiclePhotoPath.getPhotoUrl());
            }
        }
        applyAuthVo.setOtherPhoto(otherPhotos);
        authInfoVo.setApplyAuth(applyAuthVo);
        authInfoVo.setType(1);
        authInfoVoList.add(authInfoVo);
        return authInfoVoList;
    }
    /**
     * 用户查询历史
     *
     * @param userId 用户Id
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    public ResponseEntity<RestResponse> userSearchHistory(String userId, int page, int size){
        if(StringUtil.isNull(userId)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","当前登录用户手机号为空"),
                    HttpStatus.OK);
        }
        // 根据用户id，查找用户查看过的车辆信息
        List<RewardForVehicleVo> vehicleTaskList = vehicleTaskService.userSearchHistory(TaskStatusEnums.NORMAL.getType(), userId, page, size);
        if(vehicleTaskList != null && !vehicleTaskList.isEmpty()){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,vehicleTaskList,""),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","数据加载完毕"),
                HttpStatus.OK);
    }



}
