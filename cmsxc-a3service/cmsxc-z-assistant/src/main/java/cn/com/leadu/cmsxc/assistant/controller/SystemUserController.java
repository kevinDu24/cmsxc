package cn.com.leadu.cmsxc.assistant.controller;

import cn.com.leadu.cmsxc.assistant.service.SystemUserService;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ModifyPasswordVo;
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
 * Created by yuanzhenxia on 2018/1/15.
 *
 * 系统Controller
 */
@RestController
@RequestMapping("system")
public class SystemUserController {
    private static final Logger logger = LoggerFactory.getLogger(SystemUserController.class);
    @Autowired
    private SystemUserService systemService;

    /**
     * 获取用户角色、头像和用户所属委托公司
     *
     * @param systemUser 用户信息
     * @return
     */
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> userInfo(@AuthUserInfo SystemUser systemUser) {
        try{
            return systemService.userInfo(systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取用户角色和所属公司error",ex);
            throw new CmsServiceException("获取用户角色和所属公司失败");
        }
    }
    /**
     * 退出登陆
     *
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> logout(String userId) {
        try{
            return systemService.logout(userId);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("退出登录error",ex);
            throw new CmsServiceException("退出登录失败");
        }
    }
    /**
     * 上传用户头像
     *
     * @param systemUser 用户信息
     * @param userPhotoUrl 用户头像url
     * @return
     */
    @RequestMapping(value = "/uploadUserPhoto", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> uploadUserPhoto(@AuthUserInfo SystemUser systemUser, String userPhotoUrl) {
        try{
            return systemService.uploadUserPhoto(systemUser, userPhotoUrl);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("上传用户头像error",ex);
            throw new CmsServiceException("上传用户头像失败");
        }
    }

    /**
     * 修改密码
     *
     * @param systemUser 用户信息
     * @param modifyPasswordVo 修改密码信息
     * @return
     */
    @RequestMapping(value = "/modifyPassword", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> modifyPassword(@AuthUserInfo SystemUser systemUser, @RequestBody ModifyPasswordVo modifyPasswordVo){
        try{
            return systemService.modifyPassword(systemUser.getUserId(), modifyPasswordVo.getOldPassword(), modifyPasswordVo.getNewPassword());
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("修改密码error",ex);
            throw new CmsServiceException("修改失败");
        }
    }
}