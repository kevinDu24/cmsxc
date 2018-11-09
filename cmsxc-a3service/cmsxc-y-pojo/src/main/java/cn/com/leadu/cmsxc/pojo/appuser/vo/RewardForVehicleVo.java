package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 悬赏列表返回值用
 */
@Data
public class RewardForVehicleVo {

    private String taskId;// 任务id

    private String plate;// 车牌号

    private Integer serviceFee;// 服务费

    private String sendCarAddress;// 送车地址

    private String vehicleType;// 车型

    private String hasGpsFlag;// gps有无

    private String volitionFlag;// 违章有无

    private String clueFlag;// 线索有无

    private String checkFlag;// 是否查看过（0：未查看，1：已查看）

    private String effectiveFlag; // 工单状态是否有效、即是否为正常（0:失效，1：有效）

    private String ownFlag;// 是否是自己收车公司的（0：不是，1：是）

    private String priceRange;// 价格区间

    private String vehicleIdentifyNum;// 车架号

}
