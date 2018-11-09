package cn.com.leadu.cmsxc.appuser.controller;

import cn.com.leadu.cmsxc.appuser.validator.sysuser.vo.ModifyPasswordVo;
import cn.com.leadu.cmsxc.common.constant.Constants;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.appuser.validator.sysuser.vo.RegisterVo;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appuser.vo.FeedbackVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cn.com.leadu.cmsxc.appuser.service.MessageService;
import cn.com.leadu.cmsxc.appuser.service.SystemUserService;

import javax.validation.Valid;

/**
 * Created by yuanzhenxia on 2018/1/15.
 *
 * 系统登录,注册，获取验证码，修改密码,找回密码
 */
@RestController
@RequestMapping("system")
public class SystemUserController {
    private static final Logger logger = LoggerFactory.getLogger(SystemUserController.class);
    @Autowired
    private SystemUserService systemService;
    @Autowired
    private MessageService messageService;

    /**
     * 注册获取短信验证码，验证码3分钟有效
     *
     * @param userId:需要获取短信验证码的手机号
     * @return
     */
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> sendCode(String userId) {
        try{
            return messageService.sendSingleMt(userId, "赏金寻车", "赏金寻车注册获取短信验证码", Constants.SENDCODE_SCAN);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取短信验证码error",ex);
            throw new CmsServiceException("获取验证码失败");
        }
    }
    /**
     * 找回密码获取短信验证码，验证码3分钟有效
     *
     * @param userPhone:需要获取短信验证码的手机号
     * @return
     */
    @RequestMapping(value = "/sendCodeFindPassword", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> sendCodeFindPassword(String userPhone) {
        try{
            return messageService.sendMt(userPhone, "赏金寻车", "赏金寻车找回密码获取短信验证码", Constants.SENDCODEFINDPASSWORD_SCAN);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取短信验证码error",ex);
            throw new CmsServiceException("获取验证码失败");
        }
    }
    /**
     * 用户注册
     *
     * @param registerVo 注册信息
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> register(@Valid @RequestBody RegisterVo registerVo) {
        try{
            return systemService.register(registerVo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("用户注册error",ex);
            throw new CmsServiceException("用户注册失败");
        }
    }
    /**
     * 根据用户名,密码登录
     *
     * @param userId 用户id
     * @param userPassword 密码
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> login( String userId,String userPassword){
        try{
            return systemService.login(userId,userPassword);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("根据用户名密码登录error",ex);
            throw new CmsServiceException("用户登录失败");
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
    /**
     * 找回密码
     *
     * @param modifyPasswordVo 找回密码信息
     * @param systemUser 用户信息
     * @return
     */
    @RequestMapping(value = "/findBackPassword", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> findBackPassword(@AuthUserInfo SystemUser systemUser, @RequestBody ModifyPasswordVo modifyPasswordVo){
        try{
            return systemService.findBackPassword(modifyPasswordVo.getUserPhone(), modifyPasswordVo.getVerficationCode(), modifyPasswordVo.getNewPassword());
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("找回密码error",ex);
            throw new CmsServiceException("找回失败");
        }
    }

    /**
     * 根据用户id（手机号）获取用户信息
     * @param userId 用户id
     * @return
     */
    @RequestMapping(value = "/findSystemUserByUserId", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findSystemUserByUserId(String userId) {
        try{
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, systemService.selectSystemUserByUserId(userId),""),
                    HttpStatus.OK);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取用户信息error",ex);
            throw new CmsServiceException("获取用户信息失败");
        }
    }
    /**
     * 用户意见反馈
     *
     * @param systemUser 用户信息
     * @param feedbackVo 意见信息
     * @return
     */
    @RequestMapping(value = "/editUserFeedback", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> editUserFeedback(@AuthUserInfo SystemUser systemUser, @RequestBody FeedbackVo feedbackVo) {
        try{
            return systemService.editUserFeedback(systemUser.getUserId(),feedbackVo.getFeedBackContent(), feedbackVo.getConnectWay());
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("用户意见反馈error",ex);
            throw new CmsServiceException("用户意见反馈失败");
        }
    }
    /**
     * 获取用户角色和所属公司
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
     * app启动时判断是否显示敏感信息（app审核用）
     *
     * @return
     */
    @RequestMapping(value = "/appCheck", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> appCheck() {
        try{
            return systemService.appCheck();
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("app启动时判断是否显示敏感信息error",ex);
            throw new CmsServiceException("app启动时判断是否显示敏感信息失败");
        }
    }
    /**
     * 单点登录判断
     *
     * @return
     */
    @RequestMapping(value = "/singleLoginCheck", method = RequestMethod.GET)
    public boolean singleLoginCheck(String userId) {
        try{
            return systemService.singleLoginCheck(userId);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("单点登录验证error",ex);
            throw new CmsServiceException("a单点登录验证失败");
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
     * 覆盖更新时，若是登录状态更新最新的deviceToken
     *
     * @param userId 用户名
     * @return
     */
    @RequestMapping(value = "/pushNewestDeviceToken", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> pushNewestDeviceToken(String userId) {
        try{
            return systemService.pushNewestDeviceToken(userId);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("盖更新时，若是登录状态更新最新的deviceTokenerror",ex);
            throw new CmsServiceException("盖更新时，若是登录状态更新最新的deviceToken失败");
        }
    }
}