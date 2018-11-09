package cn.com.leadu.cmsxc.assistant.controller;

import cn.com.leadu.cmsxc.assistant.service.ApprovalService;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ApprovalDetailSearchVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ApprovalOperateVo;
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
 * 申请审批Controller
 */
@RestController
@RequestMapping("approval")
public class ApprovalController {
    private static final Logger logger = LoggerFactory.getLogger(ApprovalController.class);
    @Autowired
    private ApprovalService approvalService;

    /**
     * 获取待审批数量
     *
     * @param systemUser 用户信息
     * @return
     */
    @RequestMapping(value = "/getCount", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getCount(@AuthUserInfo SystemUser systemUser) {
        try{
            return approvalService.getCount(systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取待审批数量error",ex);
            throw new CmsServiceException("获取待审批数量失败");
        }
    }

    /**
     * 分页获取审批列表
     * @param systemUser
     * @param status 授权状态
     * @param condition 检索条件
     * @param flag "0":分页查询，"1"：搜索查询
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    @RequestMapping(value = "/getApprovalList", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getApprovalList(@AuthUserInfo SystemUser systemUser,
                                                 String status,
                                                 String condition,
                                                 int flag,
                                                 int page,
                                                 int size) {
        try{
            return approvalService.getApprovalList(systemUser,status,condition,flag,page,size);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("分页获取审批列表error",ex);
            throw new CmsServiceException("分页获取审批列表失败");
        }
    }

    /**
     * 审批详情
     *
     * @param param 入参
     * @return
     */
    @RequestMapping(value = "/getApprovalDetail", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getApprovalDetail(ApprovalDetailSearchVo param) {
        try{
            return approvalService.getApprovalDetail(param);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取审批详情error",ex);
            throw new CmsServiceException("获取审批详情失败");
        }
    }

    /**
     * 授权操作
     *
     * @param  vo 画面参数
     * @return
     */
    @RequestMapping(value = "/authorization", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> authorization(@RequestBody ApprovalOperateVo vo, @AuthUserInfo SystemUser sysUser){
        try{
            return approvalService.authorization(vo.getAuthorizationId(), vo.getRemark(), vo.getUserId(),vo.getPlate(),sysUser);
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
    public ResponseEntity<RestResponse> refuse(@RequestBody ApprovalOperateVo vo, @AuthUserInfo SystemUser sysUser){
        try{
            return approvalService.refuse(vo.getAuthorizationId(), vo.getRemark(),sysUser.getUserId());
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
    public ResponseEntity<RestResponse> delay(@RequestBody ApprovalOperateVo vo, @AuthUserInfo SystemUser sysUser) {
        try {
            return approvalService.delay(vo.getAuthorizationId(), vo.getAuthEndDate(), vo.getRemark(), sysUser);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("授权延期error", ex);
            throw new CmsServiceException("授权延期失败");
        }
    }
}