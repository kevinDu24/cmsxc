package cn.com.leadu.cmsxc.system.controller;

import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.system.service.RecoveryUserService;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryCompanyVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryUserListVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yuanzhenxia on 2018/2/1.
 *
 * 收车公司用户管理
 */
@RestController
@RequestMapping("recovery_user")
public class RecoveryUserController {
    private static final Logger logger = LoggerFactory.getLogger(RecoveryUserController.class);
    @Autowired
    private RecoveryUserService recoveryUserService;

    /**
     * 根据收车公司简称或全称，分页获取收车公司信息
     *
     * @param recoveryUserListVo 收车公司全称
     * @return
     */
    @RequestMapping(value = "/findRecoveryUserByPage", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findRecoveryUserByPage(RecoveryUserListVo recoveryUserListVo){
        try{
            return recoveryUserService.findRecoveryUserByPage(recoveryUserListVo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("分页获取收车公司信息error",ex);
            throw new CmsServiceException("分页获取收车公司信息失败");
        }
    }
    /**
     * 收车公司用户注册
     *
     * @param  recoveryCompanyVo 收车公司用户注册信息
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> register(@RequestBody RecoveryCompanyVo recoveryCompanyVo){
        try{
            return recoveryUserService.register(recoveryCompanyVo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("收车公司用户注册error",ex);
            throw new CmsServiceException("收车公司用户注册失败");
        }
    }
}
