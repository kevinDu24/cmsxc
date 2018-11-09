package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appuser.entity.CommissionSettlementHistory;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/8/9.
 *
 * 佣金结算日志信息
 */
public interface CommissionSettlementHistoryRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<CommissionSettlementHistory> selectByExampleList(Example example);

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    CommissionSettlementHistory selectOneByExample(Example example);

    /**
     * 登录佣金结算日志信息表
     * @param commissionSettlementHistory
     */
    public void insertOne(CommissionSettlementHistory commissionSettlementHistory);

    /**
     * 根据主键更新表
     * @param commissionSettlementHistory
     */
    void updateByPrimaryKey(CommissionSettlementHistory commissionSettlementHistory);
    /**
     * 批量登录我的佣金结算日志信息
     *
     * @param commissionSettlementHistorys
     */
    void insertMore(List<CommissionSettlementHistory> commissionSettlementHistorys);
}
