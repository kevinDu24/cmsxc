package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by yuanzhenxia on 2018/8/9.
 *
 * 佣金结算申请提交传参用vo
 */
@Data
public class CommissionApplyParamVo implements Serializable{
    private String taskId;// 工单id
    private String storageInfoId;// 入库信息表id
    private String startAddress;// 板车起点地址
    private String startLon;// 起点经度
    private String startLat;// 起点纬度
    private String flatBedDistance;// 公里数
    private String flatBedFee;// 板车费用
    private String keyFee;// 钥匙费用
    private String applyRemark;// 申请备注
}
