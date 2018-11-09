package cn.com.leadu.cmsxc.system.controller;

import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.LeaseCompanyVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.LeaseUserListVo;
import cn.com.leadu.cmsxc.system.service.LeaseUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yuanzhenxia on 2018/2/2.
 *
 * 委托公司用户管理
 */
@RestController
@RequestMapping("lease_user")
public class LeaseUserController {
    private static final Logger logger = LoggerFactory.getLogger(LeaseUserController.class);
    @Autowired
    private LeaseUserService LeaseUserService;

    /**
     * 根据委托公司简称或全称，分页获取委托公司信息
     *
     * @param LeaseUserListVo 委托公司全称
     * @return
     */
    @RequestMapping(value = "/findLeaseUserByPage", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findLeaseUserByPage(LeaseUserListVo LeaseUserListVo){
        try{
            return LeaseUserService.findLeaseUserByPage(LeaseUserListVo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("分页获取委托公司信息error",ex);
            throw new CmsServiceException("分页获取委托公司信息失败");
        }
    }
    /**
     * 获取委托公司信息
     *
     * @return
     */
    @RequestMapping(value = "/getLeaseUser", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getLeaseUser(){
        try{
            return LeaseUserService.getLeaseUser();
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取委托公司信息error",ex);
            throw new CmsServiceException("获取委托公司信息失败");
        }
    }
    /**
     * 委托公司用户注册
     *
     * @param  LeaseCompanyVo 委托公司用户注册信息
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> register(@RequestBody LeaseCompanyVo LeaseCompanyVo){
        try{
            return LeaseUserService.register(LeaseCompanyVo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("委托公司用户注册error",ex);
            throw new CmsServiceException("委托公司用户注册失败");
        }
    }
}
