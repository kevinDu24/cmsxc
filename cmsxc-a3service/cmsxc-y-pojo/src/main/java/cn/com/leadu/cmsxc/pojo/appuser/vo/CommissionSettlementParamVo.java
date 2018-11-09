package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/8/7.
 *
 * 佣金结算传参用vo
 */
@Data
public class CommissionSettlementParamVo {
    private String status;// 状态 01：待申请，02：审核中，03：被退回，04：已通过
    private String plate;// 车牌号
    private int page;// 页码
    private int size;// 每页条数
}
