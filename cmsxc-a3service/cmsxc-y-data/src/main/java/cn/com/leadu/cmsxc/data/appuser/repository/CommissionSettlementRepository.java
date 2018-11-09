package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appuser.entity.CommissionSettlementInfo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.CommissionSettlementDetailVo;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/8/9.
 *
 * 佣金结算
 */
public interface CommissionSettlementRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<CommissionSettlementInfo> selectByExampleList(Example example);

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    CommissionSettlementInfo selectOneByExample(Example example);

    /**
     * 登录佣金结算信息表
     * @param commissionSettlementInfo
     */
    public void insertOne(CommissionSettlementInfo commissionSettlementInfo);

    /**
     * 根据主键更新表
     * @param commissionSettlementInfo
     */
    void updateByPrimaryKey(CommissionSettlementInfo commissionSettlementInfo);
    /**
     * 批量登录我的佣金结算信息
     *
     * @param commissionSettlementInfos
     */
    void insertMore(List<CommissionSettlementInfo> commissionSettlementInfos);

    /**
     * 佣金结算申请详情
     *
     * @param taskId 工单id
     * @return
     */
    CommissionSettlementDetailVo selectCommissionSettlementDetail(String taskId);
}
