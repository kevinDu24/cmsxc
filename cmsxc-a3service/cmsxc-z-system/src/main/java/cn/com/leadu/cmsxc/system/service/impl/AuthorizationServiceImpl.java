package cn.com.leadu.cmsxc.system.service.impl;

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
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.data.system.repository.SystemUserRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryCompany;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryGroupUser;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTask;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskRecovery;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.AuthorizationListVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.AuthorizationVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.GroupUserClientVo;
import cn.com.leadu.cmsxc.pojo.appuser.entity.*;
import cn.com.leadu.cmsxc.pojo.appuser.vo.ResultVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import cn.com.leadu.cmsxc.pojo.system.vo.information.AuthorizationPathVo;
import cn.com.leadu.cmsxc.system.service.AuthorizationService;
import cn.com.leadu.cmsxc.system.service.CoreSystemInterface;
import cn.com.leadu.cmsxc.system.service.MessageService;
import cn.com.leadu.cmsxc.system.util.constant.MailUtils;
import com.google.gson.Gson;
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
 * Created by yuanzhenxia on 2018/2/5.
 *
 * 授权操作实现类
 */
@Service
public class AuthorizationServiceImpl implements AuthorizationService{

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationServiceImpl.class);

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
    private SystemUserRepository systemUserRepository;
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
    private Gson gson;
    @Autowired
    private MailUtils mailUtils;

    private static int THREEDAYS = 3;

    private static String URL= "customermsg";
    /**
     * 根据授权状态，分页获取授权信息
     *
     * @param authorizationVo 画面参数
     * @return
     */
    public ResponseEntity<RestResponse> findAuthorizationListByPage(AuthorizationVo authorizationVo, SystemUser sysUser){
        // 设置查询条件，申请人姓名
        if (StringUtil.isNotNull(authorizationVo.getApplicantName())) {
            authorizationVo.setApplicantName(CommonUtil.likePartten(authorizationVo.getApplicantName()));
        }
        // 车牌号
        if (StringUtil.isNotNull(authorizationVo.getVehiclePlate())) {
            authorizationVo.setVehiclePlate(CommonUtil.likePartten(authorizationVo.getVehiclePlate()));
        }
        // 车架号
        if (StringUtil.isNotNull(authorizationVo.getVehicleIdentifyNum())) {
            authorizationVo.setVehicleIdentifyNum(CommonUtil.likePartten(authorizationVo.getVehicleIdentifyNum()));
        }
        // 用户电话
        if (StringUtil.isNotNull(authorizationVo.getUserId())) {
            authorizationVo.setUserId(CommonUtil.likePartten(authorizationVo.getUserId()));
        }
        // 申请人电话
        if (StringUtil.isNotNull(authorizationVo.getApplicantPhone())) {
            authorizationVo.setApplicantPhone(CommonUtil.likePartten(authorizationVo.getApplicantPhone()));
        }
          // 分页取数据,授权表与任务工单表依据工单id相等关联，获取所有数据
        List<AuthorizationListVo> authorizationListVoList = vehicleAuthorizationRepository.selectByAuthorizationstatus(authorizationVo, 0, authorizationVo.getCurrenPage(), authorizationVo.getPageSize(), sysUser.getLeaseId());
        for(AuthorizationListVo authorizationListVo : authorizationListVoList){
            try{
                // 格式化审批时间 yyyy-MM-dd HH:mm:ss
                if(authorizationListVo.getApplyEndDate() != null){
                    authorizationListVo.setApplyEndString(DateUtil.dateToStr(authorizationListVo.getApplyEndDate(),DateUtil.formatStr_yyyyMMddHHmmss));
                }
                // 格式化申请开始时间 yyyy-MM-dd HH:mm:ss
                if(authorizationListVo.getApplyStartDate()!= null){
                    authorizationListVo.setApplyStartString(DateUtil.dateToStr(authorizationListVo.getApplyStartDate(),DateUtil.formatStr_yyyyMMddHHmmss));
                }
                // 格式化操作时间 yyyy-MM-dd HH:mm:ss
                if(authorizationListVo.getOperateDate() != null){
                    authorizationListVo.setOperateString(DateUtil.dateToStr(authorizationListVo.getOperateDate(),DateUtil.formatStr_yyyyMMddHHmmss));
                }
                // 格式化授权失效时间 yyyy-MM-dd HH:mm:ss
                if(authorizationListVo.getAuthorizationOutTimeDate() != null){
                    authorizationListVo.setOutTimeDateString(DateUtil.dateToStr(authorizationListVo.getAuthorizationOutTimeDate(),DateUtil.formatStr_yyyyMMddHHmmss));
                }
                // 服务费
                if (authorizationListVo.getServiceFee() != null) {
                    authorizationListVo.setServiceFeeValue(CommonUtil.numberToformat(authorizationListVo.getServiceFee()) + "元");
                }
                // 工单号
                authorizationListVo.setTaskNum(CommonUtil.getNumberStrLeftPatch(authorizationListVo.getTaskId(), 8));
                // 授权状态
                authorizationListVo.setAuthorizationStatus(AuthorizationStatusEnums.getEnum(authorizationListVo.getAuthorizationStatus()).getValue());
            }catch(Exception e){
                e.printStackTrace();
                logger.error("分页获取授权信息error",e);
                throw new CmsServiceException("分页获取授权信息失败");
            }
        }
        // 取总条数
        List<AuthorizationListVo> authorizationVoAllList = vehicleAuthorizationRepository.selectByAuthorizationstatus(authorizationVo, 1, authorizationVo.getCurrenPage(), authorizationVo.getPageSize(), sysUser.getLeaseId());
        // 分页处理
        PageInfoExtend<AuthorizationListVo> pageInfo = new PageInfoExtend();
        if(authorizationListVoList != null && !authorizationListVoList.isEmpty()){
            pageInfo.setData(authorizationListVoList);
            pageInfo.setRecordsTotal(Long.valueOf(authorizationVoAllList.size()));
            pageInfo.setDraw(authorizationVo.getDraw());
            pageInfo.setRecordsFiltered(Long.valueOf(authorizationVoAllList.size()));
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,pageInfo,""),
                    HttpStatus.OK);
        } else {
            pageInfo.setData(new ArrayList<>());
            pageInfo.setRecordsTotal(0L);
            pageInfo.setDraw(authorizationVo.getDraw());
            pageInfo.setRecordsFiltered(0L);
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,pageInfo,"未查询到信息"),
                    HttpStatus.OK);
        }
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
            // 获取申请人用户信息
            SystemUser sysUser = selectSystemUserByUserId(userId);
            if(sysUser == null){
                return new ResponseEntity<RestResponse>(
                        RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","申请用户信息不存在"),
                        HttpStatus.OK);
            }
            // 根据任务id获取收车公司任务信息
            VehicleTaskRecovery  vehicleTaskRecovery = selectByTaskId(vehicleAuthorization.getTaskId());
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
            vehicleAuthorization.setAuthorizationOutTimeDate(DateUtil.getDateTime(DateUtil.getStringDate(DateUtils.addDays(nowDate,THREEDAYS)).concat("000000")));
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
            String content2 = MessageTempletConstants.templet_201_1.replace(MessageTempletConstants.templet_user_id,userId).replace(MessageTempletConstants.templet_plate,plate)
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
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","获取授权信息失败"),
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
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","获取授权信息失败"),
                HttpStatus.OK);
    }

    /**
     * 授权延期操作
     *
     * @param  delayDate 延期日期
     * @param  authorizationId 授权id
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> delay(String authorizationId, Date delayDate, String remark, SystemUser systemUser){
        // 延期后日期为画面选定的日期加一天
        Calendar calendar = DateUtil.getCalendar(delayDate);
        calendar.add(Calendar.DAY_OF_YEAR,1);
        delayDate = calendar.getTime();
        Date nowDate = new Date();
        // 根据授权id获取授权信息
        VehicleAuthorization vehicleAuthorization = selectByAuthorizationId(authorizationId);
        if(vehicleAuthorization == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","获取授权信息失败"),
                    HttpStatus.OK);
        }
        if(!AuthorizationStatusEnums.AUTHORIZETED.getType().equals(vehicleAuthorization.getAuthorizationStatus())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","延期失败，只有已授权的申请才可延期"),
                    HttpStatus.OK);
        }
        if(delayDate.compareTo(vehicleAuthorization.getAuthorizationOutTimeDate()) <= 0){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","延期失败，延期日期必须大于授权失效日期"),
                    HttpStatus.OK);
        }
        // 获取申请者信息
        SystemUser sysUser = selectSystemUserByUserId(vehicleAuthorization.getUserId());
        if(sysUser == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","申请用户信息不存在"),
                    HttpStatus.OK);
        }
        // 更新授权延期状态为1：已延期
        vehicleAuthorization.setIsDelay(AuthDelayEnum.DELAY.getCode());

        vehicleAuthorization.setAuthorizationOutTimeDate(delayDate);
        vehicleAuthorization.setDelayRemark(remark); //延期备注
        vehicleAuthorization.setDelayDate(nowDate);//延期时间
        vehicleAuthorization.setUpdateTime(nowDate);//延期时间
        vehicleAuthorizationRepository.updateByPrimaryKeySelective(vehicleAuthorization);
        // 登录授权日志表
        AuthorizationHistory history = new AuthorizationHistory();
        // 授权id
        history.setAuthorizationId(vehicleAuthorization.getId());
        // 用户id
//        history.setUserId(userId);
        // 操作时间
        history.setOperateTime(nowDate);
        // 操作内容
        history.setOperateContent(OperateContentEnums.DELAY.getType());
        //延期备注
        history.setRemark(remark);
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
        GroupUserClientVo vo = recoveryGroupUserRepository.selectUserClientByRecoveryCompanyId(sysUser.getRecoveryCompanyId()); //获取内勤人员信息
        if(vo == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","授权延期成功"),
                    HttpStatus.OK);
        }
        String content2 = MessageTempletConstants.templet_201_1.replace(MessageTempletConstants.templet_user_id,vehicleAuthorization.getUserId()).replace(MessageTempletConstants.templet_plate,
                vehicleTaskRepository.selectByPrimaryKey(vehicleAuthorization.getTaskId()).getPlate())
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
     * 根据photoUuid取得全部的图片
     *
     * @param photoUuid 画面参数
     * @return
     */
    @Override
    public ResponseEntity<RestResponse> getPhotoList(String photoUuid) {
        AuthorizationPathVo vo = new AuthorizationPathVo();
        List<String> photoPathList = new ArrayList<>();
        String vedioPath = "";
        List<VehiclePhotoPath> vehiclePhotoPathList = vehiclePhotoPathRepository.selectByVehiclePhotoUUID(photoUuid);
        if (vehiclePhotoPathList != null && vehiclePhotoPathList.size() > 0) {
            for (VehiclePhotoPath data : vehiclePhotoPathList) {
                if(ApplyAuthFileTypeEnum.VEDIO.getCode().equals(data.getType())){
                    vedioPath = data.getPhotoUrl();
                } else {
                    photoPathList.add(data.getPhotoUrl());
                }
            }
        }
        vo.setVedioPath(vedioPath);
        vo.setPhotoPathList(photoPathList);
        return new ResponseEntity<RestResponse>(RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,vo,"取得附件信息成功"), HttpStatus.OK);
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
     * 根据收车公司id获取收车公司信息
     * @param id 收车公司id
     * @return
     */
    public RecoveryCompany selectRecoveryCompanyById(String id){
        if(StringUtil.isNotNull(id)) {
            Example example = new Example(RecoveryCompany.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id", id);
            return recoveryCompanyRepository.selectOneByExample(example);
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
}
