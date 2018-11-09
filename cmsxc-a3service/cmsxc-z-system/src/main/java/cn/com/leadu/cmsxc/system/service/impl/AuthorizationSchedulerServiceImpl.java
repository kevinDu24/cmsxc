package cn.com.leadu.cmsxc.system.service.impl;

import cn.com.leadu.cmsxc.common.constant.Constants;
import cn.com.leadu.cmsxc.common.constant.enums.*;
import cn.com.leadu.cmsxc.common.util.ArrayUtil;
import cn.com.leadu.cmsxc.common.util.CommonUtil;
import cn.com.leadu.cmsxc.common.util.MessageTempletConstants;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.common.util.push.UmengPushUtils;
import cn.com.leadu.cmsxc.data.appbusiness.repository.RecoveryGroupUserRepository;
import cn.com.leadu.cmsxc.data.appbusiness.repository.RecoveryTaskRepository;
import cn.com.leadu.cmsxc.data.appbusiness.repository.VehicleTaskOperateHistoryRepository;
import cn.com.leadu.cmsxc.data.appbusiness.repository.VehicleTaskRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.*;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTask;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskOperateHistory;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskRecovery;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.GroupUserClientVo;
import cn.com.leadu.cmsxc.pojo.appuser.entity.*;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import cn.com.leadu.cmsxc.system.service.AuthorizationSchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangxue on 2018/2/7.
 * 定时分析申请任务是否失效处理的service的实现类
 */
@Service
public class AuthorizationSchedulerServiceImpl implements AuthorizationSchedulerService {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationSchedulerServiceImpl.class);

    @Autowired
    private VehicleAuthorizationRepository vehicleAuthorizationRepository;
    @Autowired
    private VehicleTaskOperateHistoryRepository vehicleTaskOperateHistoryRepository;
    @Autowired
    private RecoveryTaskRepository recoveryTaskRepository;
    @Autowired
    private AuthorizationHistoryRepository authorizationHistoryRepository;
    @Autowired
    private MessageCenterRepository messageCenterRepository;
    @Autowired
    private MessagePushHistoryRepository messagePushHistoryRepository;
    @Autowired
    private RecoveryGroupUserRepository recoveryGroupUserRepository;
    @Autowired
    private UserDeviceInfoRepository userDeviceInfoRepository;
    @Autowired
    private VehicleTaskRepository vehicleTaskRepository;

    private static final long DAY_MILLISECOND = 24*60*60*1000L;
    private static final long TWO_DAY_MILLISECOND = 2*24*60*60*1000L;

    /**
     * 分析申请任务是否失效处理
     * @return
     * */
    public String applyOutTimeHandler() {
        logger.info("分析申请任务是否失效处理开始");
        Date applyStartDate = new Date(new Date().getTime() - DAY_MILLISECOND);
        // 取得申请时间超过24小时的申请中的任务
        Example example = new Example(VehicleAuthorization.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLessThanOrEqualTo("applyStartDate", applyStartDate);
        criteria.andEqualTo("authorizationStatus", AuthorizationStatusEnums.APPLYING.getType());
        example.setOrderByClause(" create_time desc ");
        List<VehicleAuthorization> vehicleAuthorizationList = vehicleAuthorizationRepository.selectByExampleList(example);
        logger.info("分析处理的失效的申请任务的件数：" + vehicleAuthorizationList.size());
        if (vehicleAuthorizationList != null && vehicleAuthorizationList.size() > 0) {
            for (VehicleAuthorization data : vehicleAuthorizationList) {
                // 授权状态
                data.setAuthorizationStatus(AuthorizationStatusEnums.OUTTIME.getType());
                // 授权时间
                data.setOperateDate(new Date());
                // 申请结束时间
                data.setApplyEndDate(new Date());
                // 更新时间
                data.setUpdateTime(new Date());
                data.setUpdater(CommonUtil.SysUpdateUser);
                // 更新授权表
                vehicleAuthorizationRepository.updateByPrimaryKeySelective(data);
                // 登录授权日志表
                saveOperateHistory(data, OperateContentEnums.OUTTIME.getType());
            }
        }
        logger.info("分析申请任务是否失效处理结束");
        return ResponseEnum.SUCCESS.getMark();
    }

    /**
     * 授权2天后，没有完成收车的，授权失效
     *
     * @return
     * */
    @Transactional
    public String authorizationOutTimeHandler(){
        logger.info("分析授权任务是否失效处理开始");
        Date nowDate = new Date();
        // 取得授权超过2天的已授权的任务
        Example example = new Example(VehicleAuthorization.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLessThanOrEqualTo("authorizationOutTimeDate", nowDate);
        criteria.andEqualTo("authorizationStatus", AuthorizationStatusEnums.AUTHORIZETED.getType());
        example.setOrderByClause(" create_time desc ");
        List<VehicleAuthorization> vehicleAuthorizationList = vehicleAuthorizationRepository.selectByExampleList(example);
        logger.info("分析授权失效任务的件数：" + vehicleAuthorizationList.size());
        if (vehicleAuthorizationList != null && vehicleAuthorizationList.size() > 0) {
            for (VehicleAuthorization data : vehicleAuthorizationList) {
                // 授权状态
                data.setAuthorizationStatus(AuthorizationStatusEnums.AUTHORIZATIONOUTTIME.getType());
                data.setUpdater(CommonUtil.SysUpdateUser);
                data.setUpdateTime(nowDate);
                // 更新授权表
                vehicleAuthorizationRepository.updateByPrimaryKeySelective(data);
                // 登录工单操作日志表
                saveOperateHistory(data, OperateContentEnums.AUTHORIZATIONOUTTIME.getType());
                // 登录授权日志表
                saveAuthorizationHistory(data);
                // 根据任务工单，获取收车公司派单任务
                Example exam = new Example(VehicleTaskRecovery.class);
                Example.Criteria criter = exam.createCriteria();
                criter.andEqualTo("taskId", data.getTaskId());
                exam.setOrderByClause(" create_time desc limit 1");
                String recoveryCompanyId = "";
                if(recoveryTaskRepository.selectOneByExample(exam) != null ){
                    VehicleTaskRecovery vehicleTaskRecovery = recoveryTaskRepository.selectOneByExample(exam);
                    recoveryCompanyId = vehicleTaskRecovery.getRecoveryCompanyId();
                    // 状态更新为 待收车
                    vehicleTaskRecovery.setStatus(RecoveryTaskStatusEnums.WAITRECOVERY.getType());
                    vehicleTaskRecovery.setUpdateTime(nowDate);
                    vehicleTaskRecovery.setUpdater(CommonUtil.SysUpdateUser);
                    // 更新收车公司任务表
                    recoveryTaskRepository.updateByPrimaryKey(vehicleTaskRecovery);
                }
                Example examp = new Example(VehicleTask.class);
                Example.Criteria crit = examp.createCriteria();
                crit.andEqualTo("id", data.getTaskId());
                VehicleTask task = vehicleTaskRepository.selectOneByExample(examp);
                // 授权失效时，给已授权人员+ 内勤人员 推送消息
                createAuthOutTimePush(task.getPlate(), "",data.getUserId(),recoveryCompanyId);
            }
        }
        logger.info("分析授权任务是否失效处理结束");
        return ResponseEnum.SUCCESS.getMark();
    }

    /**
     * 登录工单操作日志表
     *
     * @param data
     * @param operateContent 操作内容
     */
    private void saveOperateHistory(VehicleAuthorization data, String operateContent){
        Date nowDate = new Date();
        VehicleTaskOperateHistory vehicleTaskOperateHistory = new VehicleTaskOperateHistory();
        // 工单id
        vehicleTaskOperateHistory.setTaskId(data.getTaskId());
        // 操作人员
        vehicleTaskOperateHistory.setUserId(CommonUtil.SysUpdateUser);
        // 备注
        vehicleTaskOperateHistory.setRemark(OperateContentEnums.getEnum(operateContent).getValue());
        // 操作内容
        vehicleTaskOperateHistory.setOperateContent(operateContent);
        // 操作时间
        vehicleTaskOperateHistory.setOperateTime(data.getAuthorizationOutTimeDate());
        vehicleTaskOperateHistory.setCreateTime(nowDate);
        vehicleTaskOperateHistory.setCreator(CommonUtil.SysUpdateUser);
        vehicleTaskOperateHistory.setUpdateTime(nowDate);
        vehicleTaskOperateHistory.setUpdater(CommonUtil.SysUpdateUser);
        vehicleTaskOperateHistoryRepository.insertOne(vehicleTaskOperateHistory);
    }
    /**
     * 登录授权日志表
     *
     * @param data
     */
    private void saveAuthorizationHistory(VehicleAuthorization data){
        // 登录授权日志表
        AuthorizationHistory history = new AuthorizationHistory();
        // 授权id
        history.setAuthorizationId(data.getId());
        // 用户id
//        history.setUserId(userId);
        // 操作时间
        history.setOperateTime(data.getAuthorizationOutTimeDate());
        // 操作内容
        history.setOperateContent(OperateContentEnums.AUTHORIZATIONOUTTIME.getType());
        history.setRemark(OperateContentEnums.AUTHORIZATIONOUTTIME.getValue());
        authorizationHistoryRepository.insertOne(history);
    }


    /**
     * 授权失效时推送消息给小组成员和内勤人员以及获取授权的人，并插入消息中心表,推送结果消息推送状态表
     *
     * @param plate 车牌号
     * @param userId 用户id
     * @param authorizationUserId 已授权用户id
     */
    @Transactional
    private void createAuthOutTimePush( String plate, String userId,String authorizationUserId,String recoveryCompanyId) {
        // 推送内容
        String content = MessageTempletConstants.templet_203.replace(MessageTempletConstants.templet_plate,plate);
        Date nowDate = new Date();
        // 获取内勤人员信息
        GroupUserClientVo groupUserClientVo = getUserClientByRecoveryCompanyId(recoveryCompanyId);
        if(groupUserClientVo != null){
            // 登录消息中心表数据----内勤人员
            MessageCenter messageCenter = insertMessageCenter(content,groupUserClientVo.getUserId(),nowDate,userId, MessageTriggerEnum.AUTH_203.getCode());
            messageCenterRepository.insertOne(messageCenter);
            // 推送消息 --- 内勤人员
            List<GroupUserClientVo>  groupUserClientVoList = new ArrayList<>();
            groupUserClientVoList.add(groupUserClientVo);
            push(userId, groupUserClientVoList, content, groupUserClientVo.getClient(), PushCastEnum.UNICAST.getCode(),MessageTriggerEnum.AUTH_203.getNane(), groupUserClientVo.getDeviceToken());
        }
        if(StringUtil.isNotNull(authorizationUserId)){
            // 获取已授权人员信息
            GroupUserClientVo userClientVo = getUserClientByUserId(authorizationUserId);
            // 登录消息中心表数据----已授权人员
            MessageCenter messageCenter = insertMessageCenter(content,authorizationUserId,nowDate,userId, MessageTriggerEnum.AUTH_203.getCode());
            messageCenterRepository.insertOne(messageCenter);
            if(userClientVo != null){
                // 推送消息 --- 已授权人员
                List<GroupUserClientVo>  groupUserClientVoList = new ArrayList<>();
                groupUserClientVoList.add(userClientVo);
                push(userId, groupUserClientVoList, content, userClientVo.getClient(), PushCastEnum.UNICAST.getCode(),MessageTriggerEnum.AUTH_203.getNane(),userClientVo.getDeviceToken());
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
        messageCenter.setType(MessageTypeEnum.AUTH.getCode()); //消息类型:任务小消息
//        messageCenter.setTag(MessageTagEnum.RECORD.getCode()); //消息标签：记录
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
}
