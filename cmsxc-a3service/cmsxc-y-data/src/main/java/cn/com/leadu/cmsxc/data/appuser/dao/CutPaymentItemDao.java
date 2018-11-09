package cn.com.leadu.cmsxc.data.appuser.dao;

import cn.com.leadu.cmsxc.data.base.config.BaseDao;
import cn.com.leadu.cmsxc.pojo.appuser.entity.CutPaymentItem;
import cn.com.leadu.cmsxc.pojo.appuser.vo.CutPaymentItemVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/8/9.
 *
 * 佣金结算扣款项dao
 */
public interface CutPaymentItemDao extends BaseDao<CutPaymentItem> {
    /**
     * 根据佣金结算信息id获取扣款项
     *
     * @param commissionSettlementInfoId 佣金结算信息id
     * @return
     */
    List<CutPaymentItemVo> selectByCommissionSettlementInfoId(@Param("commissionSettlementInfoId") String commissionSettlementInfoId);

}
