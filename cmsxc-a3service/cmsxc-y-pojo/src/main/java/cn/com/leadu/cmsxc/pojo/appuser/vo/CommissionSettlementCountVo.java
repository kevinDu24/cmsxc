package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/8/8.
 *
 * 佣金结算列表数量
 */
@Data
public class CommissionSettlementCountVo {

    private Integer applyCount;// 待申请数量
    private Integer auditingCount;// 审核中数量
    private Integer rebackCount;// 被退回数量
    private Integer passCount;// 已通过数量

}
