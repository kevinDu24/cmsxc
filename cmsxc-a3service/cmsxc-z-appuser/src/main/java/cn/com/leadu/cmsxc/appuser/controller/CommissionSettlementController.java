package cn.com.leadu.cmsxc.appuser.controller;

import cn.com.leadu.cmsxc.appuser.service.CommissionSettlementService;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appuser.vo.CommissionApplyParamVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.CommissionApplyVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.CommissionSettlementParamVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.KilometresParamVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/8/7.
 *
 * 佣金结算
 */
@RestController
@RequestMapping("commissionsettlement")
public class CommissionSettlementController {
    private static final Logger logger = LoggerFactory.getLogger(CommissionSettlementController.class);
    @Autowired
    private CommissionSettlementService commissionSettlementService;

    /**
     * 获取佣金结算列表信息
     *
     * @param systemUser 当前用户
     * @param vo 画面参数
     * @return
     */
    @RequestMapping(value = "/getCommissionSettlementList", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getCommissionSettlementList(@AuthUserInfo SystemUser systemUser, CommissionSettlementParamVo vo) {
        try{
            return commissionSettlementService.getCommissionSettlementList(systemUser,vo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取佣金结算列表信息error",ex);
            throw new CmsServiceException("获取佣金结算列表信息失败");
        }
    }
    /**
     * 获取佣金结算列表数量
     *
     * @param systemUser 当前用户
     * @param vo 画面参数
     * @return
     */
    @RequestMapping(value = "/getCommissionSettlementCount", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getCommissionSettlementCount(@AuthUserInfo SystemUser systemUser, CommissionSettlementParamVo vo) {
        try{
            return commissionSettlementService.getCommissionSettlementCount(systemUser,vo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取佣金结算列表数量error",ex);
            throw new CmsServiceException("获取佣金结算列表数量失败");
        }
    }
    /**
     * 获取佣详情信息
     *
     * @param systemUser 当前用户
     * @param taskId 工单id
     * @return
     */
    @RequestMapping(value = "/getCommissionSettlementDetail", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getCommissionSettlementDetail(@AuthUserInfo SystemUser systemUser, String taskId) {
        try{
            return commissionSettlementService.getCommissionSettlementDetail(taskId);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取佣详情信息error",ex);
            throw new CmsServiceException("获取佣详情信息失败");
        }
    }
    /**
     * 申请佣金结算
     *
     * @param systemUser 当前用户
     * @param vo 画面参数
     * @return
     */
    @RequestMapping(value = "/applyCommissionSettlement", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> applyCommissionSettlement(@AuthUserInfo SystemUser systemUser, @RequestBody CommissionApplyVo vo) {
        try{
            return commissionSettlementService.applyCommissionSettlement(systemUser,vo.getCommissionApplyParamVoList());
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("申请佣金结算error",ex);
            throw new CmsServiceException("申请佣金结算失败");
        }
    }
    /**
     * 佣金结算申请流程被退回后，批量提交佣金结算申请
     *
     * @param systemUser 当前用户
     * @param vo 画面参数
     * @return
     */
    @RequestMapping(value = "/submitCommissionSettlement", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> submitCommissionSettlement(@AuthUserInfo SystemUser systemUser, @RequestBody CommissionApplyVo vo) {
        try{
            return commissionSettlementService.submitCommissionSettlement(systemUser,vo.getCommissionApplyParamVoList());
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("佣金结算申请流程被退回后，批量提交佣金结算申请error",ex);
            throw new CmsServiceException("佣金结算申请流程被退回后，批量提交佣金结算申请失败");
        }
    }


    /**
     * 根据工单id与收车公司id，获取板车使用起点地址
     *
     * @param systemUser 当前用户
     * @return
     */
    @RequestMapping(value = "/getStartAddress", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getStartAddress(@AuthUserInfo SystemUser systemUser, String taskId) {
        try{
            return commissionSettlementService.getStartAddress(systemUser,taskId);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("根据工单id与收车公司id，获取板车使用起点地址error",ex);
            throw new CmsServiceException("根据工单id与收车公司id，获取板车使用起点地址失败");
        }
    }

    /**
     * 获取公里数
     *
     * @param systemUser 当前用户
     * @return
     */
    @RequestMapping(value = "/getKilometres", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getKilometres(@AuthUserInfo SystemUser systemUser, KilometresParamVo vo) {
        try{
            return commissionSettlementService.getKilometres(systemUser,vo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取公里数error",ex);
            throw new CmsServiceException("获取公里数失败");
        }
    }

}
