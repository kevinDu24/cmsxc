package cn.com.leadu.cmsxc.appuser.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appuser.vo.CommissionApplyParamVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.CommissionSettlementParamVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.KilometresParamVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/8/6.
 *
 * 佣金结算service
 */
public interface CommissionSettlementService {
    /**
     * 根据工单id与收车公司id，获取板车使用起点地址
     *
     * @param systemUser 用户信息
     * @param taskId 工单id
     * @return
     */
    ResponseEntity<RestResponse> getStartAddress(SystemUser  systemUser, String taskId);

    /**
     * 获取公里数
     *
     * @param systemUser
     * @param vo 画面参数
     * @return
     */
    ResponseEntity<RestResponse> getKilometres(SystemUser systemUser, KilometresParamVo vo);

    /**
     * 获取佣金结算列表信息
     *
     * @param systemUser 用户信息
     * @param vo 画面参数
     * @return
     */
    ResponseEntity<RestResponse> getCommissionSettlementList(SystemUser  systemUser, CommissionSettlementParamVo vo);

    /**
     * 获取佣金结算列表数量
     *
     * @param systemUser 用户信息
     * @param vo 画面参数
     * @return
     */
    ResponseEntity<RestResponse> getCommissionSettlementCount(SystemUser  systemUser, CommissionSettlementParamVo vo);

    /**
     *  获取佣金结算详情
     *
     * @param taskId 工单id
     * @return
     */
    ResponseEntity<RestResponse> getCommissionSettlementDetail(String taskId );

    /**
     * 批量申请佣金结算
     *
     * @param systemUser 用户信息
     * @param commissionApplyParamVoList  画面参数
     * @return
     */
    ResponseEntity<RestResponse> applyCommissionSettlement(SystemUser systemUser, List<CommissionApplyParamVo> commissionApplyParamVoList);

    /**
     * 佣金结算申请流程被退回后，批量提交佣金结算申请
     *
     * @param systemUser 用户信息
     * @param commissionApplyParamVoList  画面参数
     * @return
     */
    ResponseEntity<RestResponse> submitCommissionSettlement(SystemUser systemUser, List<CommissionApplyParamVo> commissionApplyParamVoList);

}
