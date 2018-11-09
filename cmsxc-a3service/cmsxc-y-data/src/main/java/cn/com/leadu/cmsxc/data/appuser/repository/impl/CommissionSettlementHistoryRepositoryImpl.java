package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.data.appuser.dao.CommissionSettlementHistoryDao;
import cn.com.leadu.cmsxc.data.appuser.repository.CommissionSettlementHistoryRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.CommissionSettlementHistory;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/8/9.
 *
 * 佣金结算日志信息
 */
@Component
public class CommissionSettlementHistoryRepositoryImpl extends AbstractBaseRepository<CommissionSettlementHistoryDao,CommissionSettlementHistory> implements CommissionSettlementHistoryRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    public List<CommissionSettlementHistory> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    public CommissionSettlementHistory selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }

    /**
     * 登录佣金结算日志信息表
     * @param commissionSettlementHistory
     */
    public void insertOne(CommissionSettlementHistory commissionSettlementHistory){
        super.insert(commissionSettlementHistory);
    }

    /**
     * 根据主键更新表
     * @param commissionSettlementHistory
     */
    @Override
    public void updateByPrimaryKey(CommissionSettlementHistory commissionSettlementHistory) {
        super.updateByPrimaryKey(commissionSettlementHistory);
    }
    /**
     * 批量登录我的佣金结算日志信息
     *
     * @param commissionSettlementHistorys
     */
    public void insertMore(List<CommissionSettlementHistory> commissionSettlementHistorys){
        super.insertListByMapper(commissionSettlementHistorys);
    }
}
