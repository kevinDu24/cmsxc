package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenixa on 2018/8/9.
 *
 * 佣金结算扣款项vo
 */
@Data
public class CutPaymentItemVo {

    private String cutPaymentName;// 扣款项目名称

    private Double cutPaymentCount;// 扣款项目金额
}
