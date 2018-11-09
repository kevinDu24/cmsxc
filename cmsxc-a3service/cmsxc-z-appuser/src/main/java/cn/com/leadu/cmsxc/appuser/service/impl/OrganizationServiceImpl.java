package cn.com.leadu.cmsxc.appuser.service.impl;

import cn.com.leadu.cmsxc.appbusiness.service.RecoveryCompanyService;
import cn.com.leadu.cmsxc.appbusiness.service.RecoveryGroupService;
import cn.com.leadu.cmsxc.appuser.service.OrganizationService;
import cn.com.leadu.cmsxc.appuser.service.RecoveryTaskService;
import cn.com.leadu.cmsxc.appuser.service.SystemUserService;
import cn.com.leadu.cmsxc.appuser.service.VehicleTaskGroupService;
import cn.com.leadu.cmsxc.appuser.util.constant.enums.TaskAssignOperateEnums;
import cn.com.leadu.cmsxc.common.constant.enums.AuthorizationStatusEnums;
import cn.com.leadu.cmsxc.common.constant.enums.ClientTypeEnums;
import cn.com.leadu.cmsxc.common.util.ArrayUtil;
import cn.com.leadu.cmsxc.common.util.CommonUtil;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.appbusiness.repository.*;
import cn.com.leadu.cmsxc.data.appuser.repository.VehicleAuthorizationRepository;
import cn.com.leadu.cmsxc.data.system.repository.SystemUserRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.*;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.*;
import cn.com.leadu.cmsxc.pojo.appuser.entity.VehicleAuthorization;
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
 * Created by yuanzhenxia on 2018/4/10.
 *
 * 组织架构
 */
@Service
public class OrganizationServiceImpl implements OrganizationService{
    private static final Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);
    @Autowired
    private RecoveryGroupRepository recoveryGroupRepository;
    @Autowired
    private RecoveryGroupUserRepository recoveryGroupUserRepository;
    @Autowired
    private RecoveryCompanyService recoveryCompanyService;
    @Autowired
    private RecoveryGroupService recoveryGroupService;
    @Autowired
    private SystemUserRepository systemUserRepository;
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private VehicleTaskGroupService vehicleTaskGroupService;
    @Autowired
    private RecoveryTaskService recoveryTaskService;
    @Autowired
    private VehicleTaskGroupRepository vehicleTaskGroupRepository;
    @Autowired
    private TaskAssignHistoryRepository taskAssignHistoryRepository;
    @Autowired
    private VehicleTaskRepository vehicleTaskRepository;
    @Autowired
    private VehicleAuthorizationRepository vehicleAuthorizationRepository;

    /**
     * 根据内勤人员收车公司id，获取收车公司分组及未分组成员
     *
     * @param systemUser 用户信息
     * @return
     */
    public ResponseEntity<RestResponse> getOrganizationList(SystemUser systemUser){
        OrganizationListVo organizationListVo = new OrganizationListVo();
        // 根据收车公司id，获取收车公司全称
        RecoveryCompany recoveryCompany = recoveryCompanyService.selectRecoveryCompanyById(systemUser.getRecoveryCompanyId());
        if(recoveryCompany == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","收车公司不存在，组织架构有误！"),
                    HttpStatus.OK);
        }
        organizationListVo.setRecoveryCompanyName(recoveryCompany.getRecoveryFullName());
        // 根据收车公司id取出所有小组及组员数量
        List<GroupListVo> groupList = recoveryGroupRepository.selectRecoveryGroupListByRecoveryCompanyId(systemUser.getRecoveryCompanyId());
        // 根据收车公司id和角色取出所有未分组成员信息
        List<UserListVo> userList = recoveryGroupRepository.selectUserByRecoveryCompanyIdAndUserRole(systemUser.getRecoveryCompanyId());
        organizationListVo.setGroupListVos(groupList);
        organizationListVo.setUserListVos(userList);
        return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,organizationListVo,""),
                    HttpStatus.OK);
    }

    /**
     * 根据内勤人员收车公司id，获取收车公司未分组成员
     *
     * @param systemUser 用户信息
     * @return
     */
    public ResponseEntity<RestResponse> getUserNotInGroup(SystemUser systemUser){
        // 根据收车公司id和角色取出所有未分组成员信息
        List<UserListVo> userList = recoveryGroupRepository.selectUserByRecoveryCompanyIdAndUserRole(systemUser.getRecoveryCompanyId());
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,userList,""),
                HttpStatus.OK);
    }
    /**
     * 添加小组
     *
     * @param systemUser 用户信息
     * @param vo 参数信息
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> createRecoveryGroup(SystemUser systemUser, CreateRecoveryGroupVo vo){
        // 去除组名中的表情符号
        vo.setGroupName(CommonUtil.filterEmoji(vo.getGroupName()));
        // 根据分组名称和收车公司id获取分组信息
        RecoveryGroup recoveryGroup = recoveryGroupService.selectByGroupNameAndRecoveryCompanyId(vo.getGroupName(),systemUser.getRecoveryCompanyId());
        if(recoveryGroup != null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","分组已存在，请修改分组名！"),
                    HttpStatus.OK);
        }
        Date nowDate = new Date();
        // 1，登录分组表
        recoveryGroup = new RecoveryGroup();
        // 分组名称
        recoveryGroup.setGroupName(vo.getGroupName());
        // 组长id
        recoveryGroup.setGroupLeaderId(vo.getGroupLeaderId());
        // 收车公司id
        recoveryGroup.setRecoveryCompanyId(systemUser.getRecoveryCompanyId());
        recoveryGroup.setCreateTime(nowDate);
        recoveryGroup.setCreator(systemUser.getUserId());
        recoveryGroup.setUpdater(systemUser.getUserId());
        recoveryGroup.setUpdateTime(nowDate);
        recoveryGroupRepository.insertOne(recoveryGroup);
        // 2，登录组员表
        RecoveryGroupUser recoveryGroupUser = new RecoveryGroupUser();
        // 组员id
        recoveryGroupUser.setGroupUserId(recoveryGroup.getGroupLeaderId());
        // 收车公司id
        recoveryGroupUser.setRecoveryGroupId(recoveryGroup.getId());
        recoveryGroupUser.setUpdateTime(nowDate);
        recoveryGroupUser.setUpdater(systemUser.getUserId());
        recoveryGroupUser.setCreator(systemUser.getUserId());
        recoveryGroupUser.setCreateTime(nowDate);
        recoveryGroupUserRepository.insertOne(recoveryGroupUser);
        // 3，与此用户相关的授权小组id和完成小组id修改为最新的小组id
        // 创建小组时，组长获得授权,申请完成，委托方取消的单子的授权小组id和完成小组id设置为当前新建小组id
        List<String> authStatusList = new ArrayList<>();
        List<String> finishTaskIds = new ArrayList<>();
        List<String> authTaskIds = new ArrayList<>();
        authStatusList.add(AuthorizationStatusEnums.AUTHORIZETED.getType());
        authStatusList.add(AuthorizationStatusEnums.LEASECANCEL.getType());
        authStatusList.add(AuthorizationStatusEnums.FINISH.getType());
        List<VehicleAuthorization> vehicleAuthorizations = selectByTaskIdAndAuthorizationStatus(vo.getGroupLeaderId(),authStatusList);
        if(ArrayUtil.isNotNullAndLengthNotZero(vehicleAuthorizations)){
            for(VehicleAuthorization vehicleAuthorization : vehicleAuthorizations){
                if(AuthorizationStatusEnums.FINISH.getType().equals(vehicleAuthorization.getAuthorizationStatus())){
                    // 此用户授权完成的任务id
                    finishTaskIds.add(String.valueOf(vehicleAuthorization.getTaskId()));
                }else{
                    // 此用户获得授权，授权失效，委托方取消的任务id
                    authTaskIds.add(String.valueOf(vehicleAuthorization.getTaskId()));
                }
            }
        }
        // 更新完成小组id为新小组id
        if(ArrayUtil.isNotNullAndLengthNotZero(finishTaskIds)){
            vehicleTaskRepository.updateFinishGroupId(finishTaskIds,recoveryGroup.getId());
        }
        // 更新授权小组id为新小组id
        if(ArrayUtil.isNotNullAndLengthNotZero(authTaskIds)){
            vehicleTaskRepository.updateAuthorizationGroupId(authTaskIds, recoveryGroup.getId());
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","添加小组成功！"),
                HttpStatus.OK);
    }
    /**
     * 根据内勤人员收车公司id，获取收车公司所有分组
     *
     * @param systemUser 用户信息
     * @return
     */
    public ResponseEntity<RestResponse> getAllGroups(SystemUser systemUser){
        // 根据收车公司id获取收车公司所有分组
        List<RecoveryGroupVo> recoveryGroupVos = recoveryGroupService.selectByRecoveryCompanyId(systemUser.getRecoveryCompanyId());
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,recoveryGroupVos,""),
                HttpStatus.OK);
    }
    /**
     * 加入小组
     *
     * @param systemUser 用户信息
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> joinGroups(SystemUser systemUser, JoinGroupVo vo){
        // 去除用户名中的表情符号
        vo.setUserName(CommonUtil.filterEmoji(vo.getUserName()));
        // 1,判断用户姓名是否已经存在,如果不存在，更新用户姓名
       SystemUser sysUser = systemUserService.selectSystemUserByUserId(vo.getUserId());
        Date nowDate = new Date();
        if(StringUtil.isNotNull(vo.getUserName()) && !vo.getUserName().equals(sysUser.getUserName())){
            sysUser.setUserName(vo.getUserName());
            sysUser.setUpdater(systemUser.getUserId());
            sysUser.setUpdateTime(nowDate);
            systemUserRepository.updateByPrimaryKeySelective(sysUser);
        }
        Example example = new Example(RecoveryGroupUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupUserId", vo.getUserId());
        // 根据用户id从组员表中取分组信息，判断成员是否已经加入小组
        RecoveryGroupUser groupUser = recoveryGroupUserRepository.selectOneByExample(example);
        if(groupUser != null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该用户已加入小组，不可重复操作！"),
                    HttpStatus.OK);
        }
        // 2, 登录到组员表
        RecoveryGroupUser recoveryGroupUser = new RecoveryGroupUser();
        // 组员id
        recoveryGroupUser.setGroupUserId(vo.getUserId());
        // 分组id
        recoveryGroupUser.setRecoveryGroupId(vo.getGroupId());
        recoveryGroupUser.setUpdateTime(nowDate);
        recoveryGroupUser.setUpdater(systemUser.getUserId());
        recoveryGroupUser.setCreator(systemUser.getUserId());
        recoveryGroupUser.setCreateTime(nowDate);
        recoveryGroupUserRepository.insertOne(recoveryGroupUser);
        // 3，与此用户相关的授权小组id和完成小组id修改为最新的小组id
        // 创建小组时，组长获得授权，申请完成，委托方取消的单子的授权小组id和完成小组id设置为当前新建小组id
        List<String> authStatusList = new ArrayList<>();
        List<String> finishTaskIds = new ArrayList<>();
        List<String> authTaskIds = new ArrayList<>();
        authStatusList.add(AuthorizationStatusEnums.AUTHORIZETED.getType());
        authStatusList.add(AuthorizationStatusEnums.LEASECANCEL.getType());
        authStatusList.add(AuthorizationStatusEnums.FINISH.getType());
        List<VehicleAuthorization> vehicleAuthorizations = selectByTaskIdAndAuthorizationStatus(vo.getUserId(),authStatusList);
        if(ArrayUtil.isNotNullAndLengthNotZero(vehicleAuthorizations)){
            for(VehicleAuthorization vehicleAuthorization : vehicleAuthorizations){
                if(AuthorizationStatusEnums.FINISH.getType().equals(vehicleAuthorization.getAuthorizationStatus())){
                    // 此用户授权完成的任务id
                    finishTaskIds.add(String.valueOf(vehicleAuthorization.getTaskId()));
                }else{
                    // 此用户获得授权，授权失效，委托方取消的任务id
                    authTaskIds.add(String.valueOf(vehicleAuthorization.getTaskId()));
                }
            }
        }
        // 更新完成小组id为新小组id
        if(ArrayUtil.isNotNullAndLengthNotZero(finishTaskIds)){
            vehicleTaskRepository.updateFinishGroupId(finishTaskIds,vo.getGroupId());
        }
        // 更新授权小组id为新小组id
        if(ArrayUtil.isNotNullAndLengthNotZero(authTaskIds)){
            vehicleTaskRepository.updateAuthorizationGroupId(authTaskIds, vo.getGroupId());
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","加入小组成功！"),
                HttpStatus.OK);
    }

    /**
     * 根据用户id或用户姓名查找业务员
     *
     * @param systemUser 用户信息
     * @param inputValue 用户输入信息
     * @return
     */
    public ResponseEntity<RestResponse> searchSalesman(SystemUser systemUser, String inputValue){
        String tempString = "";
        // 拼接like的查询条件
        if(StringUtil.isNotNull(inputValue)){
            tempString = CommonUtil.likePartten(CommonUtil.filterEmoji(inputValue));
        }
        // 根据用户id或用户姓名查找业务员
        List<SearchSalesmanVo> userList = recoveryGroupRepository.selectSalesmanByUserIdOrUserName(systemUser.getRecoveryCompanyId(),tempString);
        if(ArrayUtil.isNotNullAndLengthNotZero(userList)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,userList,""),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未查询到结果"),
                HttpStatus.OK);
    }

    /**
     * 根据分组id获取组员信息
     *
     * @param systemUser 用户信息
     * @param recoveryGroupId 分组id
     * @return
     */
    public ResponseEntity<RestResponse> getGroupUsers(SystemUser systemUser, String recoveryGroupId){
        // 根据用户id或用户姓名查找业务员
        List<SearchSalesmanVo> userList = recoveryGroupRepository.selectGroupUserByGroupId(recoveryGroupId);
        if(ArrayUtil.isNotNullAndLengthNotZero(userList)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,userList,""),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未查询到结果"),
                HttpStatus.OK);
    }
    /**
     * 设为组长
     *
     * @param systemUser 用户信息
     * @param groupId 分组id
     * @param userId 用户id
     * @return
     */
    public ResponseEntity<RestResponse> setGroupLeader(SystemUser systemUser, String groupId, String userId){
        RecoveryGroup recoveryGroup = recoveryGroupService.selectByRecoveryGroupId(groupId);
        if(recoveryGroup == null ){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","分组不存在"),
                    HttpStatus.OK);
        }
        // 组长id
        recoveryGroup.setGroupLeaderId(userId);
        recoveryGroup.setUpdater(systemUser.getUserId());
        recoveryGroup.setUpdateTime(new Date());
        // 更新分组表
        recoveryGroupRepository.updateByPrimaryKey(recoveryGroup);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","设为组长成功！"),
                HttpStatus.OK);
    }

    /**
     * 移出组员
     *
     * @param systemUser 用户信息
     * @param groupId 分组id
     * @param userId 用户id
     * @return
     */
    public ResponseEntity<RestResponse> removeGroupUser(SystemUser systemUser, String groupId, String userId){
        RecoveryGroup recoveryGroup = recoveryGroupService.selectByRecoveryGroupId(groupId);
        if(recoveryGroup == null ){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","分组不存在"),
                    HttpStatus.OK);
        }
        if(userId.equals(recoveryGroup.getGroupLeaderId())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","组长不可移出分组！"),
                    HttpStatus.OK);
        }
        recoveryGroupUserRepository.deleteByGroupIdAndUserId(groupId, userId);
        // 2，与此用户相关的授权小组id和完成小组id修改空
        // 创建小组时，组长获得授权，申请完成，委托方取消的单子的授权小组id和完成小组id设置为当前新建小组id
        List<String> authStatusList = new ArrayList<>();
        List<String> finishTaskIds = new ArrayList<>();
        List<String> authoTaskIds = new ArrayList<>();
        authStatusList.add(AuthorizationStatusEnums.AUTHORIZETED.getType());
        authStatusList.add(AuthorizationStatusEnums.LEASECANCEL.getType());
        authStatusList.add(AuthorizationStatusEnums.FINISH.getType());
        List<VehicleAuthorization> vehicleAuthorizations = selectByTaskIdAndAuthorizationStatus(userId,authStatusList);
        if(ArrayUtil.isNotNullAndLengthNotZero(vehicleAuthorizations)){
            for(VehicleAuthorization vehicleAuthorization : vehicleAuthorizations){
                if(AuthorizationStatusEnums.FINISH.getType().equals(vehicleAuthorization.getAuthorizationStatus())){
                    // 此用户授权完成的任务id
                    finishTaskIds.add(String.valueOf(vehicleAuthorization.getTaskId()));
                }else{
                    // 此用户获得授权，授权失效，委托方取消的任务id
                    authoTaskIds.add(String.valueOf(vehicleAuthorization.getTaskId()));
                }
            }
        }
        // 更新完成小组id为新小组id
        if(ArrayUtil.isNotNullAndLengthNotZero(finishTaskIds)){
            vehicleTaskRepository.updateFinishGroupId(finishTaskIds,null);
        }
        // 更新授权小组id为新小组id
        if(ArrayUtil.isNotNullAndLengthNotZero(authoTaskIds)){
            vehicleTaskRepository.updateAuthorizationGroupId(authoTaskIds, null);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","移出组员成功！"),
                HttpStatus.OK);
    }
    /**
     * 编辑小组名称
     *
     * @param systemUser 用户信息
     * @param vo 参数信息
     * @return
     */
    public ResponseEntity<RestResponse> editGroupName(SystemUser systemUser, EditGroupNameVo vo){
        // 去除组名中的表情符号
        vo.setGroupName(CommonUtil.filterEmoji(vo.getGroupName()));
        RecoveryGroup recoveryGroup = recoveryGroupService.selectByRecoveryGroupId(vo.getGroupId());
        if(recoveryGroup == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","小组不存在！"),
                    HttpStatus.OK);
        }
        if(recoveryGroup.getGroupName().equals(vo.getGroupName())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","组名未改变！"),
                    HttpStatus.OK);
        }
        // 根据分组名称和收车公司id获取分组信息
        RecoveryGroup group = recoveryGroupService.selectByGroupNameAndRecoveryCompanyId(vo.getGroupName(),systemUser.getRecoveryCompanyId());
        if(group != null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","小组名已存在！"),
                    HttpStatus.OK);
        }
        recoveryGroup.setGroupName(vo.getGroupName());
        recoveryGroup.setUpdateTime(new Date());
        recoveryGroup.setUpdater(systemUser.getUserId());
        recoveryGroupRepository.updateByPrimaryKey(recoveryGroup);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","组名修改成功！"),
                HttpStatus.OK);
    }
    /**
     * 解散小组
     *
     * @param systemUser 用户信息
     * @param groupId 分组id
     * @param groupName 分组名称
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> deleteRecoveryGroup(SystemUser systemUser, String groupId, String groupName){
        RecoveryGroup recoveryGroup = recoveryGroupService.selectByRecoveryGroupId(groupId);
        if(recoveryGroup == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","分组不存在！"),
                    HttpStatus.OK);
        }
        Date nowDate = new Date();
        List<String>  vehicleTaskRecoveryIdList = new ArrayList<>();
        List<TaskAssignHistory> taskAssignHistoryList = new ArrayList<>();
        // 1，根据分组id获取所有小组任务
        List<VehicleTaskGroup> vehicleTaskGroupList = vehicleTaskGroupService.getVehicleTaskGroupByGroupId(groupId);
        if(ArrayUtil.isNotNullAndLengthNotZero(vehicleTaskGroupList)){
            for(VehicleTaskGroup vehicleTaskGroup : vehicleTaskGroupList){
                // 获取此收车任务是否分给了其他小组
//            VehicleTaskGroup taskGroup = vehicleTaskGroupService.getByGroupIdAndVehicleTaskRecoveryId(vehicleTaskGroup.getVehicleTaskRecoveryId(), vehicleTaskGroup.getGroupId());
                vehicleTaskRecoveryIdList.add(vehicleTaskGroup.getVehicleTaskRecoveryId());
            }
        }
        // 根据id获取收车任务
        List<VehicleTaskRecovery> vehicleTaskRecoverys = recoveryTaskService.getRecoveryTaskByIds(vehicleTaskRecoveryIdList);
        if(ArrayUtil.isNotNullAndLengthNotZero(vehicleTaskRecoverys)){
            for(VehicleTaskRecovery vehicleTaskRecovery : vehicleTaskRecoverys){
                // 3, 登录分组日志表
                TaskAssignHistory taskAssignHistory = new TaskAssignHistory();
                // 组名称
                taskAssignHistory.setGroupName(groupName);
                // 分组id
                taskAssignHistory.setGroupId(groupId);
                // 操作内容
                taskAssignHistory.setOperateContent(TaskAssignOperateEnums.RESTORE_TASK.getType());
                // 收车公司id
                taskAssignHistory.setRecoveryCompanyId(vehicleTaskRecovery.getRecoveryCompanyId());
                // 工单id
                taskAssignHistory.setTaskId(vehicleTaskRecovery.getTaskId());
                taskAssignHistory.setUpdateTime(nowDate);
                taskAssignHistory.setUpdater(systemUser.getUserId());
                taskAssignHistory.setCreateTime(nowDate);
                taskAssignHistory.setCreator(systemUser.getUserId());
                taskAssignHistoryList.add(taskAssignHistory);
            }
        }
        // 1,更新收车任务表的任务分配flag为 0 ：未分配
        if(ArrayUtil.isNotNullAndLengthNotZero(vehicleTaskRecoveryIdList)){
            vehicleTaskRepository.updateAssignmentFlag(vehicleTaskRecoveryIdList);
        }
        // 2.删除任务分组表数据
        vehicleTaskGroupRepository.deleteByGroupId(groupId);
        // 3, 登录分组日志表
        taskAssignHistoryRepository.insertMore(taskAssignHistoryList);
        // 4，根据分组id删除分组
        recoveryGroupRepository.deleteByRecoveryGroupId(groupId);
        // 5，根据分组id删除所有组员
        recoveryGroupUserRepository.deleteByGroupId(groupId);
        // 6,更新授权小组id和完成小组id为空
        vehicleTaskRepository.updateAuthGroupIdNull(groupId);
        vehicleTaskRepository.updateFinishGroupIdNull(groupId);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","小组解散成功！"),
                HttpStatus.OK);

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
     * 根据授权ID获取授权信息
     *
     * @param userId 用户id
     * @param authorizationStatusList 授权状态
     * @return
     */
    public List<VehicleAuthorization> selectByTaskIdAndAuthorizationStatus(String userId, List<String> authorizationStatusList){
        if(StringUtil.isNotNull(userId) && authorizationStatusList != null && !authorizationStatusList.isEmpty()) {
            Example example = new Example(VehicleAuthorization.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId", userId);
            criteria.andIn("authorizationStatus", authorizationStatusList);
            List<VehicleAuthorization> ehicleAuthorizationvList = vehicleAuthorizationRepository.selectByExampleList(example);
            if (ArrayUtil.isNotNullAndLengthNotZero(ehicleAuthorizationvList))
                return ehicleAuthorizationvList;
        }
        return null;
    }



}
