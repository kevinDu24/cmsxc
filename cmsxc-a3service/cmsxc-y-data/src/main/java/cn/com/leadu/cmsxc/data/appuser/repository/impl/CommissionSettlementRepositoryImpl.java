package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.data.appuser.dao.CommissionSettlementDao;
import cn.com.leadu.cmsxc.data.appuser.repository.CommissionSettlementRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.CommissionSettlementInfo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.CommissionSettlementDetailVo;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/8/9.
 *
 * 佣金结算信息
 */
@Component
public class CommissionSettlementRepositoryImpl extends AbstractBaseRepository<CommissionSettlementDao,CommissionSettlementInfo> implements CommissionSettlementRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    public List<CommissionSettlementInfo> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    public CommissionSettlementInfo selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }

    /**
     * 登录佣金结算信息表
     * @param commissionSettlementInfo
     */
    public void insertOne(CommissionSettlementInfo commissionSettlementInfo){
        super.insert(commissionSettlementInfo);
    }

    /**
     * 根据主键更新表
     * @param commissionSettlementInfo
     */
    @Override
    public void updateByPrimaryKey(CommissionSettlementInfo commissionSettlementInfo) {
        super.updateByPrimaryKey(commissionSettlementInfo);
    }
    /**
     * 批量登录我的佣金结算信息
     *
     * @param commissionSettlementInfos
     */
    public void insertMore(List<CommissionSettlementInfo> commissionSettlementInfos){
        super.insertListByMapper(commissionSettlementInfos);
    }
    /**
     * 佣金结算申请详情
     *
     * @param taskId 工单id
     * @return
     */
    public CommissionSettlementDetailVo selectCommissionSettlementDetail(String taskId){
        return baseDao.selectCommissionSettlementDetail(taskId);
    }


}
