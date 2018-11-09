package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appuser.entity.CommissionSettlementInfo;
import cn.com.leadu.cmsxc.pojo.appuser.entity.CutPaymentItem;
import cn.com.leadu.cmsxc.pojo.appuser.vo.CutPaymentItemVo;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/8/9.
 *
 * 佣金结算扣款项
 */
public interface CutPaymentItemRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<CutPaymentItem> selectByExampleList(Example example);

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    CutPaymentItem selectOneByExample(Example example);

    /**
     * 登录佣金结算扣款项信息表
     * @param cutPaymentItem
     */
    public void insertOne(CutPaymentItem cutPaymentItem);

    /**
     * 根据主键更新表
     * @param cutPaymentItem
     */
    void updateByPrimaryKey(CutPaymentItem cutPaymentItem);
    /**
     * 批量登录我的佣金结算扣款项信息
     *
     * @param cutPaymentItems
     */
    void insertMore(List<CutPaymentItem> cutPaymentItems);
    /**
     * 根据佣金结算信息id获取扣款项
     *
     * @param commissionSettlementInfoId 佣金结算信息id
     * @return
     */
    List<CutPaymentItemVo>  selectByCommissionSettlementInfoId(String commissionSettlementInfoId);
}
