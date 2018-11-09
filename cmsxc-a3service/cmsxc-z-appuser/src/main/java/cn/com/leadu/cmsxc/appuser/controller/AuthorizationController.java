package cn.com.leadu.cmsxc.appuser.controller;

import cn.com.leadu.cmsxc.appuser.service.AuthorizationService;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 授权Controller
 */
@RestController
@RequestMapping("authorization")
public class AuthorizationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationController.class);
    @Autowired
    private AuthorizationService authorizationService;

    /**
     * 申请授权前，获取此用户针对此车辆之前的最新申请信息
     *
     * @param systemUser  用户信息
     * @param plate 车牌号
     * @return
     */
    @RequestMapping(value = "/beforeApplyAuthorization", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> beforeApplyAuthorization(@AuthUserInfo SystemUser systemUser, @RequestParam String plate) {
        try{
            return authorizationService.beforeApplyAuthorization(systemUser.getUserId(),plate);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取此用户针对此车辆之前的最新申请信息error",ex);
            throw new CmsServiceException("获取此用户针对此车辆之前的最新申请信息失败");
        }
    }

    /**
     * 申请授权
     *
     * @param systemUser  用户信息
     * @param paramterMap 参数集合（车牌号，申请人姓名，申请人手机号，申请人证件号码，备注）
     * @return
     */
    @RequestMapping(value = "/applyAuthorization", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> applyAuthorization(@AuthUserInfo SystemUser systemUser, @RequestParam Map<String,String> paramterMap) {
        try{
            return authorizationService.applyAuthorization(systemUser.getUserId(),paramterMap);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("申请授权error",ex);
            throw new CmsServiceException("申请授权失败");
        }
    }
    /**
     * 文件上传
     *
     * @param file 文件
     * @return
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> uploadFile(MultipartFile file) {
        try{
            return authorizationService.uploadFile(file);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("文件上传error",ex);
            throw new CmsServiceException("文件上传失败");
        }
    }
    /**
     * 分页查询指定用户指定授权状态的授权列表
     *
     * @param systemUser 用户信息
     * @param authorizationStatus 授权状态
     * @param page 当前页
     * @param size 每页个数
     * @return
     */
    @RequestMapping(value = "/getApplyAuthorizationList", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getApplyAuthorizationList(@AuthUserInfo SystemUser systemUser, String authorizationStatus,
                                                                  @RequestParam(required = true) int page, @RequestParam(required = true) int size){
        try{
            return authorizationService.getApplyAuthorizationList(systemUser.getUserId(), authorizationStatus, page, size);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取授权列表error",ex);
            throw new CmsServiceException("获取授权列表失败");
        }
    }
    /**
     * 申请资料预览
     *
     * @param authorizationId 授权id
     * @return
     */
    @RequestMapping(value = "/viewMaterial", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> viewMaterial( String authorizationId){
        try{
            return authorizationService.viewMaterial(authorizationId);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取申请资料预览error",ex);
            throw new CmsServiceException("获取申请资料预览失败");
        }
    }
    /**
     * 取消申请
     *
     * @param authorizationId  授权id
     * @param systemUser 用户信息
     * @return
     */
    @RequestMapping(value = "/cancelApplyAuthorization", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> cancelApplyAuthorization(@AuthUserInfo SystemUser systemUser, String authorizationId){
        try{
            return authorizationService.cancelApplyAuthorization(systemUser.getUserId(), authorizationId);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("取消申请error",ex);
            throw new CmsServiceException("取消申请失败");
        }
    }

    /**
     * 编辑资料
     *
     * @param systemUser 用户信息
     * @param paramterMap 参数集合（授权id，车牌号，申请人姓名，申请人电话，申请人证件号，备注）
     * @param photoMsg1 图片1
     * @param photoMsg2 图片2
     * @param photoMsg3 图片3
     * @param photoMsg4 图片4
     * @param photoMsg5 图片5
     * @return
     */
    @RequestMapping(value = "/editAuthorization", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> editAuthorization(@AuthUserInfo SystemUser systemUser, @RequestParam Map<String,String> paramterMap, MultipartFile photoMsg1, MultipartFile photoMsg2,
                                                           MultipartFile photoMsg3, MultipartFile photoMsg4, MultipartFile photoMsg5) {
        try{
            return authorizationService.editAuthorization(systemUser.getUserId(),paramterMap,photoMsg1,photoMsg2,photoMsg3, photoMsg4,photoMsg5);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("编辑资料error",ex);
            throw new CmsServiceException("编辑资料失败");
        }
    }
    /**
     * 文件下载(查看)
     *
     * @param response 网页请求
     * @param fileName 文件名
     * @param loadDate 上传时间
     * @return
     */
    @RequestMapping(value="/download/{type}/{loadDate}/{fileName}", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response, @PathVariable("type") String type,@PathVariable("fileName") String fileName, @PathVariable("loadDate") String loadDate) {
        try{
            authorizationService.downloadFile(response, fileName, loadDate);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("文件下载error",ex);
            throw new CmsServiceException("你访问的文件不存在");
        }
    }


}
