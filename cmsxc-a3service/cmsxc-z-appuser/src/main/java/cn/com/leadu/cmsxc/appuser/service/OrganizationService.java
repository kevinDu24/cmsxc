package cn.com.leadu.cmsxc.appuser.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.*;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.springframework.http.ResponseEntity;

/**
 * Created by yuanzhenxia on 2018/4/9.
 *
 * 组织架构管理Service
 */
public interface OrganizationService {
    /**
     * 根据内勤人员收车公司id，获取收车公司分组及未分组成员
     *
     * @param systemUser 用户信息
     * @return
     */
    ResponseEntity<RestResponse> getOrganizationList(SystemUser systemUser);
    /**
     * 根据内勤人员收车公司id，获取收车公司未分组成员
     *
     * @param systemUser 用户信息
     * @return
     */
    ResponseEntity<RestResponse> getUserNotInGroup(SystemUser systemUser);
    /**
     * 添加小组
     *
     * @param systemUser 用户信息
     * @param vo 参数信息
     * @return
     */
    ResponseEntity<RestResponse> createRecoveryGroup(SystemUser systemUser, CreateRecoveryGroupVo vo);
    /**
     * 根据内勤人员收车公司id，获取收车公司所有分组
     *
     * @param systemUser 用户信息
     * @return
     */
    ResponseEntity<RestResponse> getAllGroups(SystemUser systemUser);
    /**
     * 加入小组
     *
     * @param systemUser 用户信息
     * @return
     */
    ResponseEntity<RestResponse> joinGroups(SystemUser systemUser, JoinGroupVo vo);
    /**
     * 根据用户id或用户姓名查找业务员
     *
     * @param systemUser 用户信息
     * @param inputValue 用户输入信息
     * @return
     */
    ResponseEntity<RestResponse> searchSalesman(SystemUser systemUser, String inputValue);
    /**
     * 根据分组id获取组员信息
     *
     * @param systemUser 用户信息
     * @param recoveryGroupId 用户id
     * @return
     */
    ResponseEntity<RestResponse> getGroupUsers(SystemUser systemUser, String recoveryGroupId);
    /**
     * 设为组长
     *
     * @param systemUser 用户信息
     * @param groupId 分组id
     * @param userId 用户id
     * @return
     */
    ResponseEntity<RestResponse> setGroupLeader(SystemUser systemUser, String groupId, String userId);

    /**
     * 移出组员
     *
     * @param systemUser 用户信息
     * @param groupId 分组id
     * @param userId 用户id
     * @return
     */
    ResponseEntity<RestResponse> removeGroupUser(SystemUser systemUser, String groupId, String userId);
    /**
     * 编辑小组名称
     *
     * @param systemUser 用户信息
     * @param vo 参数信息
     * @return
     */
    ResponseEntity<RestResponse> editGroupName(SystemUser systemUser, EditGroupNameVo vo);
    /**
     * 解散小组
     *
     * @param systemUser 用户信息
     * @param groupId 分组id
     * @return
     */
    ResponseEntity<RestResponse> deleteRecoveryGroup(SystemUser systemUser, String groupId, String groupName);
    /**
     * 通过组id获取小组成员并且按照客户端类型分类返回
     *
     * @param groupId 分组id
     * @return
     */
    GroupUserListVo getGroupUserClientByGroupId(String groupId);
    /**
     * 通过收车公司id与角色获取该收车公司内勤人员客户端类型
     *
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    GroupUserClientVo getUserClientByRecoveryCompanyId(String recoveryCompanyId);
}
