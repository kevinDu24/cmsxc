package cn.com.leadu.cmsxc.system.controller;

import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.AuthorizationVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import cn.com.leadu.cmsxc.pojo.system.vo.AuthorizationOperateVo;
import cn.com.leadu.cmsxc.system.service.AuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yuanzhenxia on 2018/2/3.
 *
 * 授权操作----授权，驳回，完成等
 */
@RestController
@RequestMapping("authorization")
public class AuthorizationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationController.class);
    @Autowired
    private AuthorizationService authorizationService;

    /**
     * 根据画面设定参数，分页获取授权信息
     *
     * @param authorizationVo 画面参数
     * @return
     */
    @RequestMapping(value = "/findAuthorizationListByPage", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findAuthorizationListByPage(AuthorizationVo authorizationVo, @AuthUserInfo SystemUser sysUser){
        try{
            return authorizationService.findAuthorizationListByPage(authorizationVo, sysUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("分页获取授权信息error",ex);
            throw new CmsServiceException("分页获取授权信息失败");
        }
    }
    /**
     * 授权操作
     *
     * @param  vo 画面参数
     * @return
     */
    @RequestMapping(value = "/authorization", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> authorization(@RequestBody AuthorizationOperateVo vo, @AuthUserInfo SystemUser sysUser){
        try{
            return authorizationService.authorization(vo.getAuthorizationId(), vo.getRemark(), vo.getUserId(),vo.getPlate(),sysUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("授权操作error",ex);
            throw new CmsServiceException("授权操作失败");
        }
    }
    /**
     * 拒绝申请
     *
     * @param  vo 画面参数
     * @return
     */
    @RequestMapping(value = "/refuse", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> refuse(@RequestBody AuthorizationOperateVo vo, @AuthUserInfo SystemUser sysUser){
        try{
            return authorizationService.refuse(vo.getAuthorizationId(), vo.getRemark(),sysUser.getUserId());
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("拒绝申请error",ex);
            throw new CmsServiceException("拒绝申请失败");
        }
    }

    /**
     * 授权延期
     *
     * @param  vo 画面参数
     * @return
     */
    @RequestMapping(value = "/delay", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> delay(@RequestBody AuthorizationOperateVo vo, @AuthUserInfo SystemUser sysUser){
        try{
            return authorizationService.delay(vo.getAuthorizationId(), vo.getDelayDate(), vo.getRemark(), sysUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("授权延期error",ex);
            throw new CmsServiceException("授权延期失败");
        }
    }

    /**
     * 根据photoUuid取得全部的图片
     *
     * @param photoUuid 画面参数
     * @return
     */
    @RequestMapping(value="getPhotoList",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getPhotoList(String photoUuid) {
        try{
            return authorizationService.getPhotoList(photoUuid);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("取得图片信息error",ex);
            throw new CmsServiceException("取得图片信息失败");
        }
    }

}
