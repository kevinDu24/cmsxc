package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.data.appuser.dao.CutPaymentItemDao;
import cn.com.leadu.cmsxc.data.appuser.repository.CutPaymentItemRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.CutPaymentItem;
import cn.com.leadu.cmsxc.pojo.appuser.vo.CutPaymentItemVo;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/8/9.
 *
 * 佣金结算扣款项
 */
@Component
public class CutPaymentItemRepositoryImpl extends AbstractBaseRepository<CutPaymentItemDao,CutPaymentItem> implements CutPaymentItemRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
   public List<CutPaymentItem> selectByExampleList(Example example){
       return super.selectListByExample(example);
   }

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    public  CutPaymentItem selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }

    /**
     * 登录佣金结算扣款项信息表
     * @param cutPaymentItem
     */
    public void insertOne(CutPaymentItem cutPaymentItem){
        super.insert(cutPaymentItem);
    }

    /**
     * 根据主键更新表
     * @param cutPaymentItem
     */
    public void updateByPrimaryKey(CutPaymentItem cutPaymentItem){
        super.updateByPrimaryKey(cutPaymentItem);
    }
    /**
     * 批量登录我的佣金结算扣款项信息
     *
     * @param cutPaymentItems
     */
    public void insertMore(List<CutPaymentItem> cutPaymentItems){
        super.insertListByMapper(cutPaymentItems);
    }
    /**
     * 根据佣金结算信息id获取扣款项
     *
     * @param commissionSettlementInfoId 佣金结算信息id
     * @return
     */
    public List<CutPaymentItemVo>  selectByCommissionSettlementInfoId(String commissionSettlementInfoId){
        return baseDao.selectByCommissionSettlementInfoId(commissionSettlementInfoId);
    }
}
