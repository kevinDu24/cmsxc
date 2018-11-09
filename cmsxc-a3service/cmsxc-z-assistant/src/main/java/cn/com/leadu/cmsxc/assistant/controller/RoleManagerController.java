package cn.com.leadu.cmsxc.assistant.controller;

import cn.com.leadu.cmsxc.assistant.service.RoleManagerService;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.assistant.vo.EditAuditorVo;
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
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 角色管理Controller
 */
@RestController
@RequestMapping("roleManager")
public class RoleManagerController {
    private static final Logger logger = LoggerFactory.getLogger(RoleManagerController.class);
    @Autowired
    private RoleManagerService roleManagerService;

    /**
     * 返回角色列表
     * @param systemUser 用户信息
     * @return
     */
    @RequestMapping(value = "/getUserList", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getUserList(@AuthUserInfo SystemUser systemUser) {
        try{
            return roleManagerService.getUserList(systemUser.getLeaseId());
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("返回角色列表error",ex);
            throw new CmsServiceException("返回角色列表失败");
        }
    }

    /**
     * 返回角色省份集合
     * @param id 用户表id
     * @return
     */
    @RequestMapping(value = "/getProvinces", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getProvinces(String id) {
        try{
            return roleManagerService.getProvinces(id);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("返回角色省份集合error",ex);
            throw new CmsServiceException("返回角色省份集合失败");
        }
    }

    /**
     * 新增审核人员
     * @param vo 审核人员信息参数
     * @return
     */
    @RequestMapping(value = "/addAuditor", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> addAuditor(@AuthUserInfo SystemUser systemUser, @RequestBody EditAuditorVo vo) {
        try{
            return roleManagerService.addAuditor(vo,systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("新增审核人员error",ex);
            throw new CmsServiceException("新增审核人员失败");
        }
    }

    /**
     * 编辑审核人员
     * @param vo 审核人员信息参数
     * @return
     */
    @RequestMapping(value = "/editAuditor", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> editAuditor(@AuthUserInfo SystemUser systemUser, @RequestBody EditAuditorVo vo) {
        try{
            return roleManagerService.editAuditor(vo,systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("编辑审核人员error",ex);
            throw new CmsServiceException("编辑审核人员失败");
        }
    }
    /**
     * 禁用审核人员
     *
     * @param disableUserId 被禁用审核人员账号
     * @return
     */
    @RequestMapping(value = "/disableAuditor", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> disableAuditor(@AuthUserInfo SystemUser systemUser,String disableUserId) {
        try{
            return roleManagerService.disableAuditor(systemUser,disableUserId);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("禁用审核人员error",ex);
            throw new CmsServiceException("禁用审核人员失败");
        }
    }
}
