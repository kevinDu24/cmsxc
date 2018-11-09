package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/8/9.
 *
 * 申请佣金结算传参用vo
 */
@Data
public class CommissionApplyVo {
    private List<CommissionApplyParamVo> commissionApplyParamVoList;
}
