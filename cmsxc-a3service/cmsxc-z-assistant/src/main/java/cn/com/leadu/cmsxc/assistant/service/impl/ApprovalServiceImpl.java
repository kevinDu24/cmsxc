package cn.com.leadu.cmsxc.assistant.service.impl;

import cn.com.leadu.cmsxc.assistant.service.ApprovalService;
import cn.com.leadu.cmsxc.assistant.service.CoreSystemInterface;
import cn.com.leadu.cmsxc.assistant.service.MessageService;
import cn.com.leadu.cmsxc.assistant.util.constant.MailUtils;
import cn.com.leadu.cmsxc.common.constant.Constants;
import cn.com.leadu.cmsxc.common.constant.enums.*;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.common.util.*;
import cn.com.leadu.cmsxc.common.util.push.UmengPushUtils;
import cn.com.leadu.cmsxc.data.appbusiness.repository.RecoveryCompanyRepository;
import cn.com.leadu.cmsxc.data.appbusiness.repository.RecoveryGroupUserRepository;
import cn.com.leadu.cmsxc.data.appbusiness.repository.RecoveryTaskRepository;
import cn.com.leadu.cmsxc.data.appbusiness.repository.VehicleTaskRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.*;
import cn.com.leadu.cmsxc.data.system.repository.SystemUserRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryCompany;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryGroupUser;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTask;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskRecovery;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.ClueDetail;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.GroupUserClientVo;
import cn.com.leadu.cmsxc.pojo.appuser.entity.*;
import cn.com.leadu.cmsxc.pojo.appuser.vo.ResultVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.applydetail.*;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ApplyRecoveryInfoVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ApprovalDetailSearchVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ApprovalDetailVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ApprovalListVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import com.google.gson.Gson;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yuanzhenxia on 2018/1/15.
 *
 * 申请审批
 */
@Service
public class ApprovalServiceImpl implements ApprovalService {
    private static final Logger logger = LoggerFactory.getLogger(ApprovalServiceImpl.class);
    @Autowired
    private GpsAppInfoRepository gpsAppInfoRepository;
    @Autowired
    private ClueInforRepository clueInforRepository;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private VehicleAuthorizationRepository vehicleAuthorizationRepository;
    @Autowired
    private AuthorizationHistoryRepository authorizationHistoryRepository;
    @Autowired
    private RecoveryTaskRepository recoveryTaskRepository;
    @Autowired
    private VehiclePhotoPathRepository vehiclePhotoPathRepository;
    @Autowired
    private MessageService messageService;
    @Autowired
    private CoreSystemInterface coreSystemInterface;
    @Autowired
    private VehicleTaskRepository vehicleTaskRepository;
    @Autowired
    private RecoveryCompanyRepository recoveryCompanyRepository;
    @Autowired
    private AuthMailRecordRepository authMailRecordRepository;
    @Autowired
    private RecoveryGroupUserRepository recoveryGroupUserRepository;
    @Autowired
    private UserDeviceInfoRepository userDeviceInfoRepository;
    @Autowired
    private MessageCenterRepository messageCenterRepository;
    @Autowired
    private MessagePushHistoryRepository messagePushHistoryRepository;
    @Autowired
    private SystemUserRepository systemUserRepository;
    @Autowired
    private AuditorAreaRepository auditorAreaRepository;
    @Autowired
    private Gson gson;
    @Autowired
    private MailUtils mailUtils;

    private static String URL= "customermsg";

    /**
     * 获取待审批数量
     *
     * @param systemUser 用户信息
     * @return
     */
    public ResponseEntity<RestResponse> getCount(SystemUser systemUser){
        if(StringUtil.isNull(systemUser.getLeaseId())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","当前角色有误"),
                    HttpStatus.OK);
        }
        // 获取审核人员管辖范围
        List<String> resultList =  auditorAreaRepository.selectProvinces(systemUser.getId());
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,vehicleAuthorizationRepository.selectCount(systemUser.getLeaseId(),systemUser.getUserRole(),resultList),""),
                HttpStatus.OK);
    }

    /**
     * 分页获取审批列表（app用）
     * @param systemUser 用户信息
     * @param status 授权
     * @param condition 检索条件
     * @param flag "0":分页查询，"1"：搜索查询
     * @param page
     * @param size
     * @return
     */
    public ResponseEntity<RestResponse> getApprovalList(SystemUser systemUser,
                                                        String status,
                                                        String condition,
                                                        int flag,
                                                        int page,
                                                        int size){
        if(StringUtil.isNull(systemUser.getLeaseId())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","当前角色有误"),
                    HttpStatus.OK);
        }
        // 获取审核人员管辖范围
        List<String> resultList =  auditorAreaRepository.selectProvinces(systemUser.getId());
        List<ApprovalListVo> approvalList = vehicleAuthorizationRepository.
                selectApprovalList(systemUser.getUserRole(),resultList,status,condition,systemUser.getLeaseId(),flag,page,size);
        if(ArrayUtil.isNullOrLengthZero(approvalList)){
            if(StringUtil.isNull(condition)){
                return new ResponseEntity<RestResponse>(
                        RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","数据加载完毕"),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<RestResponse>(
                        RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未查询到车辆信息"),
                        HttpStatus.OK);
            }

        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,approvalList,""),
                HttpStatus.OK);
    }

    /**
     * 审批详情
     *
     * @param param 入参
     * @return
     */
    public ResponseEntity<RestResponse> getApprovalDetail(ApprovalDetailSearchVo param){
        // 获取车辆信息详情，获取GPS登录方式url，登录或更新查看历史表,查看车辆线索
        ApprovalDetailVo vo = getVehicleDetail(param.getTaskId(), param.getPlate());
        if(vo != null ){
            //车辆详情页面返回授权状态和备注，并获取委托方电话
            if(StringUtil.isNotNull(param.getAuthorizationId())){
                VehicleAuthorization authorizationById = vehicleAuthorizationRepository.selectByPrimaryKey(param.getAuthorizationId());
                if(authorizationById != null){
                    // 授权id
                    vo.setAuthorizationId(authorizationById.getId());
                    // 授权状态
                    vo.setAuthorizationStatus(authorizationById.getAuthorizationStatus());
                    //获取申请基本信息
                    List<AuthInfoVo> authInfoVoList = buildAuthInfo(param.getAuthorizationId(), authorizationById);
                    vo.setAuthInfo(authInfoVoList);
                }
                // 根据用户id取收车公司全称、电话及用户角色
                ApplyRecoveryInfoVo resultVo = recoveryCompanyRepository.selectRecoveryInfo(authorizationById.getUserId());
                vo.setUserName(resultVo.getUserName()); //账号对应用户姓名
                vo.setUserId(resultVo.getUserId()); //用户id
                vo.setUserRole(resultVo.getUserRole()); //用户角色
                vo.setRecoveryCompanyName(resultVo.getRecoveryCompanyName()); //收车公司名称
                vo.setRecoveryCompanyPhone(resultVo.getRecoveryCompanyPhone()); //收车公司电话
                vo.setApplicantPhone(authorizationById.getApplicantPhone()); //申请授权时填写的联系人电话
            }
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,vo,""),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未查询到车辆信息"),
                HttpStatus.OK);
    }

    /**
     * 根据任务id获取该任务的详情信息
     *
     * @param taskId 任务id
     * @param plate 车牌号
     * @return
     */
    public ApprovalDetailVo getVehicleDetail(String taskId, String plate){
        // 根据任务id查找车辆信息
        VehicleTask task = vehicleTaskRepository.selectByPrimaryKey(Long.parseLong(taskId));
        ApprovalDetailVo vo;
        if(task != null ){
            // 车辆详情信息
            vo = new ApprovalDetailVo(task);
            // 根据委托公司用户名查询下载连接
            Example example = new Example(GpsAppInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("leaseCompanyUserName", task.getLeaseCompanyUserName());
            GpsAppInfo gpsAppInfo  = gpsAppInfoRepository.selectOneByExample(example);
            if (gpsAppInfo != null){
                // 获取安卓请求还是iOS请求
                String client = httpServletRequest.getHeader("client");
                vo.setAppDownLoadUrl(ClientTypeEnums.IOS.getCode().equals(client) ? gpsAppInfo.getIosUrl() : ClientTypeEnums.ANDROID.getCode().equals(client) ? gpsAppInfo.getAndroidUrl() : "" );
            }
            // 查询车辆所有线索信息
            Example example1 = new Example(ClueInfo.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("plate", plate);
            criteria1.orEqualTo("vehicleIdentifyNum",task.getVehicleIdentifyNum());
            example1.setOrderByClause(" upload_date desc ");
            List<ClueInfo> clueList = clueInforRepository.selectByExampleList(example1);
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
            return vo;
        }
        return null;
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
     * 授权申请操作
     *
     * @param  remark 备注
     * @param  authorizationId 授权id
     * @param  userId 用户id
     * @param  plate 车牌号
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> authorization(String authorizationId, String remark, String userId, String plate, SystemUser loginUser){
        // 去除备注内容中的表情符号
        remark = CommonUtil.filterEmoji(remark);
        // 根据授权id获取授权信息
        VehicleAuthorization vehicleAuthorization = selectByAuthorizationId(authorizationId);
        if(vehicleAuthorization != null && AuthorizationStatusEnums.APPLYING.getType().equals(vehicleAuthorization.getAuthorizationStatus())){
            Date nowDate = new Date();
            // 授权状态   ---- 已授权，已完成
            List<String> statusList = new ArrayList();
            // 已授权
            statusList.add(AuthorizationStatusEnums.AUTHORIZETED.getType());
            // 已完成
            statusList.add(AuthorizationStatusEnums.FINISH.getType());
            // 查找该任务有没有授权给其他人
            List<VehicleAuthorization> vehicleAuthoList = selectByTaskIdAndAuthorizationStatus(vehicleAuthorization.getTaskId(),statusList);
            if(ArrayUtil.isNotNullAndLengthNotZero(vehicleAuthoList)){
                return new ResponseEntity<RestResponse>(
                        RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该任务已授权给其他人，不可重复授权！"),
                        HttpStatus.OK);
            }
            // 判断此任务有没有取消
            VehicleTask task = selectVehicleTeskByTaskId(String.valueOf(vehicleAuthorization.getTaskId()));
            if(TaskStatusEnums.CANCEL.getType().equals(task.getTaskStatus())){
                return new ResponseEntity<RestResponse>(
                        RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该任务已取消，不可授权！"),
                        HttpStatus.OK);
            }
            if(TaskStatusEnums.FINISH.getType().equals(task.getTaskStatus())){
                return new ResponseEntity<RestResponse>(
                        RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该任务已完成，不可授权！"),
                        HttpStatus.OK);
            }
            // 获取申请人用户信息
            SystemUser sysUser = selectSystemUserByUserId(userId);
            if(sysUser == null){
                return new ResponseEntity<RestResponse>(
                        RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","申请用户信息不存在"),
                        HttpStatus.OK);
            }
            // 根据任务id获取收车公司任务信息
            VehicleTaskRecovery vehicleTaskRecovery = selectByTaskId(vehicleAuthorization.getTaskId());
            if(vehicleTaskRecovery != null){
                // 获得授权用户所在小组id
                String groupId = getGroupIdByUserId(vehicleAuthorization.getUserId());
                // 如果获得授权用户所在小组id不为空，则 授权小组id为获得授权用户所在小组id，否则 授权小组id为空
                if(StringUtil.isNotNull(groupId)){
                    // 授权小组id为获得授权用户所在小组id
                    vehicleTaskRecovery.setAuthGroupId(groupId);
                }
                // 如果收车公司自己申请被授权，状态更新为 07:自己已授权
                if(vehicleTaskRecovery.getRecoveryCompanyId().equals(sysUser.getRecoveryCompanyId())){
                    vehicleTaskRecovery.setStatus(RecoveryTaskStatusEnums.SELFAUTHORIZATION.getType());
                    // 否则，状态更新为 03：他人已授权
                }else{
                    vehicleTaskRecovery.setStatus(RecoveryTaskStatusEnums.OTHERAUTHORIZETED.getType());
                }
                vehicleTaskRecovery.setUpdateTime(nowDate);
                // 1，更新收车公司派单表
                recoveryTaskRepository.updateByPrimaryKey(vehicleTaskRecovery);
            }
            // 更新授权状态为03：已授权
            vehicleAuthorization.setAuthorizationStatus(AuthorizationStatusEnums.AUTHORIZETED.getType());
            // 审批备注
            vehicleAuthorization.setApprovalRemark(remark);
            vehicleAuthorization.setUpdateTime(nowDate);
            // 申请结束时间
            vehicleAuthorization.setApplyEndDate(nowDate);
            // 操作时间
            vehicleAuthorization.setOperateDate(nowDate);
            // 授权失效时间  = 当前时间 + 3天 的日期 + 00:00:00
            vehicleAuthorization.setAuthorizationOutTimeDate(DateUtil.getDateTime(DateUtil.getStringDate(DateUtils.addDays(nowDate,3)).concat("000000")));
            if(task != null){
                // 调用主系统接口，获取授权书url
                String result = coreSystemInterface.getAuthPdf(URL, task.getPlate(), task.getVehicleIdentifyNum());
                // json 解析
                ResultVo res = gson.fromJson(result, ResultVo.class);
                if(!"00000000".equals(res.getCode())){
                    return new ResponseEntity<RestResponse>(
                            RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","获取授权书失败"),
                            HttpStatus.OK);
                }
//                ResultVo res = new ResultVo();
//                res.setPdfUrl("http://222.73.56.12:8588/files/download/idCard/20180227/8ee535b9-1cfa-4413-917a-3f3b1d32efe6.pdf");
                //收车公司id不为空，给对应的收车公司发邮件
                if(StringUtil.isNotNull(sysUser.getRecoveryCompanyId())){
                    RecoveryCompany recoveryCompany = recoveryCompanyRepository.selectByPrimaryKey(sysUser.getRecoveryCompanyId());
                    //如果存在该收车公司，给其发邮件
                    if(recoveryCompany != null && StringUtil.isNotNull(recoveryCompany.getContactEmail())){
                        //构建邮件发送记录表的数据
                        AuthMailRecord authMailRecord = new AuthMailRecord(task);
                        // 授权书url
                        authMailRecord.setUrl(res.getPdfUrl());
                        // 收件人邮件地址
                        authMailRecord.setEmail(recoveryCompany.getContactEmail());
                        // 发送状态----未发送
                        authMailRecord.setSendStatus(MailSendStatusEnums.NOT_SEND.getType()); //设置初始状态为未发送
                        //发送失败次数
                        authMailRecord.setRetryTimes(0); //初始重试次数为0
                        authMailRecord.setCreator(loginUser.getUserId());
                        // 邮件类型---授权操作
                        authMailRecord.setMailType(MailTypeEnum.AUTHORIZATION.getCode());
                        authMailRecord.setCreateTime(nowDate);
                        Calendar calendar = DateUtil.getCalendar(nowDate);
                        calendar.add(Calendar.DAY_OF_YEAR,3);//日+3天
                        //设定三天后的00:00:00
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        // 授权失效时间
                        authMailRecord.setFinishDate(calendar.getTime());
                        authMailRecord.setUpdateTime(authMailRecord.getCreateTime());
                        authMailRecord.setUpdater(loginUser.getUserId());
                        //2，登录授权日志表
                        authMailRecord = authMailRecordRepository.insertOne(authMailRecord);
                        try {
                            logger.info("授权成功，发送邮件给收车公司开始", authMailRecord.toString());
                            //新建一个线程，异步处理发邮箱事件，减少前台授权操作等待时间
                            try {
                                ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
                                AuthMailRecord finalAuthMailRecord = authMailRecord;
                                cachedThreadPool.execute(new Runnable() {
                                    public void run() {
                                        // do task
                                        try {
                                            // 3，发邮件操作
                                            mailUtils.sendAuthMail(finalAuthMailRecord);
                                            //发送成功，更新发送状态为已发送
                                            finalAuthMailRecord.setSendStatus(MailSendStatusEnums.SENTED.getType());
                                            finalAuthMailRecord.setUpdateTime(nowDate);
                                            authMailRecordRepository.updateByPrimaryKey(finalAuthMailRecord);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                cachedThreadPool.shutdown();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            return new ResponseEntity<RestResponse>(
                                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","授权失败,请稍后重试"),
                                    HttpStatus.OK);
                        }
                        logger.info("授权成功，发送邮件给收车公司结束", authMailRecord.toString());
                    }
                }
                // 授权书url
                vehicleAuthorization.setAuthorizationPaperUrl(res.getPdfUrl());
            }
//            vehicleAuthorization.setAuthorizationPaperUrl("http://222.73.56.12:8588/files/download/idCard/20180227/8ee535b9-1cfa-4413-917a-3f3b1d32efe6.pdf");
            // 4， 更新授权表
            vehicleAuthorizationRepository.updateByPrimaryKeySelective(vehicleAuthorization);
            // 登录授权日志表
            AuthorizationHistory history = new AuthorizationHistory();
            // 授权id
            history.setAuthorizationId(vehicleAuthorization.getId());
            // 用户id
            history.setUserId(userId);
            // 操作时间
            history.setOperateTime(nowDate);
            // 操作内容
            history.setOperateContent(OperateContentEnums.AUTHORIZETED.getType());
            // 备注
            history.setRemark(remark);
            history.setUpdateTime(nowDate);
            history.setCreateTime(nowDate);
            // 5，登录授权日志表
            authorizationHistoryRepository.insertOne(history);
            //过期时间format
            String outTimeDate = DateUtil.dateToStr(vehicleAuthorization.getAuthorizationOutTimeDate(),DateUtil.formatStr_yyyyMMddHHmm);
            // 6，向申请人发送短信提醒
            try{
                messageService.sendMessage(vehicleAuthorization.getApplicantPhone(), null, plate,outTimeDate,
                        "赏金寻车", "申请被授权，向申请人员发送短信", Constants.AUTHORIZATION_SCAN);
            }catch (Exception e){
                e.printStackTrace();
                logger.error("发送短信error",e);
                throw new CmsServiceException("发送短信失败");
            }
            // 7.给申请者推送消息，插入消息中心表
            String content1 = MessageTempletConstants.templet_201_2.replace(MessageTempletConstants.templet_plate,plate)
                    .replace(MessageTempletConstants.templet_date,outTimeDate);
            //构建申请者消息中心表实体类
            MessageCenter messageCenter1 = insertMessageCenter(content1,userId,nowDate,loginUser.getUserId(),MessageTriggerEnum.AUTH_201.getCode());
            messageCenterRepository.insertOne(messageCenter1); //登录消息中心表
            //推送
            UserDeviceInfo userDeviceInfo = selectByUserId(userId);  //获取用户登录设备信息
            if(userDeviceInfo != null && StringUtil.isNotNull(userDeviceInfo.getDeviceToken())){ //如果该用户处于登陆状态，进行消息推送
                push(userDeviceInfo.getUserId(), nowDate, userDeviceInfo.getClient().toString(), userDeviceInfo.getDeviceToken(), content1, MessageTriggerEnum.AUTH_201.getNane());
            }
            // 8，向内勤人员发送短信提醒
            GroupUserClientVo vo = recoveryGroupUserRepository.selectUserClientByRecoveryCompanyId(sysUser.getRecoveryCompanyId()); //获取内勤人员信息
            if(vo == null){ //若无对应内勤人员，则不去要做短信和消息推送，返回授权成功
                return new ResponseEntity<RestResponse>(
                        RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","授权成功"),
                        HttpStatus.OK);
            }
            try{
                messageService.sendMessage(vehicleAuthorization.getApplicantPhone(), vo.getUserId(), plate,outTimeDate,
                        "赏金寻车", "申请被授权，向内勤人员发送短信", Constants.AUTHORIZATION_SCAN);
            }catch (Exception e){
                e.printStackTrace();
                logger.error("发送短信error",e);
                throw new CmsServiceException("发送短信失败");
            }
            // 9.给内勤推送消息，插入消息中心表
            String content2 = MessageTempletConstants.templet_201_1.replace(MessageTempletConstants.templet_plate,plate)
                    .replace(MessageTempletConstants.templet_user_id,vehicleAuthorization.getUserId())
                    .replace(MessageTempletConstants.templet_date,outTimeDate);
            //构建内勤消息中心表实体类
            MessageCenter messageCenter2 = insertMessageCenter(content2,vo.getUserId(),nowDate,loginUser.getUserId(),MessageTriggerEnum.AUTH_201.getCode());
            messageCenterRepository.insertOne(messageCenter2); //登录消息中心表
            //推送
            if(StringUtil.isNotNull(vo.getDeviceToken())){ //如果内勤用户处于登录状态，进行消息推送
                push(vo.getUserId(), nowDate, vo.getClient(), vo.getDeviceToken(), content2, MessageTriggerEnum.AUTH_201.getNane());
            }
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","授权成功"),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该申请信息状态已变更，请刷新重试"),
                HttpStatus.OK);
    }

    /**
     * 消息推送，单播
     * @param userId 消息接收者
     * @param now 当前时间
     * @param client 客户端类型
     * @param deviceToken 友盟设备号
     * @param content 内容
     * @param title 标题
     */
    private void push(String userId, Date now, String client, String deviceToken,String content,String title) {
        //构建消息推送历史表信息
        MessagePushHistory messagePushHistory = new MessagePushHistory();
        messagePushHistory.setClient(client); //客户端类型
        messagePushHistory.setContent(content); //内容
        messagePushHistory.setDeviceTokens(deviceToken);
        try {
            int result = UmengPushUtils.push(PushCastEnum.UNICAST.getCode(),client,
                    deviceToken,null,title,content);
            messagePushHistory.setResultCode(result); //设定调用友盟api响应code
            //如果是返回状态200，设为成功、否则失败
            if(result == HttpStatus.OK.value()){
                messagePushHistory.setStatus(UmengPushResultEnum.SUCCESS.getCode());
            } else {
                messagePushHistory.setStatus(UmengPushResultEnum.FAIL.getCode());
            }
        } catch (Exception e) {
            messagePushHistory.setResultCode(-1); //如果异常错误，设定结果为-1
            messagePushHistory.setStatus(UmengPushResultEnum.FAIL.getCode());
            e.printStackTrace();
        } finally {
            messagePushHistory.setUpdateTime(now);
            messagePushHistory.setCreator(userId);
            messagePushHistory.setUpdater(userId);
            messagePushHistoryRepository.insertOne(messagePushHistory);
        }
    }

    /**
     * 拒绝申请操作
     *
     * @param  remark 备注
     * @param  authorizationId 授权id
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> refuse(String authorizationId, String remark, String loginUserId){
        // 去除备注内容中的表情符号
        remark = CommonUtil.filterEmoji(remark);
        // 根据授权id获取授权信息
        VehicleAuthorization vehicleAuthorization = selectByAuthorizationId(authorizationId);
        Date nowDate = new Date();
        if(vehicleAuthorization != null && AuthorizationStatusEnums.APPLYING.getType().equals(vehicleAuthorization.getAuthorizationStatus())){
            // 更新授权状态为02：已拒绝
            vehicleAuthorization.setAuthorizationStatus(AuthorizationStatusEnums.REFUSE.getType());
            // 审批备注
            vehicleAuthorization.setApprovalRemark(remark);
            vehicleAuthorization.setUpdateTime(nowDate);
            // 申请结束时间
            vehicleAuthorization.setApplyEndDate(nowDate);
            // 操作时间
            vehicleAuthorization.setOperateDate(nowDate);
            vehicleAuthorizationRepository.updateByPrimaryKeySelective(vehicleAuthorization);
            // 登录授权日志表
            AuthorizationHistory history = new AuthorizationHistory();
            // 授权id
            history.setAuthorizationId(vehicleAuthorization.getId());
            // 用户id
            //history.setUserId(vehicleAuthorization.getUserId());
            // 操作时间
            history.setOperateTime(nowDate);
            // 操作内容
            history.setOperateContent(OperateContentEnums.REFUSE.getType());
            // 备注
            history.setRemark(remark);
            history.setUpdateTime(nowDate);
            history.setCreateTime(nowDate);
            history.setCreator(loginUserId);
            history.setUpdater(loginUserId);
            authorizationHistoryRepository.insertOne(history);
            // 7.给申请者推送消息，插入消息中心表
            String content = MessageTempletConstants.templet_204.replace(MessageTempletConstants.templet_plate,
                    vehicleTaskRepository.selectByPrimaryKey(vehicleAuthorization.getTaskId()).getPlate());
            //构建申请者消息中心表实体类
            MessageCenter messageCenter1 = insertMessageCenter(content,vehicleAuthorization.getUserId(),nowDate,loginUserId,MessageTriggerEnum.AUTH_204.getCode());
            messageCenterRepository.insertOne(messageCenter1); //登录消息中心表
            //推送
            UserDeviceInfo userDeviceInfo = selectByUserId(vehicleAuthorization.getUserId());  //获取用户登录设备信息
            if(userDeviceInfo != null && StringUtil.isNotNull(userDeviceInfo.getDeviceToken())){ //如果该用户处于登陆状态，进行消息推送
                push(userDeviceInfo.getUserId(), nowDate, userDeviceInfo.getClient().toString(), userDeviceInfo.getDeviceToken(), content, MessageTriggerEnum.AUTH_204.getNane());
            }
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","拒绝成功"),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该申请信息状态已变更，请刷新重试"),
                HttpStatus.OK);
    }

    /**
     * 授权延期操作
     *
     * @param  date 授权截止日期
     * @param  authorizationId 授权id
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> delay(String authorizationId, String date, String remark,SystemUser systemUser){
        // 重新设值授权失效时间
        Date resultDate = DateUtil.getDateForString(date); //授权截止年月日
        Calendar calendar = DateUtil.getCalendar(resultDate);
        calendar.add(Calendar.DAY_OF_YEAR,1);//日+1天
        //设定时间为下一天的00:00:00
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        resultDate = calendar.getTime();
        // 去除备注内容中的表情符号
        remark = CommonUtil.filterEmoji(remark);
        Date nowDate = new Date();
        // 根据授权id获取授权信息
        VehicleAuthorization vehicleAuthorization = selectByAuthorizationId(authorizationId);
        if(vehicleAuthorization == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该申请信息状态已变更，请刷新重试"),
                    HttpStatus.OK);
        }
        if(!AuthorizationStatusEnums.AUTHORIZETED.getType().equals(vehicleAuthorization.getAuthorizationStatus())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","延期失败，只有已授权的申请才可延期"),
                    HttpStatus.OK);
        }
        if(resultDate.compareTo(vehicleAuthorization.getAuthorizationOutTimeDate()) <= 0){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","延期失败，延期日期必须大于授权失效日期"),
                    HttpStatus.OK);
        }
        // 更新授权延期状态为1：已延期
        vehicleAuthorization.setIsDelay(AuthDelayEnum.DELAY.getCode());
        vehicleAuthorization.setAuthorizationOutTimeDate(resultDate);
        // 延期备注
        vehicleAuthorization.setDelayRemark(remark);
        //延期日期
        vehicleAuthorization.setDelayDate(nowDate);
        vehicleAuthorization.setUpdateTime(nowDate);
        vehicleAuthorizationRepository.updateByPrimaryKeySelective(vehicleAuthorization);
        // 登录授权日志表
        AuthorizationHistory history = new AuthorizationHistory();
        // 授权id
        history.setAuthorizationId(vehicleAuthorization.getId());
        // 用户id
//        history.setUserId(userId);
        // 操作时间
        history.setOperateTime(nowDate);
        //延期备注
        history.setRemark(remark);
        // 操作内容
        history.setOperateContent(OperateContentEnums.DELAY.getType());
        history.setUpdateTime(nowDate);
        history.setCreateTime(nowDate);
        history.setCreator(systemUser.getUserId());
        history.setUpdater(systemUser.getUserId());
        authorizationHistoryRepository.insertOne(history);

        // 7.给申请者推送消息，插入消息中心表
        //过期时间format
        String outTimeDate = DateUtil.dateToStr(vehicleAuthorization.getAuthorizationOutTimeDate(),DateUtil.formatStr_yyyyMMddHHmm);
        String content1 = MessageTempletConstants.templet_202.replace(MessageTempletConstants.templet_plate,
                vehicleTaskRepository.selectByPrimaryKey(vehicleAuthorization.getTaskId()).getPlate())
                .replace(MessageTempletConstants.templet_date,outTimeDate);
        //构建申请者消息中心表实体类
        MessageCenter messageCenter1 = insertMessageCenter(content1,vehicleAuthorization.getUserId(),nowDate,systemUser.getUserId(),MessageTriggerEnum.AUTH_202.getCode());
        messageCenterRepository.insertOne(messageCenter1); //登录消息中心表
        //推送
        UserDeviceInfo userDeviceInfo = selectByUserId(vehicleAuthorization.getUserId());  //获取用户登录设备信息
        if(userDeviceInfo != null && StringUtil.isNotNull(userDeviceInfo.getDeviceToken())){ //如果该用户处于登陆状态，进行消息推送
            push(userDeviceInfo.getUserId(), nowDate, userDeviceInfo.getClient().toString(), userDeviceInfo.getDeviceToken(), content1, MessageTriggerEnum.AUTH_202.getNane());
        }
        // 9.给内勤推送消息，插入消息中心表
        GroupUserClientVo vo = recoveryGroupUserRepository.selectUserClientByRecoveryCompanyId(systemUser.getRecoveryCompanyId()); //获取内勤人员信息
        if(vo == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","授权延期成功"),
                    HttpStatus.OK);
        }
        String content2 = MessageTempletConstants.templet_201_1.replace(MessageTempletConstants.templet_plate,
                vehicleTaskRepository.selectByPrimaryKey(vehicleAuthorization.getTaskId()).getPlate())
                .replace(MessageTempletConstants.templet_user_id,vehicleAuthorization.getUserId())
                .replace(MessageTempletConstants.templet_date,outTimeDate);
        //构建内勤消息中心表实体类
        MessageCenter messageCenter2 = insertMessageCenter(content2,vo.getUserId(),nowDate,systemUser.getUserId(),MessageTriggerEnum.AUTH_202.getCode());
        messageCenterRepository.insertOne(messageCenter2); //登录消息中心表
        //推送
        if(StringUtil.isNotNull(vo.getDeviceToken())){ //如果内勤用户处于登录状态，进行消息推送
            push(vo.getUserId(), nowDate, vo.getClient(), vo.getDeviceToken(), content2, MessageTriggerEnum.AUTH_202.getNane());
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","授权延期成功"),
                HttpStatus.OK);
    }

    /**
     * 登录消息中心表数据
     *
     * @param content 消息内容
     * @param receiver 接收用户
     * @param nowDate 当前时间
     * @param userId 用户id
     */
    private MessageCenter insertMessageCenter(String content, String receiver, Date nowDate,String userId,String messageTrigger){
        MessageCenter messageCenter = new MessageCenter();
        messageCenter.setType(MessageTypeEnum.AUTH.getCode()); //消息类型:任务小消息
        //消息内容，把模板消息的参数替换掉
        messageCenter.setContent(content);
        //判断消息动作类型
        messageCenter.setActivity(messageTrigger);//消息触发动作:新增催收记录
        messageCenter.setReceiver(receiver); //接收者
        messageCenter.setIsReaded(MessageReadEnum.NO_READ.getCode()); //设定为未读消息
        messageCenter.setPushDate(nowDate); //推送时间
        messageCenter.setDeleteFlag(DeleteFlagEnum.ON.getCode()); //未删除
        messageCenter.setCreateTime(nowDate);
        messageCenter.setUpdateTime(nowDate);
        messageCenter.setCreator(userId);
        messageCenter.setUpdater(userId);
        return messageCenter;
    }

    /**
     * 根据授权ID获取授权信息
     *
     * @param authorizationId 授权id
     * @return
     */
    public VehicleAuthorization selectByAuthorizationId(String authorizationId){
        if(StringUtil.isNotNull(authorizationId)) {
            Example example = new Example(VehicleAuthorization.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id", authorizationId);
            VehicleAuthorization recoveryCompany = vehicleAuthorizationRepository.selectOneByExample(example);
            return recoveryCompany;
        }
        return null;
    }

    /**
     * 根据user_id获取用户登录设备信息
     * @param userId 用户id
     * @return
     */
    public UserDeviceInfo selectByUserId(String userId){
        if(StringUtil.isNotNull(userId)) {
            Example example = new Example(UserDeviceInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId", userId);
            return userDeviceInfoRepository.selectOneByExample(example);
        }
        return null;
    }

    /**
     * 根据用户id（手机号）获取用户信息
     * @param userId 用户id
     * @return
     */
    private SystemUser selectSystemUserByUserId(String userId){
        if(StringUtil.isNotNull(userId)) {
            Example example = new Example(SystemUser.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId", userId);
            return systemUserRepository.selectOneByExample(example);
        }
        return null;
    }
    /**
     * 根据id获取车辆任务工单信息
     * @param taskId 任务id
     * @return
     */
    public VehicleTask selectVehicleTeskByTaskId(String taskId){
        if(StringUtil.isNotNull(taskId)) {
            Example example = new Example(VehicleTask.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id", taskId);
            return vehicleTaskRepository.selectOneByExample(example);
        }
        return null;
    }

    /**
     * 根据授权ID获取授权信息
     *
     * @param taskId 工单id
     * @param authorizationStatusList 授权状态
     * @return
     */
    public List<VehicleAuthorization> selectByTaskIdAndAuthorizationStatus(Long taskId, List<String> authorizationStatusList){
        if(StringUtil.isNotNull(taskId) && authorizationStatusList != null && !authorizationStatusList.isEmpty()) {
            Example example = new Example(VehicleAuthorization.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("taskId", String.valueOf(taskId));
            criteria.andIn("authorizationStatus", authorizationStatusList);
            List<VehicleAuthorization> recoveryCompanieList = vehicleAuthorizationRepository.selectByExampleList(example);
            if (ArrayUtil.isNotNullAndLengthNotZero(recoveryCompanieList))
                return recoveryCompanieList;
        }
        return null;
    }

    /**
     * 根据授权ID获取授权信息
     *
     * @param taskId 任务id
     * @return
     */
    private VehicleTaskRecovery selectByTaskId(Long taskId){
        if(StringUtil.isNotNull(taskId)) {
            Example example = new Example(VehicleTaskRecovery.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("taskId", String.valueOf(taskId));
            example.setOrderByClause(" create_time desc ");
            return recoveryTaskRepository.selectOneByExample(example);
        }
        return null;
    }

    /**
     * 根据用户名获取该用户所属小组id
     *
     * @return
     */
    private String getGroupIdByUserId(String userId){
        if(StringUtil.isNotNull(userId)) {
            Example example = new Example(RecoveryGroupUser.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("groupUserId", userId);
            RecoveryGroupUser recoveryGroupUser = recoveryGroupUserRepository.selectOneByExample(example);
            if(recoveryGroupUser == null){
                return null;
            }
            return recoveryGroupUser.getRecoveryGroupId();
        }
        return null;
    }
}
