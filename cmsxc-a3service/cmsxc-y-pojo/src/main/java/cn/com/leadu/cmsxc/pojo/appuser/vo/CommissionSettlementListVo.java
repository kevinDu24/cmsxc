package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/8/7.
 *
 * 佣金结算列表返回用vo
 */
@Data
public class CommissionSettlementListVo {
    private String taskId;// 工单号
    private String storageInfoId;// 入库信息表id
    private String plate;// 车牌号
    private String vehicleIdentifyNum;// 车架号
    private Double serviceFee;// 服务费
    private String flatBedFlag;// 是否使用板车flag 0：未使用，1：使用
    private String keyFlag;// 是否使用钥匙flag   0：未使用，1：使用
    private Date finishDate;// 完成时间
    private String status;// 状态
    private String applyDate;// 申请时间

}
