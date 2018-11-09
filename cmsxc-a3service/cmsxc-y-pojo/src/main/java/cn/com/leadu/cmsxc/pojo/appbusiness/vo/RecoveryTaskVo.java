package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/2/12.
 *
 * 收车公司任务派单列表返回用vo
 */
@Data
public class RecoveryTaskVo {

    private String taskId; // 任务id

    private String plate;// 车牌号

    private Date startTime;// 发起时间

    private Date failureTime;// 失效时间

    private String status;// 状态

    private String hasGpsFlag;// gps有无

    private String volitionFlag;// 违章有无

    private String clueFlag;// 线索有无

    private String sendCarAddress;// 送车地址

    private String vehicleIdentifyNum;// 车架号

    private String vehicleType;// 车型

    private String authorizationId;// 授权id

    private Date authStartDate;// 授权开始时间

    private Date authOutTimeDate;// 授权失效时间

    private Date operateDate;// 操作时间

    private String priceRange;// 价格区间

}
