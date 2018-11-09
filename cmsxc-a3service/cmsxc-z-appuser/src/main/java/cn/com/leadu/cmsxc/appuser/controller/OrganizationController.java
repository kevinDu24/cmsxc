package cn.com.leadu.cmsxc.appuser.controller;

import cn.com.leadu.cmsxc.appuser.service.OrganizationService;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.*;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yuanzhenxia on 2018/4/9.
 *
 * 组织架构
 */
@RestController
@RequestMapping("organization")
public class OrganizationController {
    private static final Logger logger = LoggerFactory.getLogger(OrganizationController.class);
    @Autowired
    private OrganizationService organizationService;
    /**
     * 根据内勤人员收车公司id，获取组织结构列表
     *
     * @param systemUser
     * @return
     */
    @RequestMapping(value = "/getOrganizationList", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getOrganizationList(@AuthUserInfo SystemUser systemUser) {
        try{
            return organizationService.getOrganizationList(systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取收车公司分组及未分组成员error",ex);
            throw new CmsServiceException("获取收车公司分组及未分组成员失败");
        }
    }
    /**
     * 根据内勤人员收车公司id，获取收车公司未分组成员
     *
     * @param systemUser
     * @return
     */
    @RequestMapping(value = "/getUserNotInGroup", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getUserNotInGroup(@AuthUserInfo SystemUser systemUser) {
        try{
            return organizationService.getUserNotInGroup(systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取收车公司未分组成员error",ex);
            throw new CmsServiceException("获取收车公司未分组成员失败");
        }
    }
    /**
     * 添加小组
     *
     * @param systemUser
     * @return
     */
    @RequestMapping(value = "/createRecoveryGroup", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> createRecoveryGroup(@AuthUserInfo SystemUser systemUser, @RequestBody CreateRecoveryGroupVo vo) {
        try{
            return organizationService.createRecoveryGroup(systemUser, vo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("添加小组error",ex);
            throw new CmsServiceException("添加小组失败");
        }
    }
    /**
     * 根据内勤人员收车公司id，获取收车公司所有分组
     *
     * @param systemUser 用户信息
     * @return
     */
    @RequestMapping(value = "/getAllGroups", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getAllGroups(@AuthUserInfo SystemUser systemUser) {
        try{
            return organizationService.getAllGroups(systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取收车公司所有分组error",ex);
            throw new CmsServiceException("获取收车公司所有分组失败");
        }
    }
    /**
     * 加入小组
     *
     * @param systemUser 用户信息
     * @param vo
     * @return
     */
    @RequestMapping(value = "/joinGroups", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> joinGroups(@AuthUserInfo SystemUser systemUser, @RequestBody JoinGroupVo vo) {
        try{
            return organizationService.joinGroups(systemUser, vo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("加入小组error",ex);
            throw new CmsServiceException("加入小组失败");
        }
    }
    /**
     * 根据用户id或用户姓名查找业务员
     *
     * @param systemUser 用户信息
     * @param inputValue 用户输入值
     * @return
     */
    @RequestMapping(value = "/searchSalesman", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> searchSalesman(@AuthUserInfo SystemUser systemUser, String inputValue) {
        try{
            return organizationService.searchSalesman(systemUser, inputValue);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("根据用户id或用户姓名查找业务员error",ex);
            throw new CmsServiceException("根据用户id或用户姓名查找业务员失败");
        }
    }
    /**
     * 根据分组id获取组员信息
     *
     * @param systemUser 用户信息
     * @param recoveryGroupId 用户id
     * @return
     */
    @RequestMapping(value = "/getGroupUsers", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> getGroupUsers(@AuthUserInfo SystemUser systemUser, String recoveryGroupId) {
        try{
            return organizationService.getGroupUsers(systemUser, recoveryGroupId);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("根据分组id获取组员信息error",ex);
            throw new CmsServiceException("根据分组id获取组员信息失败");
        }
    }
    /**
     * 设为组长
     *
     * @param systemUser 用户信息
     * @param vo 参数
     * @return
     */
    @RequestMapping(value = "/setGroupLeader", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> setGroupLeader(@AuthUserInfo SystemUser systemUser, @RequestBody SetGroupLeaderVo vo) {
        try{
            return organizationService.setGroupLeader(systemUser, vo.getGroupId(), vo.getUserId());
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("设为组长error",ex);
            throw new CmsServiceException("设为组长失败");
        }
    }
    /**
     * 移出组员
     *
     * @param systemUser 用户信息
     * @param vo 参数
     * @return
     */
    @RequestMapping(value = "/removeGroupUser", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> removeGroupUser(@AuthUserInfo SystemUser systemUser, @RequestBody SetGroupLeaderVo vo) {
        try{
            return organizationService.removeGroupUser(systemUser, vo.getGroupId(), vo.getUserId());
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("移出组员error",ex);
            throw new CmsServiceException("移出组员失败");
        }
    }
    /**
     * 编辑小组名称
     *
     * @param systemUser 用户信息
     * @param vo 参数
     * @return
     */
    @RequestMapping(value = "/editGroupName", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> editGroupName(@AuthUserInfo SystemUser systemUser, @RequestBody EditGroupNameVo vo) {
        try{
            return organizationService.editGroupName(systemUser, vo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("编辑小组名称error",ex);
            throw new CmsServiceException("编辑小组名称失败");
        }
    }
    /**
     * 解散小组
     *
     * @param systemUser 用户信息
     * @param vo 参数
     * @return
     */
    @RequestMapping(value = "/deleteRecoveryGroup", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> deleteRecoveryGroup(@AuthUserInfo SystemUser systemUser, @RequestBody DeleteRecoveryGroupVo vo) {
        try{
            return organizationService.deleteRecoveryGroup(systemUser, vo.getGroupId(),vo.getGroupName());
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("解散小组error",ex);
            throw new CmsServiceException("解散小组失败");
        }
    }
}
