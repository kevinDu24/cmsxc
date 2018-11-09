package cn.com.leadu.cmsxc.external.service.impl;

import cn.com.leadu.cmsxc.common.constant.Constants;
import cn.com.leadu.cmsxc.common.constant.enums.*;
import cn.com.leadu.cmsxc.common.redis.RedisRepository;
import cn.com.leadu.cmsxc.common.util.*;
import cn.com.leadu.cmsxc.common.util.push.UmengPushUtils;
import cn.com.leadu.cmsxc.data.appbusiness.repository.*;
import cn.com.leadu.cmsxc.data.appuser.repository.*;
import cn.com.leadu.cmsxc.data.system.repository.SystemUserRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.ResponseFailEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.external.service.MessageService;
import cn.com.leadu.cmsxc.external.service.TaskPushService;
import cn.com.leadu.cmsxc.external.util.constant.EncodeUtils;
import cn.com.leadu.cmsxc.external.util.constant.MailUtils;
import cn.com.leadu.cmsxc.external.util.constant.RedisKeys;
import cn.com.leadu.cmsxc.external.util.constant.enums.*;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.*;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.*;
import cn.com.leadu.cmsxc.pojo.appuser.entity.*;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cn.com.leadu.cmsxc.common.util.DateUtil.getDateTime;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 委托机构推送Service实现类
 */
@Service
public class TaskPushServiceImpl implements TaskPushService {
    private static final Logger logger = LoggerFactory.getLogger(TaskPushServiceImpl.class);
    @Autowired
    private VehicleTaskRepository vehicleTaskRepository;
    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private ClueInforRepository clueInforRepository;
    @Autowired
    private GpsAppInfoRepository gpsAppInfoRepository;
    @Autowired
    private RecoveryCompanyTempRepository recoveryCompanyTempRepository;
    @Autowired
    private VehicleTaskRecoveryRepository vehicleTaskRecoveryRepository;
    @Autowired
    private LeaseCompanyRepository leaseCompanyRepository;
    @Autowired
    private RecoveryCompanyRepository recoveryCompanyRepository;
    @Autowired
    private SystemUserRepository systemUserRepository;
    @Autowired
    private VehicleAuthorizationRepository vehicleAuthorizationRepository;
    @Autowired
    private AuthorizationHistoryRepository authorizationHistoryRepository;
    @Autowired
    private RecoveryLeaseRelationRepository recoveryLeaseRelationRepository;
    @Autowired
    private MessageCenterRepository messageCenterRepository;
    @Autowired
    private MessagePushHistoryRepository messagePushHistoryRepository;
    @Autowired
    private RecoveryGroupUserRepository recoveryGroupUserRepository;
    @Autowired
    private UserDeviceInfoRepository userDeviceInfoRepository;
    @Autowired
    private VehicleTaskGroupRepository vehicleTaskGroupRepository;
    @Autowired
    private AuthMailRecordRepository authMailRecordRepository;
    @Autowired
    private MailUtils mailUtils;
    @Autowired
    private MessageService messageService;
    //主系统的核销公司的收车公司id
    private final String recovery_company_id_hx = "04bv3cf22da611e8a82e0242ac110002";
    //先锋太盟委托公司在赏金系统的用户名
    private final String lease_taimeng = "taimeng";
    /**
     * 委托机构获取token
     *
     * @return
     */
    public ResponseEntity<RestResponse> getToken(String userName, String password){
        if(StringUtil.isNull(userName) || StringUtil.isNull(password)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","用户名或密码不可为空"),
                    HttpStatus.OK);
        }
        SystemUser systemUser = findSysUserByUserId(userName);
        //如果用户名不存在或者此用户类型非委托公司
        if(systemUser == null || !UserRoleEnums.LEASE_ADMIN.getType().equals(systemUser.getUserRole())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","用户名不正确"),
                    HttpStatus.OK);
        }
        String pwd = EncodeUtils.MD5(EncodeUtils.getBase64(password));
        if(!systemUser.getUserPassword().equals(pwd)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","密码不正确"),
                    HttpStatus.OK);
        }
        String uuid = UUID.randomUUID().toString().replace("-", "");
        redisRepository.save(RedisKeys.lease_recovery + userName, userName + ":" + uuid, 60*60*24);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,userName + ":" + uuid,"获取token成功"),
                HttpStatus.OK);
    }

    /**
     * 委托机构推送工单
     *
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> pushVehicleTask(String token, VehicleTaskPushVo vo){
        boolean tokenFlag = verifyToken(token);
        if(!tokenFlag){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","token验证失败"),
                    HttpStatus.OK);
        }
        String userName = token.split(":")[0];
        //从token中获取委托公司用户名
        vo.setLeaseCompanyUserName(userName);
        //找出委托公司用户名
        SystemUser systemUser = findSysUserByUserId(userName);
        if(systemUser != null && StringUtil.isNotNull(systemUser.getLeaseId())){
            LeaseCompany leaseCompany = leaseCompanyRepository.selectByPrimaryKey(systemUser.getLeaseId());
            vo.setLeaseCompanyName(leaseCompany.getLeaseFullName());
        }
        //必要参数为空提示信息，初始为""
        StringBuffer param = new StringBuffer("");
        checkPushTaskParam(vo, param);
        if(!param.toString().isEmpty()){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","推送失败，" + param + "不可为空"),
                    HttpStatus.OK);
        }
        //如果是太盟推过来的数据并且是核销的数据，设定收车公司id为null
        if(lease_taimeng.equals(userName)&&recovery_company_id_hx.equals(vo.getRecoveryCompanyId())){
            vo.setRecoveryCompanyId(null);
        }
        //判断工单表中是否存在此车牌号或车架号
        Example example = new Example(VehicleTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("plate", vo.getPlate());
        criteria.orEqualTo("vehicleIdentifyNum",vo.getVehicleIdentifyNum());
        example.setOrderByClause(" update_time desc ");
        List<VehicleTask> vehicleTaskList = vehicleTaskRepository.selectByExampleList(example);
        //如果存在
        if(vehicleTaskList != null && !vehicleTaskList.isEmpty()){
            VehicleTask task = vehicleTaskList.get(0);
            //只有工单状态为取消的收车任务才可以再推送
            if(!TaskStatusEnums.CANCEL.getType().equals(task.getTaskStatus())){
                return new ResponseEntity<RestResponse>(
                        RestResponseGenerator.genResponse(ResponseFailEnum.LEASE_PUSH_ERROR,"","该收车任务已存在"),
                        HttpStatus.OK);
            }
        }
        //判断入参收车公司id(即主系统)是否存在
        RecoveryCompany recoveryCompany = null;
        if(!StringUtil.isNull(vo.getRecoveryCompanyId())){
            recoveryCompany = recoveryCompanyRepository.selectByPrimaryKey(vo.getRecoveryCompanyId());
            if(recoveryCompany == null){
                return new ResponseEntity<RestResponse>(
                        RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未找到该收车公司"),
                        HttpStatus.OK);
            }
        }
        //构建任务工单表数据
        VehicleTask task = buildVehicleTask(vo);
        VehicleTask vehicleTask = vehicleTaskRepository.insertOne(task);
        //db操作，insert工单表和收车公司任务表
        saveRecoveryBak(vehicleTask.getId(),recoveryCompany,vo);
        // 以下为给收车公司内勤人员推送消息及发送短信
        // 推送内容
        String content = MessageTempletConstants.templet_100.replace(MessageTempletConstants.templet_plate,vo.getPlate());
        Date nowDate = new Date();
        // 获取内勤人员信息    ----- 推送消息
        GroupUserClientVo groupUserClientVo = getUserClientByRecoveryCompanyId(vo.getRecoveryCompanyId());
        if(groupUserClientVo != null){
            // 登录消息中心表数据----内勤人员
            MessageCenter messageCenter = insertMessageCenter(content,groupUserClientVo.getUserId(),nowDate,userName, MessageTriggerEnum.TASK_100.getCode());
            messageCenterRepository.insertOne(messageCenter);
            // 推送消息 --- 内勤人员
            List<GroupUserClientVo>  groupUserClientVoList = new ArrayList<>();
            groupUserClientVoList.add(groupUserClientVo);
            push(userName, groupUserClientVoList, content, groupUserClientVo.getClient(), PushCastEnum.UNICAST.getCode(),MessageTriggerEnum.TASK_100.getNane(), groupUserClientVo.getDeviceToken());
            // 推送任务时给内勤人员发短信
            // 短信内容
            String messageContent = "【赏金寻车】" + content;
            if(StringUtil.isNotNull(groupUserClientVo.getUserId())){
                try{
                    messageService.sendMessage(groupUserClientVo.getUserId(),messageContent,"赏金寻车","委托公司推送收车时内勤人员发送短信", Constants.SENDMESSAGE_TASK_PUSH_SCAN);
                }catch (Exception e){
                    e.printStackTrace();
                    logger.error("发送短信error",e);
                }
            }
        }
        // 6, 若此工单推送给指定收车公司，给收车公司发邮件
        if(!StringUtil.isNull(vo.getRecoveryCompanyId())){
            sendMailToRecoveryCompany(task,vo.getRecoveryCompanyId(),userName,MailTypeEnum.PUSH.getCode());
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","推送成功"),
                HttpStatus.OK);
    }

    /**
     * 委托机构推送收车app信息
     *
     * @return
     */
    public ResponseEntity<RestResponse> pushAppInfo(String token, AppInfoPushVo vo){
        boolean tokenFlag = verifyToken(token);
        if(!tokenFlag){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","token验证失败"),
                    HttpStatus.OK);
        }
        String userName = token.split(":")[0];
        //从token中获取委托公司用户名
        vo.setLeaseCompanyUserName(userName);
        //必要参数为空提示信息，初始为""
        StringBuffer param = new StringBuffer("");
        checkAppInfoParam(vo,param);
        if(!param.toString().isEmpty()){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","推送失败，" + param + "不可为空"),
                    HttpStatus.OK);
        }
        //判断app信息表中是否存在此委托机构的收车app信息
        Example example = new Example(GpsAppInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("leaseCompanyUserName", vo.getLeaseCompanyUserName());
        example.setOrderByClause(" update_time desc ");
        GpsAppInfo gpsAppInfo =gpsAppInfoRepository.selectOneByExample(example);
        //如果不存在,insert
        Date date = new Date();
        if(gpsAppInfo == null){
            gpsAppInfo = new GpsAppInfo();
            gpsAppInfo.setCreator(vo.getLeaseCompanyUserName());
            gpsAppInfo.setUpdater(vo.getLeaseCompanyUserName());
            gpsAppInfo.setCreateTime(date);
            gpsAppInfo.setUpdateTime(date);
            gpsAppInfo.setLeaseCompanyUserName(vo.getLeaseCompanyUserName());
            gpsAppInfo.setAndroidUrl(vo.getAndroidUrl());
            gpsAppInfo.setIosUrl(vo.getIosUrl());
            gpsAppInfoRepository.insertOne(gpsAppInfo);
        } else {
            //如果存在update
            gpsAppInfo.setUpdater(vo.getLeaseCompanyUserName());
            gpsAppInfo.setUpdateTime(date);
            gpsAppInfo.setLeaseCompanyUserName(vo.getLeaseCompanyUserName());
            gpsAppInfo.setAndroidUrl(vo.getAndroidUrl());
            gpsAppInfo.setIosUrl(vo.getIosUrl());
            gpsAppInfoRepository.updateByPrimaryKey(gpsAppInfo);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","推送成功"),
                HttpStatus.OK);
    }

    /**
     * 委托机构推送新的收车公司
     *
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> pushRecoveryCompany(String token, RecoveryCompanyPushVo vo){
        boolean tokenFlag = verifyToken(token);
        if(!tokenFlag){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","token验证失败"),
                    HttpStatus.OK);
        }
        //必要参数为空提示信息，初始为""
        StringBuffer param = new StringBuffer("");
        checkAddCompanyParam(vo, param);
        if(!param.toString().isEmpty()){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","推送失败，" + param + "不可为空"),
                    HttpStatus.OK);
        }
        String userName = token.split(":")[0];
        //判断入参收车公司id是否存在
        Example exam = new Example(RecoveryCompany.class);
        Example.Criteria criter = exam.createCriteria();
        criter.orEqualTo("recoveryFullName", vo.getRecoveryFullName());
        RecoveryCompany recoveryCompany = recoveryCompanyRepository.selectOneByExample(exam);
        if (recoveryCompany == null){
            //db操作,插入收车公司表
            recoveryCompany = saveRecoveryCompany(vo);
        }
        //db操作,插入收车公司委托公司关系表
        saveRecoveryLeaseRelation(recoveryCompany.getId(),userName);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,recoveryCompany.getId(),"推送成功"),
                HttpStatus.OK);
    }

    /**
     * 收车任务取消时，确认该收车任务是否授权给其他收车公司
     *
     * @param token 访问凭证
     * @param vo 参数信息
     * @return
     */
    public ResponseEntity<RestResponse> cancelComfirm(String token, RecoveryStatusPushVo vo){
        boolean tokenFlag = verifyToken(token);
        if(!tokenFlag){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","token验证失败"),
                    HttpStatus.OK);
        }
        CancelConfirmVo cancelConfirmVo = null;
        //1，根据车牌号和车牌号查找任务工单最新一条数据
        VehicleTask task = getVehicleTask(vo.getPlate(),vo.getVehicleIdentifyNum());
        String [] values = token.split(":");
        String userName = values[0];
        if(task == null || !task.getLeaseCompanyUserName().equals(userName)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseFailEnum.LEASE_PUSH_ERROR,"","该收车任务不存在"),
                    HttpStatus.OK);
        }
        // 获取此工单是否有已授权状态
        VehicleAuthorization vehicleAuthorization = getByUserIdAndTaskIdAndStatus(String.valueOf(task.getId()), AuthorizationStatusEnums.AUTHORIZETED.getType());
        if(vehicleAuthorization != null){
            cancelConfirmVo = new CancelConfirmVo();
            // 获取被授权用户的收车公司id
            SystemUser user = selectSystemUserByUserId(vehicleAuthorization.getUserId());
            // 根据收车公司id获取收车公司信息
            RecoveryCompany recoveryCompany = getByRecoveryCompanyById(user.getRecoveryCompanyId());
            if(recoveryCompany != null){
                cancelConfirmVo.setRecoveryCompanyName(recoveryCompany.getRecoveryFullName());
                cancelConfirmVo.setTelPhoneNum(recoveryCompany.getContactPhone());
                cancelConfirmVo.setContactEmail(recoveryCompany.getContactEmail());
            }else{
                cancelConfirmVo.setRecoveryCompanyName(user.getUserName());
                cancelConfirmVo.setTelPhoneNum(user.getUserPhone());
            }
            cancelConfirmVo.setOutTimeDate(vehicleAuthorization.getAuthorizationOutTimeDate());
            cancelConfirmVo.setStartDate(vehicleAuthorization.getOperateDate());
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,cancelConfirmVo,""),
                HttpStatus.OK);

    }
    /**
     * 收车任务取消时，主系统推送取消时间
     *
     * @param token
     * @param vo
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> pushRecoveryCanal(String token, RecoveryStatusPushVo vo){
        Date nowDate = new Date();
        boolean tokenFlag = verifyToken(token);
        if(!tokenFlag){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","token验证失败"),
                    HttpStatus.OK);
        }
        String userName = token.split(":")[0];
        //1，根据车牌号和车牌号查找任务工单最新一条数据
        VehicleTask task = getVehicleTask(vo.getPlate(),vo.getVehicleIdentifyNum());
        if(task == null || !task.getLeaseCompanyUserName().equals(userName)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseFailEnum.LEASE_PUSH_ERROR,"","该收车任务不存在"),
                    HttpStatus.OK);
        }else if(TaskStatusEnums.CANCEL.getType().equals(task.getTaskStatus())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseFailEnum.LEASE_PUSH_ERROR,"","该任务已取消，不要重复操作"),
                    HttpStatus.OK);
        }else if(TaskStatusEnums.FINISH.getType().equals(task.getTaskStatus())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseFailEnum.LEASE_PUSH_ERROR,"","该任务已完成，不可取消"),
                    HttpStatus.OK);
        }
        // 任务状态 --- 已取消
        task.setTaskStatus(TaskStatusEnums.CANCEL.getType());
        Date cancelDate = getDateTime(vo.getCanalDate());
        //取消原因
        task.setCancelReason(vo.getCancelReason());
        // 取消时间
        task.setUpdateTime(cancelDate);
        task.setUpdater(userName);
        // 1.更新任务工单表
        vehicleTaskRepository.updateByPrimaryKey(task);
        //根据任务id和收车公司id获取收车公司派单信息
        VehicleTaskRecovery vehicleTaskRecovery = getByTaskId(String.valueOf(task.getId()));
        String recoveryCompanyId = "";
        String recoveryTaskId = "";
        if(vehicleTaskRecovery != null){
            recoveryCompanyId = vehicleTaskRecovery.getRecoveryCompanyId();
            recoveryTaskId = vehicleTaskRecovery.getId();
            // 状态---已取消
            vehicleTaskRecovery.setStatus(RecoveryTaskStatusEnums.CANCEL.getType());
            // 取消时间
            vehicleTaskRecovery.setUpdateTime(cancelDate);
            vehicleTaskRecovery.setUpdater(userName);
            // 2.更新收车公司派单表
            vehicleTaskRecoveryRepository.updateByPrimaryKey(vehicleTaskRecovery);
        }
        // 获取此工单是否有已授权状态
        VehicleAuthorization vehicleAuthorization = getByUserIdAndTaskIdAndStatus(String.valueOf(task.getId()), AuthorizationStatusEnums.AUTHORIZETED.getType());
        String authrizationUserId = "";
        if(vehicleAuthorization != null){
            authrizationUserId = vehicleAuthorization.getUserId();
            // 授权状态---委托公司取消
            vehicleAuthorization.setAuthorizationStatus(AuthorizationStatusEnums.LEASECANCEL.getType());
            // 操作时间----取消时间
            vehicleAuthorization.setOperateDate(cancelDate);
            // 取消证书url
            vehicleAuthorization.setCancelPaperUrl(vo.getCancelPaperUrl());
            // 取消原因
            vehicleAuthorization.setCancelReason(vo.getCancelReason());
            vehicleAuthorization.setUpdateTime(cancelDate);
            vehicleAuthorization.setUpdater(userName);
            // 3,更新授权表
            vehicleAuthorizationRepository.updateByPrimaryKeySelective(vehicleAuthorization);
            // 登录授权日志表
            AuthorizationHistory history = new AuthorizationHistory();
            // 授权id
            history.setAuthorizationId(vehicleAuthorization.getId());
            // 用户id
            history.setUserId(vehicleAuthorization.getUserId());
            // 操作时间
            history.setOperateTime(cancelDate);
            // 操作内容
            history.setOperateContent(OperateContentEnums.LEASECANCEL.getType());
            // 备注
            history.setRemark(OperateContentEnums.LEASECANCEL.getValue());
            history.setCreator(userName);
            history.setCreateTime(nowDate);
            history.setUpdateTime(nowDate);
            history.setUpdater(userName);
            // 4.登录操作日志表
            authorizationHistoryRepository.insertOne(history);
        }
        // 获取任务分组信息
        Example example = new Example(VehicleTaskGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("vehicleTaskRecoveryId", recoveryTaskId);
        VehicleTaskGroup vehicleTaskGroup =  vehicleTaskGroupRepository.selectOneByExample(example);
        String groupId = "";
        // 获取小组id
        if(vehicleTaskGroup != null){
            groupId = vehicleTaskGroup.getGroupId();
        }
        // 5，任务取消，给小组成员+ 内勤人员+ 已授权人员 推送消息
        createRecoveryCancelPush(groupId,vo.getPlate(),userName,authrizationUserId,recoveryCompanyId);
        // 6, 任务取消，给收车公司发邮件
        sendMailToRecoveryCompany(task,recoveryCompanyId,userName,MailTypeEnum.CANCEL.getCode());
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","推送成功"),
                HttpStatus.OK);
    }
    /**
     * 收车任务完成时，主系统推送完成时间
     *
     * @param token
     * @param vo
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> pushRecoveryFinish(String token, RecoveryStatusPushVo vo){
        boolean tokenFlag = verifyToken(token);
        if(!tokenFlag){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","token验证失败"),
                    HttpStatus.OK);
        }
        //1，根据车牌号和车牌号查找任务工单最新一条数据
        VehicleTask task = getVehicleTask(vo.getPlate(), vo.getVehicleIdentifyNum());
        String userName = token.split(":")[0];
        if(task == null || !task.getLeaseCompanyUserName().equals(userName)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseFailEnum.LEASE_PUSH_ERROR,"","该收车任务不存在"),
                    HttpStatus.OK);
        }else if(TaskStatusEnums.CANCEL.getType().equals(task.getTaskStatus())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseFailEnum.LEASE_PUSH_ERROR,"","该任务已取消"),
                    HttpStatus.OK);
        }else if(TaskStatusEnums.FINISH.getType().equals(task.getTaskStatus())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseFailEnum.LEASE_PUSH_ERROR,"","该任务已完成，不可重复操作"),
                    HttpStatus.OK);
        }
        // 任务状态 --- 已完成
        task.setTaskStatus(TaskStatusEnums.FINISH.getType());
        // 完成时间
        Date finishDate = getDateTime(vo.getFinishDate());
        task.setUpdateTime(finishDate);
        // 1.更新任务工单表
        vehicleTaskRepository.updateByPrimaryKey(task);
        // 获取此工单是否有已授权状态
        VehicleAuthorization vehicleAuthorization = getByUserIdAndTaskIdAndStatus(String.valueOf(task.getId()), AuthorizationStatusEnums.AUTHORIZETED.getType());
        //根据任务id和收车公司id获取收车公司派单信息
        VehicleTaskRecovery vehicleTaskRecovery = getByTaskId(String.valueOf(task.getId()));
        if(vehicleTaskRecovery != null){
            if(vehicleAuthorization != null){
                // 获取被授权用户的收车公司id
                SystemUser user = selectSystemUserByUserId(vehicleAuthorization.getUserId());
                // 如果收车公司自己申请被授权，状态更新为 04:自己已完成
                if(vehicleTaskRecovery.getRecoveryCompanyId().equals(user.getRecoveryCompanyId())){
                    vehicleTaskRecovery.setStatus(RecoveryTaskStatusEnums.SELFFINISH.getType());
                    // 否则，状态更新为 05：他人已完成
                }else{
                    vehicleTaskRecovery.setStatus(RecoveryTaskStatusEnums.OTHERFINISH.getType());
                }
                // 获得授权用户所在小组id
                String groupId = getGroupIdByUserId(vehicleAuthorization.getUserId());
                // 如果获得授权用户所在小组id不为空，则 完成小组id为获得授权用户所在小组id，否则 完成小组id为空
                if(StringUtil.isNotNull(groupId)){
                    // 完成小组id为获得授权用户所在小组id
                    vehicleTaskRecovery.setFinishGroupId(groupId);
                }
                // 如果没有已授权状态，状态更新为 04:自己已完成
            }else{
                // 取得任务分组状况
                List<VehicleTaskGroup> vehicleTaskGroupList = getByVehicleTaskRecoveryId(vehicleTaskRecovery.getId());
                // 如果任务已经分配小组，则完成小组id为分配小组id，否则完成小组id为空
                if(ArrayUtil.isNotNullAndLengthNotZero(vehicleTaskGroupList)){
                    vehicleTaskRecovery.setFinishGroupId(vehicleTaskGroupList.get(0).getGroupId());
                }
                vehicleTaskRecovery.setStatus(RecoveryTaskStatusEnums.SELFFINISH.getType());
            }
            vehicleTaskRecovery.setUpdateTime(getDateTime(vo.getFinishDate()));
            // 2.更新收车公司派单表
            vehicleTaskRecoveryRepository.updateByPrimaryKey(vehicleTaskRecovery);
        }
        if(vehicleAuthorization != null){
            // 3.更新授权状态为已完成
            vehicleAuthorization.setAuthorizationStatus(AuthorizationStatusEnums.FINISH.getType());
            // 操作时间
            vehicleAuthorization.setOperateDate(finishDate);
            vehicleAuthorization.setUpdateTime(finishDate);
            vehicleAuthorizationRepository.updateByPrimaryKeySelective(vehicleAuthorization);
            // 登录授权日志表
            AuthorizationHistory history = new AuthorizationHistory();
            // 授权id
            history.setAuthorizationId(vehicleAuthorization.getId());
            // 用户id
            history.setUserId(vehicleAuthorization.getUserId());
            // 操作时间
            history.setOperateTime(finishDate);
            // 操作内容
            history.setOperateContent(OperateContentEnums.FINISH.getType());
            // 备注
            history.setRemark(OperateContentEnums.FINISH.getValue());
            authorizationHistoryRepository.insertOne(history);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","推送成功"),
                HttpStatus.OK);
    }

    /**
     * 主系统收车车辆gps在线变离线时的推送接口
     *
     * @param token
     * @param vehicleIdentifyNum 车架号
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> pushGpsOnline(String token, String vehicleIdentifyNum){
        boolean tokenFlag = verifyToken(token);
        if(!tokenFlag){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","token验证失败"),
                    HttpStatus.OK);
        }
        if(StringUtil.isNull(vehicleIdentifyNum)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","车架号不可为空"),
                    HttpStatus.OK);
        }
        //判断工单表中是否存在此车架号
        Example example = new Example(VehicleTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("vehicleIdentifyNum", vehicleIdentifyNum);
        criteria.andEqualTo("taskStatus", TaskStatusEnums.NORMAL.getType());
        VehicleTask task = vehicleTaskRepository.selectOneByExample(example);
        //如果没有找到，则推送失败
        String userName = token.split(":")[0];
        if(task == null || !task.getLeaseCompanyUserName().equals(userName)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseFailEnum.LEASE_PUSH_ERROR,"","该收车任务不存在"),
                    HttpStatus.OK);
        }
        //更新工单表【gps_online】状态为离线
        task.setGpsOnline(GpsOnlineEnums.OUT.getType());
        vehicleTaskRepository.updateByPrimaryKey(task);
        //获取收车公司任务表数据
        VehicleTaskRecovery vehicleTaskRecovery = getRecoveryCompanyTask(task.getId());
        //如果该任务已分配给收车公司
        if(vehicleTaskRecovery != null){
            //任务开始时间
            Date startDate = vehicleTaskRecovery.getStartTime();
            Calendar calendar = DateUtil.getCalendar(startDate);
            calendar.add(Calendar.DAY_OF_YEAR,14);//该任务有效期为14天
            //独家期间的结束日期
            Date failureTime = calendar.getTime();
            vehicleTaskRecovery.setFailureTime(failureTime);
            //更新收车公司任务表
            vehicleTaskRecoveryRepository.updateByPrimaryKey(vehicleTaskRecovery);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","推送成功"),
                HttpStatus.OK);
    }

    /**
     * 主系统收车车辆服务费变更推送
     *
     * @param token
     * @param vehicleIdentifyNum 车架号
     * @return
     */
    public ResponseEntity<RestResponse> pushServiceFee(String token, String vehicleIdentifyNum, String serviceFee){
        boolean tokenFlag = verifyToken(token);
        if(!tokenFlag){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","token验证失败"),
                    HttpStatus.OK);
        }
        if(StringUtil.isNull(vehicleIdentifyNum)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","车架号不可为空"),
                    HttpStatus.OK);
        }
        if(StringUtil.isNull(serviceFee)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","服务费不可为空"),
                    HttpStatus.OK);
        }
        //判断工单表中是否存在此车架号
        Example example = new Example(VehicleTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("vehicleIdentifyNum", vehicleIdentifyNum);
        criteria.andEqualTo("taskStatus", TaskStatusEnums.NORMAL.getType());
        VehicleTask task = vehicleTaskRepository.selectOneByExample(example);
        String userName = token.split(":")[0];
        //如果没有找到，则推送失败
        if(task == null || !task.getLeaseCompanyUserName().equals(userName)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseFailEnum.LEASE_PUSH_ERROR,"","该收车任务不存在"),
                    HttpStatus.OK);
        }
        //更新工单表服务费和价格区间
        Double fee = new Double(serviceFee);
        task.setServiceFee(fee);
        task.setPriceRange(getPriceRange(fee));
        vehicleTaskRepository.updateByPrimaryKey(task);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","推送成功"),
                HttpStatus.OK);
    }

    /**
     * 赏金寻车二期上线时，填充收车公司老板注册码
     *
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> buildBossCode(){
        Example example = new Example(RecoveryCompany.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIsNull("bossRegisterCode");
        List<RecoveryCompany> recoveryCompanyList = recoveryCompanyRepository.selectByExampleList(example);
        if(ArrayUtil.isNullOrLengthZero(recoveryCompanyList)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","没有需要处理的数据"),
                    HttpStatus.OK);
        }
        List<String> codes = new ArrayList();
        //取出所有收车公司注册码
        codes = recoveryCompanyRepository.selectRegisterCode();
        List<RecoveryCompany> resultList = new ArrayList();
        for(RecoveryCompany item : recoveryCompanyList){
            String bossRegisterCode = null; //初始化老板注册码
            //循环给主管和业务员注册码赋值
            do
            {
                String code = RandomUtil.getRegisterCode(); //生成随机数
                if(!codes.contains(code)){
                    bossRegisterCode = code;
                    codes.add(code);
                }
            }
            while (bossRegisterCode == null);// 直到赋值成功，结束循环
            // 老板注册码
            item.setBossRegisterCode(bossRegisterCode);
            resultList.add(item);
        }
        recoveryCompanyRepository.updateListByMapper(resultList);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","更新完毕，更新数据" + resultList.size() + "条"),
                HttpStatus.OK);
    }

    /**
     * 赏金寻车二期上线时，寄存数据价格区间重新修正
     *
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> rebuildPriceRange(){
        List<String> priceList = new ArrayList();
        priceList.add(ServiceFeeEnum.RANGE_C.value());
        priceList.add(ServiceFeeEnum.RANGE_D.value());
        Example example = new Example(VehicleTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("priceRange", priceList);
        List<VehicleTask> vehicleTaskList = vehicleTaskRepository.selectByExampleList(example);
        if(ArrayUtil.isNullOrLengthZero(vehicleTaskList)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","没有需要更新的数据"),
                    HttpStatus.OK);
        }
        List<VehicleTask> resultList = new ArrayList();
        for(VehicleTask item : vehicleTaskList){
            Double serviceFee = item.getServiceFee();
            String priceRange = getPriceRange(serviceFee);
            item.setPriceRange(priceRange);
            resultList.add(item);
        }
        vehicleTaskRepository.updateListByMapper(resultList);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","更新完毕，更新数据" + resultList.size() + "条"),
                HttpStatus.OK);
    }

    /**
     * 赏金三期更新工单表历史数据三址、fp所在省份
     *
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> buildHistoryData(){
        vehicleTaskRepository.updateHistoryData();
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","更新完毕"),
                HttpStatus.OK);
    }

    /**
     * 根据车牌号和车架号查找任务工单
     * @param plate 车牌号
     * @param vehicleIdentifyNum 车架号
     * @return
     */
    private VehicleTask getVehicleTask(String plate, String vehicleIdentifyNum){
        VehicleTask task = null;
        Example example = new Example(VehicleTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("plate", plate);
        criteria.andEqualTo("vehicleIdentifyNum", vehicleIdentifyNum);
        example.setOrderByClause(" update_time desc ");
        List<VehicleTask> vehicleTaskList = vehicleTaskRepository.selectByExampleList(example);
        //如果存在
        if(vehicleTaskList != null && !vehicleTaskList.isEmpty())
            task = vehicleTaskList.get(0);
        return  task;
    }
    /**
     * 根据任务id获取收车公司派单信息
     * @param taskId 任务id
     * @return
     */
    private VehicleTaskRecovery getByTaskId(String taskId){
        Example example = new Example(VehicleTaskRecovery.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", taskId);
        List<VehicleTaskRecovery> vehicleTaskRecoveryList = vehicleTaskRecoveryRepository.selectByExampleList(example);
        VehicleTaskRecovery vehicleTaskRecovery = null;
        //如果存在，update操作
        if (ArrayUtil.isNotNullAndLengthNotZero(vehicleTaskRecoveryList))
            vehicleTaskRecovery = vehicleTaskRecoveryList.get(0);
        return  vehicleTaskRecovery;
    }

    /**
     *  根据收车公司id获取收车公司信息
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    private RecoveryCompany getByRecoveryCompanyId(String recoveryCompanyId){
        RecoveryCompany recoveryCompany = null;
        //判断入参收车公司id是否存在
        Example exam = new Example(RecoveryCompany.class);
        Example.Criteria criter = exam.createCriteria();
        criter.andEqualTo("recoveryCompanyId", recoveryCompanyId);
        List<RecoveryCompany> recoveryCompanys = recoveryCompanyRepository.selectByExampleList(exam);
        if (ArrayUtil.isNotNullAndLengthNotZero(recoveryCompanys))
            recoveryCompany = recoveryCompanys.get(0);
        return recoveryCompany;
    }
    /**
     *  根据收车公司id获取收车公司信息
     * @param id 收车公司id
     * @return
     */
    private RecoveryCompany getByRecoveryCompanyById(String id){
        RecoveryCompany recoveryCompany = null;
        //判断入参收车公司id是否存在
        Example exam = new Example(RecoveryCompany.class);
        Example.Criteria criter = exam.createCriteria();
        criter.andEqualTo("id", id);
        List<RecoveryCompany> recoveryCompanys = recoveryCompanyRepository.selectByExampleList(exam);
        if (ArrayUtil.isNotNullAndLengthNotZero(recoveryCompanys))
            recoveryCompany = recoveryCompanys.get(0);
        return recoveryCompany;
    }

    /**
     * 根据任务id，授权状态获取授权信息
     *
     * @param taskId 任务id
     * @param status 状态
     * @return
     */
    private VehicleAuthorization getByUserIdAndTaskIdAndStatus(String taskId, String status){
        if(StringUtil.isNotNull(taskId) && StringUtil.isNotNull(status)) {
            Example example = new Example(VehicleAuthorization.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("taskId", taskId);
            criteria.andEqualTo("authorizationStatus", status);
            List<VehicleAuthorization> recoveryCompanieList = vehicleAuthorizationRepository.selectByExampleList(example);
            if (ArrayUtil.isNotNullAndLengthNotZero(recoveryCompanieList))
                return recoveryCompanieList.get(0);
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
            List<SystemUser> sysUserList = systemUserRepository.selectByExampleList(example);
            if (ArrayUtil.isNotNullAndLengthNotZero(sysUserList))
                return sysUserList.get(0);
        }
        return null;
    }

    /**
     * 批量导入收车公司——测试用
     *
     * @return
     */
    public ResponseEntity<RestResponse> test(){
        String userName = "taimeng";
        List<RecoveryCompanyTemp> companyList = recoveryCompanyTempRepository.selectAll();

        List<String> codes = new ArrayList();
        //取出所有收车公司注册码
        codes = recoveryCompanyRepository.selectRegisterCode();

        List<RecoveryCompany> resultList = new ArrayList();
        RecoveryCompany company;
        for(RecoveryCompanyTemp item : companyList){
            company = new RecoveryCompany();
            BeanUtils.copyProperties(item,company);
            company.setId(null);
            company.setRecoveryCompanyId(userName + "_" + item.getRecoveryCompanyId());
            buildRegisterCode(company);
            resultList.add(company);
        }
        recoveryCompanyRepository.insertListByMapper(resultList);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","推送成功"),
                HttpStatus.OK);
    }

    /**
     * 构建任务工单表数据
     * @param vo
     * @return
     */
    private VehicleTask buildVehicleTask(VehicleTaskPushVo vo) {
        // 任务发起时间
        Date startDate = getDateTime(vo.getPushDate());
        VehicleTask task = new VehicleTask();
        task.setCreateTime(startDate);
        task.setUpdateTime(startDate);
        task.setCreator(vo.getLeaseCompanyUserName());
        task.setUpdater(vo.getLeaseCompanyUserName());
        task.setName(vo.getName());
        task.setPlate(vo.getPlate());
        task.setServiceFee(vo.getServiceFee());
        task.setVehicleProvince(vo.getVehicleProvince());
        task.setVehicleCity(vo.getVehicleCity());
        task.setVehicleIdentifyNum(vo.getVehicleIdentifyNum());
        task.setEngineNo(vo.getEngineNo());
        task.setVehicleType(vo.getVehicleType());
        task.setVehicleColor(vo.getVehicleColor());
        task.setInsuranceInfo(vo.getInsuranceInformation());
        task.setViolationInfo(vo.getViolationInformation());
        task.setGpsSystemUserName(vo.getGpsSystemUserName());
        task.setGpsSystemUserPassword(vo.getGpsSystemUserPassword());
        task.setLeaseCompanyName(vo.getLeaseCompanyName());
        task.setLeaseCompanyUserName(vo.getLeaseCompanyUserName());
        task.setTaskStatus(TaskStatusEnums.NORMAL.getType());
        task.setBrand(vo.getBrand());
        task.setManufacturer(vo.getManufacturer());
        task.setGpsOnline(vo.getGpsOnline());
        task.setReceiveDate(new Date()); //服务器系统时间
        task.setTaskPdfUrl(vo.getUrl()); //催收服务任务单url
        task.setIdCard(vo.getIdCard()); //身份证号码
        task.setPhoneNum(vo.getPhoneNum()); //身主手机号码
        task.setFpProvince(vo.getFpProvince()); //所在fp省份
        task.setWorkAddress(vo.getWorkAddress()); //工作地址
        task.setHomeAddress(vo.getHomeAddress()); //家庭地址
        task.setLifeAddress(vo.getLifeAddress()); //常住地址
        //GPS有无
        if(StringUtil.isNotNull(vo.getGpsSystemUserName()) && StringUtil.isNotNull(vo.getGpsSystemUserPassword())){
            task.setHasGpsFlag("1");
        } else {
            task.setHasGpsFlag("0");
        }
        //违章信息有无
        if(StringUtil.isNotNull(vo.getViolationInformation())){
            task.setVolitionFlag("1");
        } else {
            task.setVolitionFlag("0");
        }
        //线索信息有无
        Example example2 = new Example(ClueInfo.class);
        Example.Criteria criteria = example2.createCriteria();
        criteria.andEqualTo("plate", vo.getPlate());
        criteria.orEqualTo("vehicleIdentifyNum", vo.getVehicleIdentifyNum());
        List<ClueInfo> clueInfoList = clueInforRepository.selectByExampleList(example2);
        if(clueInfoList != null && !clueInfoList.isEmpty()){
            task.setClueFlag("1");
        } else {
            task.setClueFlag("0");
        }
        // 设定价格区间
        Double serviceFee = vo.getServiceFee();
        if(new Double(0) <= serviceFee && serviceFee < new Double(10000)){
            task.setPriceRange(ServiceFeeEnum.RANGE_A.value());
        } else if(new Double(10000) <= serviceFee && serviceFee < new Double(20000)){
            task.setPriceRange(ServiceFeeEnum.RANGE_B.value());
        } else if(new Double(20000) <= serviceFee && serviceFee < new Double(30000)){
            task.setPriceRange(ServiceFeeEnum.RANGE_C.value());
        }  else if(new Double(30000) <= serviceFee && serviceFee < new Double(40000)){
            task.setPriceRange(ServiceFeeEnum.RANGE_D.value());
        }  else if(serviceFee >= new Double(40000)){
            task.setPriceRange(ServiceFeeEnum.RANGE_E.value());
        }
        return task;
    }

    /**
     * db操作，insert工单表和收车公司任务表
     * @param taskId
     * @param recoveryCompany
     * @param vo
     */
    @Transactional
    private void saveRecovery(Long taskId, RecoveryCompany recoveryCompany, VehicleTaskPushVo vo) {
        // 任务发起时间
        Date startDate = getDateTime(vo.getPushDate());

        if(recoveryCompany == null){
            //如果没有传递主系统收车公司id
            return;
        }
        //insert收车公司任务表
        VehicleTaskRecovery vehicleTaskRecovery = new VehicleTaskRecovery();
        vehicleTaskRecovery.setTaskId(taskId);
        vehicleTaskRecovery.setRecoveryCompanyId(recoveryCompany.getId());
        Calendar calendar = DateUtil.getCalendar(startDate);
        calendar.add(Calendar.DAY_OF_YEAR,14);//该任务有效期为14天
        Date failureTime = calendar.getTime();
        vehicleTaskRecovery.setStartTime(startDate);
        vehicleTaskRecovery.setFailureTime(failureTime);
        vehicleTaskRecovery.setStatus(RecoveryTaskStatusEnums.WAITRECOVERY.getType());//状态设为初始状态：正常
        vehicleTaskRecovery.setCreator(vo.getLeaseCompanyUserName());
        vehicleTaskRecovery.setUpdater(vo.getLeaseCompanyUserName());
        vehicleTaskRecovery.setCreateTime(startDate);
        vehicleTaskRecovery.setUpdateTime(startDate);
        vehicleTaskRecoveryRepository.insertOne(vehicleTaskRecovery);
    }

    /**
     * db操作，insert工单表和收车公司任务表，2018-4-1的数据临时方法
     * @param taskId
     * @param recoveryCompany
     * @param vo
     */
    @Transactional
    private void saveRecoveryBak(Long taskId, RecoveryCompany recoveryCompany, VehicleTaskPushVo vo) {
        if(recoveryCompany == null){
            //如果没有传递主系统收车公司id
            return;
        }
        // 任务发起时间
        Date startDate = getDateTime(vo.getPushDate());
        //当前时间
        Date nowDate = new Date();
        //最终保存到收车公司任务表的任务结束时间
        Date finishDate;
        //正式上线时间,2018-4-1
        Date prodDate = DateUtil.getDateTime("20180401000000");
        //原本的任务结束时间
        Calendar calendar = DateUtil.getCalendar(startDate);
        calendar.add(Calendar.DAY_OF_YEAR,14);//该任务有效期为14天
        Date originFinishDate = calendar.getTime(); //原本的任务结束时间
        //计算时间差
        Long days = DateUtil.getDay(startDate, nowDate);
        String gpsOnlineFlag = vo.getGpsOnline();
        //如果是一个月以前的单子
        if(days > 30l){
            // gps是否在线
            if(GpsOnlineEnums.ON.getType().equals(gpsOnlineFlag)){
                //如果在线
                finishDate = prodDate;
            } else {
                //如果不在线，直接放入悬赏列表，设定结束时间为发起时间之后的十四天
                finishDate = originFinishDate;
            }
        } else { //一个月以内的单子，并且有收车公司的，放入收车公司任务表，由于2018-4-1是个节点，所以原本的十四天有效期需要动态改变
            if(GpsOnlineEnums.ON.getType().equals(gpsOnlineFlag)){
                //永不失效时间,2099-12-31
                Date neverDate = DateUtil.getDateTime("20991231235959");
                finishDate = neverDate;
            } else {
                if(originFinishDate.before(prodDate)){
                    // 如果发起日期加上14天在2018-4-1之前，设定任务结束日期为2018-4-1
                    finishDate = prodDate;
                } else {
                    //如果发起日期加上14天在2018-4-1之后，设定任务结束日期为原本的任务结束时间，即开始日期加14天
                    finishDate = originFinishDate;
                }
            }
        }
        //insert收车公司任务表
        VehicleTaskRecovery vehicleTaskRecovery = new VehicleTaskRecovery();
        vehicleTaskRecovery.setTaskId(taskId);
        vehicleTaskRecovery.setRecoveryCompanyId(recoveryCompany.getId());
        vehicleTaskRecovery.setStartTime(startDate);
        vehicleTaskRecovery.setFailureTime(finishDate);
        vehicleTaskRecovery.setStatus(RecoveryTaskStatusEnums.WAITRECOVERY.getType());//状态设为初始状态：正常
        // 分配状态-----0：未分配
        vehicleTaskRecovery.setAssignmentFlag("0");
        vehicleTaskRecovery.setCreator(vo.getLeaseCompanyUserName());
        vehicleTaskRecovery.setUpdater(vo.getLeaseCompanyUserName());
        vehicleTaskRecovery.setCreateTime(startDate);
        vehicleTaskRecovery.setUpdateTime(startDate);
        vehicleTaskRecoveryRepository.insertOne(vehicleTaskRecovery);
    }

    /**
     * db操作,插入收车公司表
     * @param vo
     */
    @Transactional
    private RecoveryCompany saveRecoveryCompany(RecoveryCompanyPushVo vo) {
        //在收车公司表中新增一条数据
        RecoveryCompany recoveryCompany = new RecoveryCompany();
        BeanUtils.copyProperties(vo,recoveryCompany);
        recoveryCompany.setId(null);
        buildRegisterCode(recoveryCompany);
        return recoveryCompanyRepository.insertOne(recoveryCompany);
    }

    /**
     * 验证token正确性
     * @param token
     * @return
     */
    private boolean verifyToken(String token) {
        String [] values = token.split(":");
        if(values.length != 2){
            return false;
        }
        String userName = values[0];
        //比对redis中的token和入参的token
        String redisToken = (String)redisRepository.get(RedisKeys.lease_recovery + userName);
        if(redisToken == null || !redisToken.equals(token)){
            return false;
        }
        //比对成功则清空此次token值
        redisRepository.delete(RedisKeys.lease_recovery + userName);
        return true;
    }

    /**
     * 推送收车公司时check必要参数是否为空
     * @param vo
     * @param param
     */
    private void checkAddCompanyParam(RecoveryCompanyPushVo vo, StringBuffer param) {
        if(StringUtil.isNull(vo.getRecoveryFullName())){
            param.append("收车公司全称、");
        }
        if(StringUtil.isNull(vo.getContactName())){
            param.append("收车公司联系人姓名、");
        }
        if(StringUtil.isNull(vo.getContactPhone())){
            param.append("收车公司联系人号码、");
        }
        if(StringUtil.isNull(vo.getContactEmail())){
            param.append("收车公司联系人邮箱、");
        }
        if(!param.toString().isEmpty()){
            param.replace(param.length()-1,param.length(),"");
        }
    }

    /**
     * 推送工单时check必要参数是否为空
     * @param vo
     * @param param
     */
    private void checkPushTaskParam(VehicleTaskPushVo vo, StringBuffer param) {
        if(StringUtil.isNull(vo.getName())){
            param.append("车主姓名");
        }
        if(StringUtil.isNull(vo.getPlate())){
            param.append("车牌号、");
        }
        if(StringUtil.isNull(vo.getVehicleIdentifyNum())){
            param.append("车架号、");
        }
        if(StringUtil.isNull(vo.getServiceFee())){
            param.append("服务费、");
        }
        if(StringUtil.isNull(vo.getGpsOnline())){
            param.append("gps是否在线、");
        }
        if(StringUtil.isNull(vo.getManufacturer())){
            param.append("制造商、");
        }
        if(StringUtil.isNull(vo.getBrand())){
            param.append("品牌、");
        }
        if(StringUtil.isNull(vo.getVehicleType())){
            param.append("车型、");
        }
        if(!param.toString().isEmpty()){
            param.replace(param.length()-1,param.length(),"");
        }
    }

    /**
     * 推送收车app信息时check必要参数是否为空
     * @param vo
     * @param param
     */
    private void checkAppInfoParam(AppInfoPushVo vo, StringBuffer param) {
        if(StringUtil.isNull(vo.getAndroidUrl())){
            param.append("app安卓下载链接");
        }
        if(StringUtil.isNull(vo.getIosUrl())){
            param.append("app苹果下载链接、");
        }
        if(!param.toString().isEmpty()){
            param.replace(param.length()-1,param.length(),"");
        }
    }

    /**
     * 生成收车公司注册码
     * @param newRecoveryCompany
     */
    private void buildRegisterCode(RecoveryCompany newRecoveryCompany) {
        String managerRegisterCode = null; //初始化内勤注册码
        String salesmanRegisterCode = null; //初始化业务员注册码
        String bossRegisterCode = null; //初始化老板注册码
        List<String> codes = new ArrayList();
        //取出所有收车公司注册码
        codes = recoveryCompanyRepository.selectRegisterCode();
        //循环给主管和业务员注册码赋值
        do
        {
            String code = RandomUtil.getRegisterCode(); //生成随机数
            if(!codes.contains(code)){
                if(managerRegisterCode == null){
                    managerRegisterCode = code;
                    codes.add(code);
                } else if(salesmanRegisterCode == null){
                    salesmanRegisterCode = code;
                    codes.add(code);
                } else if(bossRegisterCode == null){
                    bossRegisterCode = code;
                    codes.add(code);
                }
            }
        }
        while (managerRegisterCode == null || salesmanRegisterCode == null || bossRegisterCode == null);// 直到赋值成功，结束循环

        // 主管注册码
        newRecoveryCompany.setManagerRegisterCode(managerRegisterCode);
        // 业务员注册码
        newRecoveryCompany.setSalesmanRegisterCode(salesmanRegisterCode);
        // 老板注册码
        newRecoveryCompany.setBossRegisterCode(bossRegisterCode);
    }

    /**
     * @Title:
     * @Description:  根据用户名获取用户
     * @param username
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/12 12:37:31
     */
    private SystemUser findSysUserByUserId(String username){
        if(StringUtil.isNotNull(username)) {
            Example example = new Example(SystemUser.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId", username);
            return systemUserRepository.selectOneByExample(example);
        }
        return null;
    }

    /**
     * 保存收车公司委托公司关系表
     * @param recoveryCompanyId
     * @param userName
     */
    @Transactional
    private void saveRecoveryLeaseRelation(String recoveryCompanyId, String userName) {
        //获得当前推送的委托公司id
        Example example = new Example(SystemUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userName);
        String leaseId = systemUserRepository.selectOneByExample(example).getLeaseId();
        //查询收车公司委托公司关系表中是否已经有对应关系
        Example example1 = new Example(RecoveryLeaseRelation.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("recoveryCompanyId", recoveryCompanyId);
        criteria1.andEqualTo("leaseId", leaseId);
        RecoveryLeaseRelation recoveryLeaseRelation  = recoveryLeaseRelationRepository.selectOneByExample(example1);
        // 无对应关系，则插入收车公司委托公司关系表
        if(recoveryLeaseRelation == null){
            recoveryLeaseRelation = new RecoveryLeaseRelation();
            recoveryLeaseRelation.setRecoveryCompanyId(recoveryCompanyId);
            recoveryLeaseRelation.setLeaseId(leaseId);
            recoveryLeaseRelationRepository.insertOne(recoveryLeaseRelation);
        }
    }

    /**
     * 收车任务取消时推送消息给小组成员和内勤人员以及获取授权的人，并插入消息中心表,推送结果消息推送状态表
     *
     * @param groupId  分组id
     * @param plate 车牌号
     * @param userId 用户id
     * @param authorizationUserId 已授权用户id
     */
    @Transactional
    private void createRecoveryCancelPush(String groupId, String plate, String userId,String authorizationUserId,String recoveryCompanyId) {
        // 发送短信用
        List<String> userIdList = new ArrayList<>();
        // 推送内容
        String content = MessageTempletConstants.templet_108.replace(MessageTempletConstants.templet_plate,plate);
        Date nowDate = new Date();
        // 获取内勤人员信息
        GroupUserClientVo groupUserClientVo = getUserClientByRecoveryCompanyId(recoveryCompanyId);
        if(groupUserClientVo != null){
            userIdList.add(groupUserClientVo.getUserId());
            // 登录消息中心表数据----内勤人员
            MessageCenter messageCenter = insertMessageCenter(content,groupUserClientVo.getUserId(),nowDate,userId, MessageTriggerEnum.TASK_108.getCode());
            messageCenterRepository.insertOne(messageCenter);
            // 推送消息 --- 内勤人员
            List<GroupUserClientVo>  groupUserClientVoList = new ArrayList<>();
            groupUserClientVoList.add(groupUserClientVo);
            push(userId, groupUserClientVoList, content, groupUserClientVo.getClient(), PushCastEnum.UNICAST.getCode(),MessageTriggerEnum.TASK_108.getNane(), groupUserClientVo.getDeviceToken());
        }
        if(StringUtil.isNotNull(authorizationUserId)){
              // 获取已授权人员信息
              GroupUserClientVo userClientVo = getUserClientByUserId(authorizationUserId);
              // 登录消息中心表数据----已授权人员
              MessageCenter messageCenter = insertMessageCenter(content,authorizationUserId,nowDate,userId, MessageTriggerEnum.TASK_108.getCode());
              messageCenterRepository.insertOne(messageCenter);
              if(userClientVo != null){
                  userIdList.add(userClientVo.getUserId());
                  // 推送消息 --- 已授权人员
                  List<GroupUserClientVo>  groupUserClientVoList = new ArrayList<>();
                  groupUserClientVoList.add(userClientVo);
                  push(userId, groupUserClientVoList, content, userClientVo.getClient(), PushCastEnum.UNICAST.getCode(),MessageTriggerEnum.TASK_108.getNane(),userClientVo.getDeviceToken());
              }
          }
        // 以下是小组成员信息推送
        // 获取小组成员信息
        if(StringUtil.isNotNull(groupId)){
            GroupUserListVo vo = getGroupUserClientByGroupId(groupId);
            if(vo != null){
                List<MessageCenter> messageCenters = new ArrayList();
                //小组用户集合不为空
                if(ArrayUtil.isNotNullAndLengthNotZero(vo.getGroupUsers())){
//                userIdList.addAll(vo.getGroupUsers());
                    //遍历小组用户集合
                    for(String groupUser : vo.getGroupUsers()){
                        // 登录消息中心表数据 --- 小组成员
                        messageCenters.add(insertMessageCenter(content,groupUser,nowDate,userId, MessageTriggerEnum.TASK_108.getCode())) ;
                    }
                    //批量插入消息中心表
                    messageCenterRepository.insertMore(messageCenters);
                }
                //ios设备集合不为空，调用推送共同推送消息
                if(ArrayUtil.isNotNullAndLengthNotZero(vo.getIosUsers())){
                    push(userId, vo.getIosUsers(), content, ClientTypeEnums.IOS.getCode(),PushCastEnum.LISTCAST.getCode(),MessageTriggerEnum.TASK_108.getNane(),null);
                }
                //android设备集合不为空，调用推送共同推送消息
                if(ArrayUtil.isNotNullAndLengthNotZero(vo.getAndroidUsers())){
                    push(userId, vo.getAndroidUsers(), content, ClientTypeEnums.ANDROID.getCode(),PushCastEnum.LISTCAST.getCode(),MessageTriggerEnum.TASK_108.getNane(),null);
                }
            }
        }

        // 短信内容
        String messageContent = "【赏金寻车】车牌号为【" + plate + "】的任务现已取消委托，请停止相关动作！";
        // 任务取消，给小组成员+ 内勤人员+ 已授权人员 发送短信
        if (ArrayUtil.isNotNullAndLengthNotZero(userIdList)) {
            // 内勤人员+ 已授权人员 发送短信
            try{
                for(String userPhone : userIdList){
                    messageService.sendMessage(userPhone,messageContent,"赏金寻车","收车取消给内勤人员及已授权人员发送短信", Constants.SENDMESSAGE_TASK_CANSEL_SCAN);
                }
            }catch (Exception e){
                e.printStackTrace();
                logger.error("发送短信error",e);
            }
        }

    }
    /**
     * 推送消息，登录消息推送日志表
     *
     * @param userId 用户id
     * @param voList 安卓或iOS用户信息
     * @param content 消息内容
     * @param clientType "1":ios,"0":安卓
     */
    @Transactional
    private void push(String userId, List<GroupUserClientVo> voList, String content, String clientType, String pushCast,String messageTrigger, String deviceToken) {
        List<String> deviceTokens = new ArrayList(); //设备集合
        StringBuilder deviceTokenStr = new StringBuilder(); //消息推送日志表
        for(GroupUserClientVo item : voList){
            if(deviceTokenStr.length() > 0){
                deviceTokenStr = deviceTokenStr.append(Constants.COMMA);
            }
            if(StringUtil.isNotNull(item.getDeviceToken())){
                deviceTokenStr = deviceTokenStr.append(item.getDeviceToken());
                deviceTokens.add(item.getDeviceToken());
            }
        }
        if(ArrayUtil.isNotNullAndLengthNotZero(deviceTokens) || StringUtil.isNotNull(deviceToken)){
            MessagePushHistory messagePushHistory = new MessagePushHistory();
            messagePushHistory.setClient(clientType);
            messagePushHistory.setContent(content);
            if(PushCastEnum.LISTCAST.getCode().equals(pushCast)){
                messagePushHistory.setDeviceTokens(deviceTokenStr.toString());
            }else{
                messagePushHistory.setDeviceTokens(deviceToken);
            }
            String title  = messageTrigger;//消息触发动作:新增催收记录
            try {
                int result;
                if(PushCastEnum.LISTCAST.getCode().equals(pushCast)){
                    result = UmengPushUtils.push(pushCast,clientType,null,
                            deviceTokens,title,content);
                }else{
                    result = UmengPushUtils.push(pushCast,clientType,deviceToken,
                            null,title,content);
                }
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
                Date now = new Date();
                messagePushHistory.setCreateTime(now);
                messagePushHistory.setUpdateTime(now);
                messagePushHistory.setCreator(userId);
                messagePushHistory.setUpdater(userId);
                messagePushHistoryRepository.insertOne(messagePushHistory);
            }
        }

    }

    /**
     * 登录消息中心表数据
     *
     * @param content 消息内容
     * @param groupUser 接收用户
     * @param nowDate 当前时间
     * @param userId 用户id
     */
    private MessageCenter insertMessageCenter(String content, String groupUser, Date nowDate,String userId,String messageTrigger){
        MessageCenter messageCenter = new MessageCenter();
        messageCenter.setType(MessageTypeEnum.TASK.getCode()); //消息类型:任务小消息
        messageCenter.setTag(MessageTagEnum.RECORD.getCode()); //消息标签：记录
        //消息内容，把模板消息的参数替换掉
        messageCenter.setContent(content);
        //判断消息动作类型
        messageCenter.setActivity(messageTrigger);//消息触发动作:新增催收记录
        messageCenter.setReceiver(groupUser); //接收者
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
     * 通过组id获取小组成员并且按照客户端类型分类返回
     *
     * @param groupId 分组id
     * @return
     */
    public GroupUserListVo getGroupUserClientByGroupId(String groupId){
        GroupUserListVo result = new GroupUserListVo();
        List<GroupUserClientVo> voList = recoveryGroupUserRepository.selectGroupUserClientByGroupId(groupId);
        if(ArrayUtil.isNullOrLengthZero(voList)){
            return null;
        }
        // 消息中心需要插入的数据对应的用户id集合
        List<String> groupUsers = new ArrayList();
        //该小组中需要推送的安卓用户设备集合
        List<GroupUserClientVo> androidUsers = new ArrayList();
        //该小组中需要推送的ios用户设备集合
        List<GroupUserClientVo> iosUsers = new ArrayList();
        String userId;
        for(GroupUserClientVo item : voList){
            userId = item.getUserId();
            groupUsers.add(userId);
            //如果没有Devicetoken，不需要推送
            if(StringUtil.isNull(item.getDeviceToken())){
                continue;
            }
            //安卓设备，放入安卓用户集合
            if(ClientTypeEnums.ANDROID.getCode().equals(item.getClient())){
                androidUsers.add(item);
            } else if(ClientTypeEnums.IOS.getCode().equals(item.getClient())){
                //ios设备，放入ios用户集合
                iosUsers.add(item);
            }
        }
        result.setAndroidUsers(androidUsers);
        result.setIosUsers(iosUsers);
        result.setGroupUsers(groupUsers);
        return result;
    }

    /**
     * 通过收车公司id与角色获取该收车公司内勤人员客户端类型
     *
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    public GroupUserClientVo getUserClientByRecoveryCompanyId(String recoveryCompanyId){
        GroupUserClientVo vo = recoveryGroupUserRepository.selectUserClientByRecoveryCompanyId(recoveryCompanyId);
        if(vo == null){
            return null;
        }
        return vo;
    }
    /**
     * 通过用户id获取用户客户端类型
     *
     * @param userId 用户id
     * @return
     */
    public GroupUserClientVo getUserClientByUserId(String userId){
        GroupUserClientVo groupUserClientVo = new GroupUserClientVo();
        Example example = new Example(SystemUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        UserDeviceInfo vo = userDeviceInfoRepository.selectOneByExample(example);
        if(vo == null){
            return null;
        }
        groupUserClientVo.setClient(String.valueOf(vo.getClient()));
        groupUserClientVo.setDeviceToken(vo.getDeviceToken());
        groupUserClientVo.setUserId(userId);
        return groupUserClientVo;
    }
    /**
     * 根据收车公司任务表id获取任务分组情况
     *
     * @param vehicleTaskRecoveryId 收车公司任务表主键
     * @return
     */
    private List<VehicleTaskGroup> getByVehicleTaskRecoveryId(String vehicleTaskRecoveryId){
        if(StringUtil.isNotNull(vehicleTaskRecoveryId)) {
            Example example = new Example(VehicleTaskGroup.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("vehicleTaskRecoveryId", vehicleTaskRecoveryId);
            return vehicleTaskGroupRepository.selectByExampleList(example);
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

    /**
     * 任务取消、新增任务时。给收车公司发送邮件
     *
     * @param task  任务详情
     * @param recoveryCompanyId 收车公司id
     * @param userName
     * @param sendFlag 发送类型  1：取消工单、2：推送工单
     */
    private void sendMailToRecoveryCompany(VehicleTask task, String recoveryCompanyId, String userName, String sendFlag) {
        //收车公司id不为空，给对应的收车公司发邮件
        if (StringUtil.isNotNull(recoveryCompanyId)) {
            RecoveryCompany recoveryCompany = recoveryCompanyRepository.selectByPrimaryKey(recoveryCompanyId);
            //如果存在该收车公司，给其发邮件
            if (recoveryCompany != null && StringUtil.isNotNull(recoveryCompany.getContactEmail())) {
                //构建邮件发送记录表的数据
                AuthMailRecord authMailRecord = new AuthMailRecord(task);
                authMailRecord.setEmail(recoveryCompany.getContactEmail());
                authMailRecord.setSendStatus(MailSendStatusEnums.NOT_SEND.getType()); //设置初始状态为未发送
                authMailRecord.setRetryTimes(0); //初始重试次数为0
                authMailRecord.setUrl(task.getTaskPdfUrl()); //催收服务任务单url
                authMailRecord.setMailType(sendFlag);
                authMailRecord.setCreator(userName);
                Date nowDate = new Date();
                authMailRecord.setCreateTime(nowDate);
                authMailRecord.setUpdateTime(nowDate);
                authMailRecord.setUpdater(userName);
                authMailRecord = authMailRecordRepository.insertOne(authMailRecord);
                try {
                    logger.info(MailTypeEnum.getEnum(sendFlag).name() + "成功，发送邮件给收车公司开始", authMailRecord.toString());
                    //新建一个线程，异步处理发邮箱事件，减少前台授权操作等待时间
                    try {
                        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
                        AuthMailRecord finalAuthMailRecord = authMailRecord;
                        cachedThreadPool.execute(new Runnable() {
                            public void run() {
                                // do task
                                try {
                                    //发邮件操作
                                    mailUtils.sendAuthMail(finalAuthMailRecord,sendFlag);
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
                }
                logger.info(MailTypeEnum.getEnum(sendFlag).name() + "成功，发送邮件给收车公司结束", authMailRecord.toString());
            }
        }
    }

    private String getPriceRange(Double serviceFee){
        if(new Double(0) <= serviceFee && serviceFee < new Double(10000)){
            return ServiceFeeEnum.RANGE_A.value();
        } else if(new Double(10000) <= serviceFee && serviceFee < new Double(20000)){
            return ServiceFeeEnum.RANGE_B.value();
        } else if(new Double(20000) <= serviceFee && serviceFee < new Double(30000)){
            return ServiceFeeEnum.RANGE_C.value();
        }  else if(new Double(30000) <= serviceFee && serviceFee < new Double(40000)){
            return ServiceFeeEnum.RANGE_D.value();
        }  else if(serviceFee >= new Double(40000)){
            return ServiceFeeEnum.RANGE_E.value();
        }
        return null;
    }

    /**
     * 根据工单id获取收车公司任务表对应数据
     *
     * @return
     */
    private VehicleTaskRecovery getRecoveryCompanyTask(Long taskId){
        if(StringUtil.isNotNull(taskId)) {
            Example example = new Example(VehicleTaskRecovery.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("taskId", taskId);
            VehicleTaskRecovery vehicleTaskRecovery = vehicleTaskRecoveryRepository.selectOneByExample(example);
            return vehicleTaskRecovery;
        }
        return null;
    }
}
