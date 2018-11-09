package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/8/9.
 *
 * 工单结算详情
 */
@Data
public class CommissionSettlementDetailVo {
    private String commissionSettlementInfoId;// 佣金结算信息表id
    private String taskId;// 工单id
    private String plate;// 车牌号
    private String vehicleIdentifyNum;// 车架号
    private Date finishDate;// 完成时间
    private String totalAmount;// 佣金总额
    private List<CommissionDetailVo> commissionDetailVoList;// 佣金总额因素详情
    private String flatBedDistance;// 公里数
    private String flatBedFee;// 板车费用
    private String keyFee;// 钥匙费用
    private String applyRemark;// 申请备注
    private String startAddress;// 板车起点地址
    private String status;// 状态
    private String auditingRemark;// 审批备注
    private List<CutPaymentItemVo>  cutPaymentItemList;// 扣款项
}
