package cn.com.leadu.cmsxc.assistant.service.impl;

import cn.com.leadu.cmsxc.assistant.service.CaseRecordService;
import cn.com.leadu.cmsxc.assistant.service.RoleManagerService;
import cn.com.leadu.cmsxc.common.constant.Constants;
import cn.com.leadu.cmsxc.common.constant.enums.*;
import cn.com.leadu.cmsxc.common.util.ArrayUtil;
import cn.com.leadu.cmsxc.common.util.CommonUtil;
import cn.com.leadu.cmsxc.common.util.MessageTempletConstants;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.common.util.push.UmengPushUtils;
import cn.com.leadu.cmsxc.data.appbusiness.repository.*;
import cn.com.leadu.cmsxc.data.appuser.repository.*;
import cn.com.leadu.cmsxc.data.system.repository.SystemUserRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTask;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskGroup;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskRecovery;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.GroupUserClientVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.GroupUserListVo;
import cn.com.leadu.cmsxc.pojo.appuser.entity.MessageCenter;
import cn.com.leadu.cmsxc.pojo.appuser.entity.MessagePushHistory;
import cn.com.leadu.cmsxc.pojo.appuser.entity.UserDeviceInfo;
import cn.com.leadu.cmsxc.pojo.appuser.entity.VehicleAuthorization;
import cn.com.leadu.cmsxc.pojo.assistant.vo.CaseRecordListParamVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.CaseRecordListVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.CaseRecordVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.RecoveryCompanysVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
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

/**
 * Created by yuanzhenxia on 2018/5/7.
 *
 * 寻车助手---案件管理impl
 */
@Service
public class CaseRecordServiceImpl implements CaseRecordService{
    private static final Logger logger = LoggerFactory.getLogger(CaseRecordServiceImpl.class);
    @Autowired
    private RecoveryCompanyRepository recoveryCompanyRepository;
    @Autowired
    private CaseRecordRepository caseRecordRepository;
    @Autowired
    private SystemUserRepository systemUserRepository;
    @Autowired
    private VehicleTaskRepository vehicleTaskRepository;
    @Autowired
    private RecoveryTaskRepository recoveryTaskRepository;
    @Autowired
    private VehicleAuthorizationRepository vehicleAuthorizationRepository;
    @Autowired
    private UserDeviceInfoRepository userDeviceInfoRepository;
    @Autowired
    private MessageCenterRepository messageCenterRepository;
    @Autowired
    private MessagePushHistoryRepository messagePushHistoryRepository;
    @Autowired
    private VehicleTaskGroupRepository vehicleTaskGroupRepository;
    @Autowired
    private RecoveryGroupUserRepository recoveryGroupUserRepository;
    @Autowired
    private AuditorAreaRepository auditorAreaRepository;
    /**
     * 根据委托公司id获取合作收车公司信息
     *
     * @param systemUser 用户信息
     * @return
     */
    public ResponseEntity<RestResponse> getRecoveryCompanys(SystemUser systemUser) {
        if(StringUtil.isNull(systemUser.getLeaseId())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE, "", "用户没有所属委托公司"),
                    HttpStatus.OK);
        }
        List<RecoveryCompanysVo> recoveryCompanysVoList = recoveryCompanyRepository.selectRecoveryCompanysByLeaseId(systemUser.getLeaseId());
        // 所有合作的收车公司 + 其他
        RecoveryCompanysVo vo = new RecoveryCompanysVo();
        vo.setRecoveryCompanyId("000000");
        vo.setRecoveryCompanyName("其他");
        recoveryCompanysVoList.add(vo);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, recoveryCompanysVoList, ""),
                HttpStatus.OK);
    }
    /**
     * 寻车助手---案件管理列表
     *
     * @param systemUser 用户信息
     * @param vo 参数信息
     * @return
     */
    public ResponseEntity<RestResponse> getCaseRecordList(SystemUser systemUser, CaseRecordListParamVo vo){
        // 获取委托公司一级管理员用户名
        Example example = new Example(SystemUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("leaseId", systemUser.getLeaseId());
        criteria.andEqualTo("userRole", UserRoleEnums.LEASE_ADMIN.getType());
        // 委托公司一级管理员信息
        SystemUser user = systemUserRepository.selectOneByExample(example);
        vo.setLeaseCompanyUserName(user.getUserId());
        vo.setLeaseId(systemUser.getLeaseId());
        // 获取审核人员管辖范围
        List<String> resultList =  auditorAreaRepository.selectProvinces(systemUser.getId());
        vo.setProvinces(resultList);
        // 设置用户角色信息
        vo.setUserRole(systemUser.getUserRole());
        List<CaseRecordListVo> caseRecordListVoList = caseRecordRepository.selectCaseRecordListByLeaseAuditor(vo,vo.getPage(),vo.getSize());
        if(ArrayUtil.isNotNullAndLengthNotZero(caseRecordListVoList)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, caseRecordListVoList, ""),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE, "", "数据加载完毕"),
                HttpStatus.OK);
    }
    /**
     * 寻车助手---案件详情
     *
     * @param taskId 任务id
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    public ResponseEntity<RestResponse> getCaseRecordDetail(String taskId , String recoveryCompanyId){
        List<CaseRecordVo> newCaseRecordVoList = new ArrayList<>();
        // 如果小组id不为空则根据小组id查看案件记录，如果小组id为空则根据记录人员id查看记录
        List<CaseRecordVo> caseRecordVoList = caseRecordRepository.selectCaseRecordDetail(taskId,recoveryCompanyId);
        if(ArrayUtil.isNotNullAndLengthNotZero(caseRecordVoList)){
            for(CaseRecordVo vo : caseRecordVoList){
                vo.setPhotoUrls(CommonUtil.breakStringByComma(vo.getPhotoUrl()));
                newCaseRecordVoList.add(vo);
            }
        }
        if(ArrayUtil.isNotNullAndLengthNotZero(newCaseRecordVoList)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,caseRecordVoList,""),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未查询到信息"),
                HttpStatus.OK);
    }
    /**
     * 催单推送
     *
     * @param plate 车牌号
     * @param reminderContent 催单内容
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> reminderPush(SystemUser user ,String plate, String reminderContent){
        if(StringUtil.isNull(reminderContent)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","催单内容为空"),
                    HttpStatus.OK);
        }
        // 去除催单内容中的表情符号
        reminderContent = CommonUtil.filterEmoji(reminderContent);
        String content = MessageTempletConstants.templet_107.replace(MessageTempletConstants.templet_plate,plate).replace(MessageTempletConstants.templet_remindercontent,reminderContent);
        Date nowDate = new Date();
        // 根据车牌号获取任务工单id
        VehicleTask vehicleTask = selectVehicleTeskByplate(plate);
        if(vehicleTask == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","收车任务不存在！"),
                    HttpStatus.OK);
        }
        // 获取此任务授权信息
        VehicleAuthorization vehicleAuthorization = selectByTaskIdAndAuthorizationStatus( vehicleTask.getId(), AuthorizationStatusEnums.AUTHORIZETED.getType());
        // 1.向获得授权的用户推送消息
        if(vehicleAuthorization != null){
            GroupUserClientVo userClientVo = getUserClientByUserId(vehicleAuthorization.getUserId());
            // 登录消息中心表数据----已授权人员
            MessageCenter messageCenter = insertMessageCenter(content,vehicleAuthorization.getUserId(),nowDate,user.getUserId(), MessageTriggerEnum.TASK_107.getCode());
            messageCenterRepository.insertOne(messageCenter);
            if(userClientVo != null){
                // 推送消息 --- 已授权人员
                List<GroupUserClientVo>  groupUserClientVoList = new ArrayList<>();
                groupUserClientVoList.add(userClientVo);
                push(user.getUserId(), groupUserClientVoList, content, userClientVo.getClient(), PushCastEnum.UNICAST.getCode(),MessageTriggerEnum.TASK_107.getNane(),userClientVo.getDeviceToken());
            }
        }
        // 获取此任务分配哪个收车公司
        VehicleTaskRecovery vehicleTaskRecovery = getRecoveryTaskByTaskId(String.valueOf(vehicleTask.getId()));
        // a,已经派单的情况
        if(vehicleTaskRecovery != null){
            // 获取内勤人员信息    ----- 推送消息
            GroupUserClientVo groupUserClientVo = recoveryGroupUserRepository.selectUserClientByRecoveryCompanyId(vehicleTaskRecovery.getRecoveryCompanyId());
            if(groupUserClientVo != null){
                // 登录消息中心表数据----内勤人员
                MessageCenter messageCenter = insertMessageCenter(content,groupUserClientVo.getUserId(),nowDate,user.getUserId(), MessageTriggerEnum.TASK_107.getCode());
                messageCenterRepository.insertOne(messageCenter);
                // 推送消息 --- 内勤人员
                List<GroupUserClientVo>  groupUserClientVoList = new ArrayList<>();
                groupUserClientVoList.add(groupUserClientVo);
                push(user.getUserId(), groupUserClientVoList, content, groupUserClientVo.getClient(), PushCastEnum.UNICAST.getCode(),MessageTriggerEnum.TASK_107.getNane(), groupUserClientVo.getDeviceToken());
            }
            // 获取任务分组情况
            List<VehicleTaskGroup> vehicleTaskGroupList = getByVehicleTaskRecoveryId(vehicleTaskRecovery.getId());
            if(ArrayUtil.isNotNullAndLengthNotZero(vehicleTaskGroupList)){
                GroupUserListVo vo = getGroupUserClientByGroupId(vehicleTaskGroupList.get(0).getGroupId());
                if(vo != null){
                    List<MessageCenter> messageCenters = new ArrayList();
                    //小组用户集合不为空
                    if(ArrayUtil.isNotNullAndLengthNotZero(vo.getGroupUsers())){
                        //遍历小组用户集合
                        for(String groupUser : vo.getGroupUsers()){
                            // 登录消息中心表数据 --- 小组成员
                            messageCenters.add(insertMessageCenter(content,groupUser,nowDate,user.getUserId(),MessageTriggerEnum.TASK_107.getCode())) ;
                        }
                        //批量插入消息中心表
                        messageCenterRepository.insertMore(messageCenters);
                    }
                    //ios设备集合不为空，调用推送共同推送消息
                    if(ArrayUtil.isNotNullAndLengthNotZero(vo.getIosUsers())){
                        push(user.getUserId(), vo.getIosUsers(), content, ClientTypeEnums.IOS.getCode(),PushCastEnum.LISTCAST.getCode(),MessageTriggerEnum.TASK_107.getNane(),null);
                    }
                    //android设备集合不为空，调用推送共同推送消息
                    if(ArrayUtil.isNotNullAndLengthNotZero(vo.getAndroidUsers())){
                        push(user.getUserId(), vo.getAndroidUsers(), content, ClientTypeEnums.ANDROID.getCode(),PushCastEnum.LISTCAST.getCode(),MessageTriggerEnum.TASK_107.getNane(),null);
                    }
                }
            }
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","催单推送成功！"),
                HttpStatus.OK);
    }
    /**
     * 根据车牌号获取车辆任务工单信息
     * @param plate 车牌号
     * @return
     */
    public VehicleTask selectVehicleTeskByplate(String plate){
        if(StringUtil.isNotNull(plate)) {
            Example example = new Example(VehicleTask.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("plate", plate);
            example.setOrderByClause(" update_time desc ");
            return vehicleTaskRepository.selectOneByExample(example);
        }
        return null;
    }
    /**
     * 根据任務id获取收车公司派单
     *
     * @param taskId 任務id
     * @return
     */
    public VehicleTaskRecovery getRecoveryTaskByTaskId(String taskId){
        if(StringUtil.isNotNull(taskId)) {
            Example example = new Example(VehicleTaskRecovery.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("taskId", Long.valueOf(taskId));
            return recoveryTaskRepository.selectOneByExample(example);
        }
        return null;
    }
    /**
     * 根据授权ID获取授权信息
     *
     * @param taskId 工单id
     * @param authorizationStatus 授权状态
     * @return
     */
    public VehicleAuthorization selectByTaskIdAndAuthorizationStatus(Long taskId, String authorizationStatus){
        if(StringUtil.isNotNull(taskId) && StringUtil.isNotNull(authorizationStatus)) {
            Example example = new Example(VehicleAuthorization.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("taskId", String.valueOf(taskId));
            criteria.andEqualTo("authorizationStatus", authorizationStatus);
            return vehicleAuthorizationRepository.selectOneByExample(example);
        }
        return null;
    }
    /**
     * 根据用户id（手机号）获取用户信息
     * @param userId 用户id
     * @return
     */
    public SystemUser selectSystemUserByUserId(String userId){
        if(StringUtil.isNotNull(userId)) {
            Example example = new Example(SystemUser.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId", userId);
            return systemUserRepository.selectOneByExample(example);
        }
        return null;
    }
    /**
     * 根据收车公司任务表id获取任务分组情况
     *
     * @param vehicleTaskRecoveryId 收车公司任务表主键
     * @return
     */
    public List<VehicleTaskGroup> getByVehicleTaskRecoveryId(String vehicleTaskRecoveryId){
        if(StringUtil.isNotNull(vehicleTaskRecoveryId)) {
            Example example = new Example(VehicleTaskGroup.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("vehicleTaskRecoveryId", vehicleTaskRecoveryId);
            return vehicleTaskGroupRepository.selectByExampleList(example);
        }
        return null;
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
     * 推送消息，登录消息推送日志表
     *
     * @param userId 用户id
     * @param voList 安卓或iOS用户信息
     * @param content 消息内容
     * @param clientType "1":ios,"0":安卓
     */
    @Transactional
    private void push(String userId, List<GroupUserClientVo> voList, String content, String clientType, String pushCast,String messageTrigger,String deviceToken) {
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
}
