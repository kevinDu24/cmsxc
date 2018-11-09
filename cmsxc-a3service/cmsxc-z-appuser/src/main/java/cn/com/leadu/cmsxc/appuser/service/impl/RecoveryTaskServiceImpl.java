package cn.com.leadu.cmsxc.appuser.service.impl;

import cn.com.leadu.cmsxc.appbusiness.service.RecoveryGroupService;
import cn.com.leadu.cmsxc.appbusiness.service.VehicleTaskSearchHistoryService;
import cn.com.leadu.cmsxc.appbusiness.service.VehicleTaskService;
import cn.com.leadu.cmsxc.appuser.service.*;
import cn.com.leadu.cmsxc.appuser.util.constant.enums.*;
import cn.com.leadu.cmsxc.common.constant.Constants;
import cn.com.leadu.cmsxc.common.constant.enums.*;
import cn.com.leadu.cmsxc.common.util.ArrayUtil;
import cn.com.leadu.cmsxc.common.util.CommonUtil;
import cn.com.leadu.cmsxc.common.util.MessageTempletConstants;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.common.util.push.UmengPushUtils;
import cn.com.leadu.cmsxc.data.appbusiness.repository.*;
import cn.com.leadu.cmsxc.data.appuser.repository.AuthorizationHistoryRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.MessageCenterRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.MessagePushHistoryRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.VehiclePhotoPathRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.*;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.*;
import cn.com.leadu.cmsxc.pojo.appuser.entity.*;
import cn.com.leadu.cmsxc.pojo.appuser.vo.RecoveryDetailVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.RecoveryVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.applydetail.ApplyAuthVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.applydetail.AuthDelayVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.applydetail.AuthInfoVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.applydetail.ObtainAuthVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
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
 * Created by yuanzhenxia on 2018/2/12.
 *
 * 收车公司派单任务实现类
 */
@Service
public class RecoveryTaskServiceImpl implements RecoveryTaskService{
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private VehicleTaskRepository vehicleTaskRepository;
    @Autowired
    private RecoveryTaskRepository recoveryTaskRepository;
    @Autowired
    private VehicleTaskService vehicleTaskService;
    @Autowired
    private VehicleTaskOperateHistoryRepository taskOperatorHistoryRepository;
    @Autowired
    private VehicleTaskSearchHistoryService vehicleTaskSearchHistoryService;
    @Autowired
    private VehicleTaskSearchHistoryRepository taskSearchHistoryRepository;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private RecoveryGroupUserRepository recoveryGroupUserRepository;
    @Autowired
    private VehiclePhotoPathRepository vehiclePhotoPathRepository;
    @Autowired
    private AuthorizationHistoryRepository authorizationHistoryRepository;
    @Autowired
    private RecoveryGroupService recoveryGroupService;
    @Autowired
    private VehicleTaskGroupRepository vehicleTaskGroupRepository;
    @Autowired
    private TaskAssignHistoryRepository taskAssignHisoryRepository;
    @Autowired
    private VehicleTaskGroupService vehicleTaskGroupService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private MessageCenterRepository messageCenterRepository;
    @Autowired
    private MessagePushHistoryRepository messagePushHistoryRepository;
    @Autowired
    private RecoveryGroupRepository recoveryGroupRepository;

    /**
     * 获取各状态收车公司任务派单数量
     *
     * @param systemUser 用户信息
     * @return
     */
    public ResponseEntity<RestResponse> getRecoveryTaskCountByStatus(SystemUser systemUser){
        RecoveryVo recoveryVo = new RecoveryVo();
        RecoveryTaskListVo recoveryTaskListVo = new RecoveryTaskListVo();
        String groupId = getGroupIdByUserId(systemUser.getUserId());
        if(StringUtil.isNull(systemUser.getRecoveryCompanyId()) || StringUtil.isNull(groupId)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","数据加载完毕"),
                    HttpStatus.OK);
        }
        // 分页获取该小组派单任务列表
        recoveryVo.setGroupId(groupId);
        // 设置当前页为0
        recoveryVo.setPage(0);
        // 设置每页数量为0
        recoveryVo.setSize(0);
        // 设置状态为1：待收车
        recoveryVo.setStatus(1);
        // 获取待收车数量
        List<RecoveryTaskVo> waitList = vehicleTaskRepository.selectRecoveryTaskList(recoveryVo);
        if(waitList != null && !waitList.isEmpty()){
            recoveryTaskListVo.setWaitCount(waitList.size());
        }
        // 设置状态为2：已授权
        recoveryVo.setStatus(2);
        // 获取已授权数量
        List<RecoveryTaskVo> authorizationList = vehicleTaskRepository.selectRecoveryTaskList(recoveryVo);
        if(authorizationList != null && !authorizationList.isEmpty()){
            recoveryTaskListVo.setAuthorizationCount(authorizationList.size());
        }
        // 设置状态为3：已完成
        recoveryVo.setStatus(3);
        // 获取待收车数量
        List<RecoveryTaskVo> finishList = vehicleTaskRepository.selectRecoveryTaskList(recoveryVo);
        if(finishList != null && !finishList.isEmpty()){
            recoveryTaskListVo.setFinishCount(finishList.size());
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,recoveryTaskListVo,""),
                HttpStatus.OK);
    }

    /**
     * 分页获取收车公司任务派单列表
     *
     * @param recoveryVo 画面参数
     * @param systemUser 用户信息
     * @return
     */
   public ResponseEntity<RestResponse> getRecoveryTaskList(SystemUser systemUser, RecoveryVo recoveryVo){
       String groupId = getGroupIdByUserId(systemUser.getUserId());
       if(StringUtil.isNull(systemUser.getRecoveryCompanyId()) || StringUtil.isNull(groupId)){
           return new ResponseEntity<RestResponse>(
                   RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","数据加载完毕"),
                   HttpStatus.OK);
       }
       // 分页获取该小组派单任务列表
       recoveryVo.setGroupId(groupId);
       // 去除车牌号中的表情符号
       if(StringUtil.isNotNull(recoveryVo.getPlate())){
           recoveryVo.setPlate(CommonUtil.filterEmoji(recoveryVo.getPlate()));
       }
       List<RecoveryTaskVo> recoveryTaskVoList = vehicleTaskRepository.selectRecoveryTaskList(recoveryVo);
       if(recoveryTaskVoList != null && !recoveryTaskVoList.isEmpty()){
           return new ResponseEntity<RestResponse>(
                   RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,recoveryTaskVoList,""),
                   HttpStatus.OK);
       // 如果根据车牌号查询，查询不到数据，返回为查询到车辆信息
       }else if("0".equals(String.valueOf(recoveryVo.getStatus()))){
           return new ResponseEntity<RestResponse>(
                   RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未查询到车辆信息"),
                   HttpStatus.OK);
       }
       return new ResponseEntity<RestResponse>(
               RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","数据加载完毕"),
               HttpStatus.OK);
   }

    /**
     * 分页获取派单管理列表
     *
     * @param recoveryVo 画面参数
     * @param systemUser 用户信息
     * @return
     */
    public ResponseEntity<RestResponse> getTaskManagerList(SystemUser systemUser, RecoveryVo recoveryVo){
        if(StringUtil.isNull(systemUser.getRecoveryCompanyId())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","数据加载完毕"),
                    HttpStatus.OK);
        }
        // 分页获取该小组派单任务列表
        recoveryVo.setRecoveryCompanyId(systemUser.getRecoveryCompanyId());
        List<RecoveryTaskManagerVo> recoveryTaskVoList = vehicleTaskRepository.getTaskManagerList(recoveryVo);
        if(recoveryTaskVoList != null && !recoveryTaskVoList.isEmpty()){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,recoveryTaskVoList,""),
                    HttpStatus.OK);
            // 如果根据车牌号查询，查询不到数据，返回为查询到车辆信息
        }else if("0".equals(String.valueOf(recoveryVo.getStatus()))){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未查询到车辆信息"),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","数据加载完毕"),
                HttpStatus.OK);
    }

    /**
     * 获取所有小组及该单的分配信息接口
     *
     * @param taskId 工单id
     * @param systemUser 用户信息
     * @return
     */
    public ResponseEntity<RestResponse> getAssginInfo(SystemUser systemUser, String taskId){
        //初始化返回值
        AssignedGroupVo vo= new AssignedGroupVo();
        //通过收车公司id获取该收车公司下所有的小组信息
        List<RecoveryGroupVo> allGroups= recoveryGroupService.selectByRecoveryCompanyId(systemUser.getRecoveryCompanyId());
        vo.setAllGroups(allGroups);
        //通过工单id获得收车公司任务表id
        Example example = new Example(VehicleTaskRecovery.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", taskId);
        VehicleTaskRecovery vehicleTaskRecovery = recoveryTaskRepository.selectOneByExample(example);
        if(RecoveryTaskStatusEnums.CANCEL.getType().equals(vehicleTaskRecovery.getStatus())
                || RecoveryTaskStatusEnums.SELFFINISH.getType().equals(vehicleTaskRecovery.getStatus())
                || RecoveryTaskStatusEnums.OTHERFINISH.getType().equals(vehicleTaskRecovery.getStatus())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该任务状态有变更，请刷新后重试"),
                    HttpStatus.OK);
        }
        String recoveryTaskId = vehicleTaskRecovery.getId(); //收车公司任务表id
        //通过收车公司任务表id获得该任务分配小组
        Example example1 = new Example(VehicleTaskGroup.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("vehicleTaskRecoveryId", recoveryTaskId);
        List<VehicleTaskGroup> groups = vehicleTaskGroupRepository.selectByExampleList(example1);
        //已分配的小组id集合
        List<String> assignedGroup = new ArrayList();
        if(ArrayUtil.isNotNullAndLengthNotZero(groups)){
            for(VehicleTaskGroup group : groups){
                assignedGroup.add(group.getGroupId());
            }
        }
        vo.setAssignedGroupId(assignedGroup);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,vo,""),
                HttpStatus.OK);
    }

    /**
     * 分配任务
     *
     * @param vo 任务分配参数
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> assginTask(SystemUser systemUser, AssignedTaskVo vo){
        if(ArrayUtil.isNullOrLengthZero(vo.getGroupIds())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","请选择需要分配的小组"),
                    HttpStatus.OK);
        }
        //通过工单id获得收车公司任务表id
        Example example = new Example(VehicleTaskRecovery.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", vo.getTaskId());
        VehicleTaskRecovery vehicleTaskRecovery = recoveryTaskRepository.selectOneByExample(example);
        //派单该工单状态有无变更
        if(RecoveryTaskStatusEnums.CANCEL.getType().equals(vehicleTaskRecovery.getStatus())
                || RecoveryTaskStatusEnums.SELFFINISH.getType().equals(vehicleTaskRecovery.getStatus())
                || RecoveryTaskStatusEnums.OTHERFINISH.getType().equals(vehicleTaskRecovery.getStatus())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该任务状态有变更，请刷新后重试"),
                    HttpStatus.OK);
        }
        List<VehicleTaskGroup> vehicleTaskGroups = new ArrayList(); //小组任务表登录数据集合
        List<TaskAssignHistory> taskAssignHistories = new ArrayList(); //任务分配日志表登录新分配的数据集合
        VehicleTaskGroup vehicleTaskGroup;
        TaskAssignHistory taskAssignHistory;
        Date nowDate = new Date();
        for(RecoveryGroupVo item : vo.getGroupIds()){
            //构建小组任务表数据
            vehicleTaskGroup = new VehicleTaskGroup();
            vehicleTaskGroup.setVehicleTaskRecoveryId(vehicleTaskRecovery.getId()); //收车公司任务id
            vehicleTaskGroup.setAssignmentTime(nowDate); //分配时间
            vehicleTaskGroup.setGroupId(item.getGroupId());//小组id
            vehicleTaskGroup.setCreateTime(nowDate);
            vehicleTaskGroup.setUpdateTime(nowDate);
            vehicleTaskGroup.setCreator(systemUser.getUserId());
            vehicleTaskGroup.setUpdater(systemUser.getUserId());
            vehicleTaskGroups.add(vehicleTaskGroup);
            //构建任务分配日志表数据
            taskAssignHistory = new TaskAssignHistory();
            taskAssignHistory.setGroupId(vehicleTaskGroup.getGroupId()); //组id
            taskAssignHistory.setGroupName(item.getGroupName()); //组名称
            taskAssignHistory.setRecoveryCompanyId(vehicleTaskRecovery.getRecoveryCompanyId()); //收车公司id
            taskAssignHistory.setTaskId(Long.parseLong(vo.getTaskId())); //工单id
            taskAssignHistory.setOperateContent(TaskAssignOperateEnums.ASSIGN_TASK.getType()); //操作内容
            taskAssignHistory.setCreateTime(nowDate);
            taskAssignHistory.setUpdateTime(nowDate);
            taskAssignHistory.setCreator(systemUser.getUserId());
            taskAssignHistory.setUpdater(systemUser.getUserId());
            taskAssignHistories.add(taskAssignHistory);
        }
        //根据对应的收车公司任务表id到小组任务表中查询该任务曾经分配的小组
        List<VehicleTaskGroup> vehicleTaskGroupList = vehicleTaskGroupService.getByVehicleTaskRecoveryId(vehicleTaskRecovery.getId());
        //若有，删除之前分配的小组任务,推送消息，插入任务操作日志表，消息推送日志表
        if(ArrayUtil.isNotNullAndLengthNotZero(vehicleTaskGroupList)){
            for(VehicleTaskGroup item : vehicleTaskGroupList){
                vehicleTaskGroupRepository.delete(item);
            }
            List<TaskAssignHistory> taskAssignHistoriesOld = new ArrayList(); //任务分配日志表登录数据集合
            TaskAssignHistory taskAssignHistoryold;
            for(VehicleTaskGroup item : vehicleTaskGroupList){
                //通过小组id获取小组名称
                RecoveryGroup recoveryGroup = recoveryGroupRepository.selectByPrimaryKey(item.getGroupId());
                String groupName = recoveryGroup == null ? null : recoveryGroup.getGroupName();
                taskAssignHistoryold = new TaskAssignHistory();
                taskAssignHistoryold.setGroupId(item.getGroupId()); //组id
                taskAssignHistoryold.setGroupName(groupName); //组名称
                taskAssignHistoryold.setRecoveryCompanyId(vehicleTaskRecovery.getRecoveryCompanyId()); //收车公司id
                taskAssignHistoryold.setTaskId(Long.parseLong(vo.getTaskId())); //工单id
                taskAssignHistoryold.setOperateContent(TaskAssignOperateEnums.CHANGE_TASK.getType()); //操作内容
                taskAssignHistoryold.setCreateTime(nowDate);
                taskAssignHistoryold.setUpdateTime(nowDate);
                taskAssignHistoryold.setCreator(systemUser.getUserId());
                taskAssignHistoryold.setUpdater(systemUser.getUserId());
                taskAssignHistoriesOld.add(taskAssignHistoryold);
            }
            //将分配记录插入任务分配日志表
            taskAssignHisoryRepository.insertMore(taskAssignHistoriesOld);
            //任务改派消息通知动作
            assginTaskPush(vehicleTaskGroupList.get(0).getGroupId(), vo.getTaskId(), systemUser.getUserId(),"1"); //目前只分配给一个小组，只取一个小组id
        }
        //更新收车公司任务表，分配flag为已分配、同时更新分配时间
        vehicleTaskRecovery.setAssignmentFlag(AssignmentEnums.ASSIGNED.getType());
        vehicleTaskRecovery.setAssignDate(nowDate);
        recoveryTaskRepository.updateByPrimaryKey(vehicleTaskRecovery);
        //将分配的小组任务插入小组任务表
        vehicleTaskGroupRepository.insertMore(vehicleTaskGroups);
        //将分配记录插入任务分配日志表
        taskAssignHisoryRepository.insertMore(taskAssignHistories);
        //任务分配消息通知动作
        assginTaskPush(vo.getGroupIds().get(0).getGroupId(), vo.getTaskId(), systemUser.getUserId(),"0"); //目前只分配给一个小组，只取一个小组id
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","任务分配成功"),
                HttpStatus.OK);
    }

    /**
     * 分配任务时推送消息给小组成员，并插入消息中心表,推送结果消息推送状态表
     * @param flag "0":推送给最新分配的小组、"1":推送给之前分配的小组
     */
    @Transactional
    private void assginTaskPush(String groupId, String taskId, String userId, String flag) {
        GroupUserListVo vo = organizationService.getGroupUserClientByGroupId(groupId);
        if(vo == null){
            return;
        }
        //通过工单id取得该工单对应的车牌号
        VehicleTask task = vehicleTaskService.selectVehicleTeskById(taskId);
        String plate = task.getPlate();
        String content = "";
        if(flag.equals("0")){
            content = MessageTempletConstants.templet_101.replace(MessageTempletConstants.templet_plate,plate);
        } else if(flag.equals("1")){
            content = MessageTempletConstants.templet_102.replace(MessageTempletConstants.templet_plate,plate);
        }
        List<MessageCenter> messageCenters = new ArrayList();
        MessageCenter messageCenter;
        //小组用户集合不为空
        if(ArrayUtil.isNotNullAndLengthNotZero(vo.getGroupUsers())){
            //遍历小组用户集合
            Date nowDate = new Date();
            for(String groupUser : vo.getGroupUsers()){
                messageCenter = new MessageCenter();
                messageCenter.setType(MessageTypeEnum.TASK.getCode()); //消息类型
                messageCenter.setTag(MessageTagEnum.PUSH_TASK.getCode()); //消息标签
                //消息内容，把模板消息的参数替换掉
                messageCenter.setContent(content);
                //判断消息动作类型
                if(flag.equals("0")){
                    messageCenter.setActivity(MessageTriggerEnum.TASK_101.getCode());//消息触发动作
                } else if(flag.equals("1")){
                    messageCenter.setActivity(MessageTriggerEnum.TASK_102.getCode());//消息触发动作
                }
                messageCenter.setReceiver(groupUser); //接收者
                messageCenter.setIsReaded(MessageReadEnum.NO_READ.getCode()); //设定为未读消息
                messageCenter.setPushDate(nowDate); //推送时间
                messageCenter.setDeleteFlag(DeleteFlagEnum.ON.getCode()); //未删除
                messageCenter.setCreateTime(nowDate);
                messageCenter.setUpdateTime(nowDate);
                messageCenter.setCreator(userId);
                messageCenter.setUpdater(userId);
                messageCenters.add(messageCenter);
            }
            //批量插入消息中心表
            messageCenterRepository.insertMore(messageCenters);
        }
        //ios设备集合不为空，调用推送共同推送消息
        if(ArrayUtil.isNotNullAndLengthNotZero(vo.getIosUsers())){
            push(userId, vo, flag, content, ClientTypeEnums.IOS.getCode());
        }
        //android设备集合不为空，调用推送共同推送消息
        if(ArrayUtil.isNotNullAndLengthNotZero(vo.getAndroidUsers())){
            push(userId, vo, flag, content, ClientTypeEnums.ANDROID.getCode());
        }
    }


    /**
     * 推送消息，登录消息推送日志表
     *
     * @param userId
     * @param vo
     * @param flag "0":推送给最新分配的小组、"1":推送给之前分配的小组
     * @param content
     * @param clientType "1":ios,"0":安卓
     */
    @Transactional
    private void push(String userId, GroupUserListVo vo, String flag, String content, String clientType) {
        List<String> deviceTokens = new ArrayList(); //设备集合
        StringBuilder deviceTokenStr = new StringBuilder(); //消息推送日志表
        for(GroupUserClientVo item : ClientTypeEnums.IOS.getCode().equals(clientType) ? vo.getIosUsers() : vo.getAndroidUsers()){
            if(deviceTokenStr.length() > 0){
                deviceTokenStr = deviceTokenStr.append(Constants.COMMA);
            }
            deviceTokenStr = deviceTokenStr.append(item.getDeviceToken());
            deviceTokens.add(item.getDeviceToken());
        }
        MessagePushHistory messagePushHistory = new MessagePushHistory();
        messagePushHistory.setClient(clientType); //客户端类型
        messagePushHistory.setContent(content); //内容
        messagePushHistory.setDeviceTokens(deviceTokenStr.toString());
        String title = "";
        if(flag.equals("0")){
            title = MessageTriggerEnum.TASK_101.getNane();//消息触发动作
        } else if(flag.equals("1")){
            title = MessageTriggerEnum.TASK_102.getNane();//消息触发动作
        }
        try {
            int result = UmengPushUtils.push(PushCastEnum.LISTCAST.getCode(),clientType,null,
                    deviceTokens,title,content);
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

    /**
     * 根据任务id和收车公司id获取收车公司派单
     *
     * @param taskId 任务id
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    public VehicleTaskRecovery findRecoveryTaskByTaskIdAndRecoveryCompanyId(String taskId, String recoveryCompanyId){
        if(StringUtil.isNotNull(taskId) && StringUtil.isNotNull(recoveryCompanyId)) {
            Example example = new Example(VehicleTaskRecovery.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("taskId", taskId);
            criteria.andEqualTo("recoveryCompanyId", recoveryCompanyId);
            return recoveryTaskRepository.selectOneByExample(example);
        }
        return null;
    }
    /**
     * 根据任务id获取该任务的详情信息
     *
     * @param recoveryDetailVo 参数信息
     * @param userId 用户id
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> findRecoveryTaskDetail( String userId, RecoveryDetailVo recoveryDetailVo){
        if(StringUtil.isNull(userId)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","当前登录用户手机号为空"),
                    HttpStatus.OK);
        }
        // 获取车辆信息详情，获取GPS登录方式url，登录或更新查看历史表,查看车辆线索
        VehicleTaskResultVo vo = vehicleTaskService.findTaskDetail(userId, recoveryDetailVo.getTaskId(), recoveryDetailVo.getPlate());
        if(vo != null ){
            //车辆详情页面返回授权状态和备注，并获取委托方电话
            if(StringUtil.isNotNull(recoveryDetailVo.getAuthorizationId())){
                VehicleAuthorization authorizationById = authorizationService.selectByAuthorizationId(recoveryDetailVo.getAuthorizationId());
                if(authorizationById != null){
                    //TODO/******************兼容老版本开始****************************/
                    // 授权id
                    vo.setAuthorizationId(authorizationById.getId());
                    // 审批备注
                    vo.setApproveRemark(authorizationById.getApprovalRemark());
                    // 申请备注
                    vo.setRemark(authorizationById.getRemark());
                    // 申请时间
                    vo.setApplyStartDate(authorizationById.getApplyStartDate());
                    // 审批时间
                    vo.setApplyEndDate(authorizationById.getApplyEndDate());
                    // 截止时间
                    vo.setAuthOutTimeDate(authorizationById.getAuthorizationOutTimeDate());
                    // 授权证书url
                    vo.setAuthorizationPaperUrl(authorizationById.getAuthorizationPaperUrl());
                    // 授权状态
                    vo.setAuthorizationStatus(authorizationById.getAuthorizationStatus());
                    vo.setIsDelay(authorizationById.getIsDelay());
                    //TODO/******************兼容老版本结束****************************/

                    //获取申请基本信息
                    List<AuthInfoVo> authInfoVoList = buildAuthInfo(recoveryDetailVo, authorizationById);

                    vo.setAuthInfo(authInfoVoList);
                }
            }
            // 根据委托公司用户名查询委托公司联系电话
            SystemUser leaseUser = systemUserService.selectSystemUserByUserId(vo.getLeaseCompanyUserName());
            vo.setLeasePhone(leaseUser.getUserPhone());
            // 根据车牌号和用户id查询查看日志表，判断此用户是否查看过此车辆
            VehicleTaskSearchHistory vehicleTaskSearchHistory = vehicleTaskSearchHistoryService.selectByPlateAndUserId(recoveryDetailVo.getTaskId(), userId);
            // 从前未查看过，第一次查看
            if(vehicleTaskSearchHistory == null){
                // 登录工单查询历史表
                VehicleTaskSearchHistory history = new VehicleTaskSearchHistory();
                // 任务id
                history.setTaskId(Long.parseLong(recoveryDetailVo.getTaskId()));
                // 车牌号
                history.setPlate(recoveryDetailVo.getPlate());
                // 用户id
                history.setUserId(userId);
                // 查询时间
                history.setSearchTime(new Date());
                history.setUpdater(userId);
                history.setCreator(userId);
                history.setUpdateTime(new Date());
                history.setCreateTime(new Date());
                taskSearchHistoryRepository.insertOne(history);
                // 否则更新查看日志表
            }else{
                // 任务id
                vehicleTaskSearchHistory.setTaskId(Long.parseLong(recoveryDetailVo.getTaskId()));
                // 查看时间
                vehicleTaskSearchHistory.setSearchTime(new Date());
                vehicleTaskSearchHistory.setUpdateTime(new Date());
                vehicleTaskSearchHistory.setUpdater(userId);
                // 更新查看日志表
                taskSearchHistoryRepository.updateByPrimaryKey(vehicleTaskSearchHistory);
            }

            // 登录工单操作日志表
            VehicleTaskOperateHistory operatorHistory = new VehicleTaskOperateHistory();
            // 任务id
            operatorHistory.setTaskId(Long.parseLong(recoveryDetailVo.getTaskId()));
            // 用户id
            operatorHistory.setUserId(userId);
            // 操作时间
            operatorHistory.setOperateTime(new Date());
            // 操作内容
            operatorHistory.setOperateContent("查看我的派单详情");
            // 备注
            operatorHistory.setRemark("查看我的派单详情");
            operatorHistory.setCreator(userId);
            operatorHistory.setCreateTime(new Date());
            operatorHistory.setUpdater(userId);
            operatorHistory.setUpdateTime(new Date());
            taskOperatorHistoryRepository.insertOne(operatorHistory);
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
     * @param recoveryDetailVo
     * @param authorizationById
     * @return
     */
    public List<AuthInfoVo> buildAuthInfo(RecoveryDetailVo recoveryDetailVo, VehicleAuthorization authorizationById) {
        List<AuthInfoVo>  authInfoVoList = new ArrayList<>();
        //从授权历史表中查询其他节点信息
        Example example = new Example(AuthorizationHistory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("authorizationId", recoveryDetailVo.getAuthorizationId());
        example.setOrderByClause(" create_time desc ");
        List<AuthorizationHistory> list = authorizationHistoryRepository.selectByExampleList(example);
        if(list != null && !list.isEmpty()){
            //循环授权日志结果
            for(AuthorizationHistory item : list){
                AuthInfoVo authInfoVoTemp = new AuthInfoVo();
                //获得授权记录
                if(OperateContentEnums.AUTHORIZETED.getType().equals(item.getOperateContent())){
                    //创建获得授权结果实体类
                    ObtainAuthVo obtainAuthVo = new ObtainAuthVo();
                    obtainAuthVo.setPassTime(item.getOperateTime()); //审核时间
                    obtainAuthVo.setApproveRemark(item.getRemark()); //审核备注
                    obtainAuthVo.setAuthStartDate(item.getOperateTime()); //授权有效开始时间
                    obtainAuthVo.setAuthOutTimeDate(authorizationById.getAuthorizationOutTimeDate()); //授权有效结束时间
                    authInfoVoTemp.setObtainAuth(obtainAuthVo);
                    authInfoVoTemp.setType(4);
                    authInfoVoList.add(authInfoVoTemp);
                } else if(OperateContentEnums.DELAY.getType().equals(item.getOperateContent())){ //授权延期
                    //穿件授权延期结果实体类
                    AuthDelayVo authDelayVo = new AuthDelayVo();
                    authDelayVo.setDelayTime(item.getOperateTime()); //延期时间
                    authDelayVo.setDelayRemark(item.getRemark()); //延期备注
                    authDelayVo.setAuthStartDate(item.getOperateTime());  //授权有效开始时间
                    authDelayVo.setAuthOutTimeDate(authorizationById.getAuthorizationOutTimeDate()); //授权有效结束时间
                    authInfoVoTemp.setAuthDelay(authDelayVo);
                    authInfoVoTemp.setType(5);
                    authInfoVoList.add(authInfoVoTemp);
                }
            }
        }
        AuthInfoVo authInfoVo = new AuthInfoVo();
        //申请授权
        ApplyAuthVo applyAuthVo = new ApplyAuthVo();
        applyAuthVo.setApplyTime(authorizationById.getCreateTime()); // 申请时间
        applyAuthVo.setName(authorizationById.getApplicantName()); //申请人姓名
        applyAuthVo.setPhoneNum(authorizationById.getApplicantPhone()); //电话号码
        applyAuthVo.setIdentityNum(authorizationById.getApplicantIdentityNum()); //身份证号
        applyAuthVo.setRemark(authorizationById.getRemark()); //申请备注
        applyAuthVo.setAddress(authorizationById.getAddress()); //位置
        List<VehiclePhotoPath> vehiclePhotoPathList = vehiclePhotoPathRepository.selectByVehiclePhotoUUID(authorizationById.getPhotoUuid());
        //申请相关其他照片集合
        List<String> otherPhotos = new ArrayList();
        //获取附件信息
        for(VehiclePhotoPath vehiclePhotoPath : vehiclePhotoPathList){
            if(ApplyAuthFileTypeEnum.VEDIO.getCode().equals(vehiclePhotoPath.getType())){
                applyAuthVo.setVideo(vehiclePhotoPath.getPhotoUrl()); //视频
            } else if(ApplyAuthFileTypeEnum.FRONT_PHOTO.getCode().equals(vehiclePhotoPath.getType())){
                applyAuthVo.setFrontPhoto(vehiclePhotoPath.getPhotoUrl()); //车辆正面照
            } else if(ApplyAuthFileTypeEnum.EXTERIOR_PHOTO.getCode().equals(vehiclePhotoPath.getType())){
                applyAuthVo.setExteriorPhoto(vehiclePhotoPath.getPhotoUrl()); //车辆外观照
            } else if(ApplyAuthFileTypeEnum.OTHER_PHOTO.getCode().equals(vehiclePhotoPath.getType())){
                otherPhotos.add(vehiclePhotoPath.getPhotoUrl()); //申请相关其他照片
            }
        }
        applyAuthVo.setOtherPhoto(otherPhotos);
        authInfoVo.setApplyAuth(applyAuthVo);
        authInfoVo.setType(1);
        authInfoVoList.add(authInfoVo);
        return authInfoVoList;
    }

    /**
     * 根据用户名获取该用户所属小组id
     *
     * @return
     */
    public String getGroupIdByUserId(String userId){
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
     * 派单管理_获取各个状态数量
     *
     * @param systemUser 用户信息
     * @return
     */
    public ResponseEntity<RestResponse> getTaskManagerCount(SystemUser systemUser){
        TaskManagerCountVo vo = new TaskManagerCountVo();
        //查询未处理数量
        Example example = new Example(VehicleTaskRecovery.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("recoveryCompanyId", systemUser.getRecoveryCompanyId());
        criteria.andEqualTo("assignmentFlag", AssignmentEnums.NO_ASSIGNED.getType()); //未处理
        criteria.andNotEqualTo("status", RecoveryTaskStatusEnums.SELFFINISH.getType()); //不是自己完成
        criteria.andNotEqualTo("status", RecoveryTaskStatusEnums.OTHERFINISH.getType()); //不是他人完成
        criteria.andNotEqualTo("status", RecoveryTaskStatusEnums.CANCEL.getType()); //不是取消
        vo.setUnAssignedCount(recoveryTaskRepository.selectByExampleList(example).size()); //未处理数量
        //查询已处理数量
        Example example1 = new Example(VehicleTaskRecovery.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("recoveryCompanyId", systemUser.getRecoveryCompanyId());
        criteria1.andEqualTo("assignmentFlag", AssignmentEnums.ASSIGNED.getType()); //已处理
        Example.Criteria criteria2 = example1.createCriteria();
        criteria2.andEqualTo("recoveryCompanyId", systemUser.getRecoveryCompanyId());
        List<String> statusList = new ArrayList();
        statusList.add(RecoveryTaskStatusEnums.CANCEL.getType()); //是已取消
        statusList.add(RecoveryTaskStatusEnums.SELFFINISH.getType()); //是自己完成
        statusList.add(RecoveryTaskStatusEnums.OTHERFINISH.getType()); //是他人完成
        criteria2.andIn("status", statusList);
        example1.or(criteria2);
        vo.setAssignedCount(recoveryTaskRepository.selectByExampleList(example1).size()); //已处理数量
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,vo,""),
                HttpStatus.OK);
    }

    /**
     * 根据id获取收车公司派单
     *
     * @param idList 收车任务主键
     * @return
     */
    public List<VehicleTaskRecovery> getRecoveryTaskByIds(List<String> idList){
        if(ArrayUtil.isNotNullAndLengthNotZero(idList)) {
            Example example = new Example(VehicleTaskRecovery.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("id", idList);
            return recoveryTaskRepository.selectByExampleList(example);
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
     * 取消分配任务
     *
     * @param vo 任务分配参数
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> cancelAssginTask(SystemUser systemUser, CancelAssignTaskVo vo){
        if(StringUtil.isNull(vo.getGroupId())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该任务尚未分配小组，不可取消分配"),
                    HttpStatus.OK);
        }
        Date nowDate = new Date();
        //通过工单id获得收车公司任务表id
        VehicleTaskRecovery vehicleTaskRecovery = getRecoveryTaskByTaskId(vo.getTaskId());
        //派单该工单状态有无变更
        if(RecoveryTaskStatusEnums.CANCEL.getType().equals(vehicleTaskRecovery.getStatus())
                || RecoveryTaskStatusEnums.SELFFINISH.getType().equals(vehicleTaskRecovery.getStatus())
                || RecoveryTaskStatusEnums.OTHERFINISH.getType().equals(vehicleTaskRecovery.getStatus())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该任务状态有变更，请刷新后重试"),
                    HttpStatus.OK);
        }
        // 根据任务id和分组id获取小组的任务
        VehicleTaskGroup vehicleTaskGroup = vehicleTaskGroupService.getByGroupIdAndVehicleTaskRecoveryId(vo.getGroupId(),vehicleTaskRecovery.getId());
        if(vehicleTaskGroup == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","小组任务不存在，请刷新后重试"),
                    HttpStatus.OK);
        }
        // 1.根據分組id與收車公司任務id刪除分組任務數據
        vehicleTaskGroupRepository.delete(vehicleTaskGroup);
        //2，更新收车公司任务表，分配flag为未分配
        vehicleTaskRecovery.setAssignmentFlag(AssignmentEnums.NO_ASSIGNED.getType());
        // 更新分配时间
        vehicleTaskRecovery.setAssignDate(null);
        vehicleTaskRecovery.setUpdateTime(nowDate);
        vehicleTaskRecovery.setUpdater(systemUser.getUserId());
        recoveryTaskRepository.updateByPrimaryKey(vehicleTaskRecovery);
        // 3. 登錄分派日誌表
        TaskAssignHistory taskAssignHistory= new TaskAssignHistory();
        //构建任务分配日志表数据
        //组id
        taskAssignHistory.setGroupId(vo.getGroupId());
        //组名称
        taskAssignHistory.setGroupName(vo.getGroupName());
        //收车公司id
        taskAssignHistory.setRecoveryCompanyId(vehicleTaskRecovery.getRecoveryCompanyId());
        //工单id
        taskAssignHistory.setTaskId(Long.parseLong(vo.getTaskId()));
        //操作内容
        taskAssignHistory.setOperateContent(TaskAssignOperateEnums.CANCEL_TASK.getType());
        taskAssignHistory.setCreateTime(nowDate);
        taskAssignHistory.setUpdateTime(nowDate);
        taskAssignHistory.setCreator(systemUser.getUserId());
        taskAssignHistory.setUpdater(systemUser.getUserId());
        taskAssignHisoryRepository.insertOne(taskAssignHistory);
        //任务取消分配，給小組成員推送消息
        assginTaskPush(vo.getGroupId(), vo.getTaskId(), systemUser.getUserId(),"1");
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","取消分配成功"),
                HttpStatus.OK);
    }
}
