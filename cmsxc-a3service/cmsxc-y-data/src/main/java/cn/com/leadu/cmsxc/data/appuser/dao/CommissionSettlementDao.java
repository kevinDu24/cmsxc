package cn.com.leadu.cmsxc.data.appuser.dao;

import cn.com.leadu.cmsxc.data.base.config.BaseDao;
import cn.com.leadu.cmsxc.pojo.appuser.entity.CommissionSettlementInfo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.CommissionSettlementDetailVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.GpsActiveCheckVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/8/9.
 *
 * 佣金结算
 */
public interface CommissionSettlementDao extends BaseDao<CommissionSettlementInfo> {

    /**
     * 佣金结算详情
     * @param taskId 工单id
     * @return
     */
    CommissionSettlementDetailVo selectCommissionSettlementDetail(@Param("taskId") String taskId);

}
