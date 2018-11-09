package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/2/12.
 *
 * 派单管理列表返回用vo
 */
@Data
public class RecoveryTaskManagerVo {

    private String taskId; // 任务id

    private String plate;// 车牌号

    private Date startTime;// 派单时间

    private Date failureTime;// 失效时间

    private String status;// 状态

    private String hasGpsFlag;// gps有无

    private String volitionFlag;// 违章有无

    private String clueFlag;// 线索有无

    private String vehicleIdentifyNum;// 车架号

    private String vehicleType;// 车型

    private String authorizationId;// 授权id

    private Date authStartDate;// 授权开始时间

    private Date authOutTimeDate;// 授权失效时间

    private Date operateDate;// 操作时间

    private String serviceFee;// 服务费

    private String leaseFullName;//委托方

    private Date assignDate;//分配时间

    private String groupName; //分配小组名称

    private String assignmentFlag; //是否分配  "0":未分配、"1":已分配

    private String groupId;// 分組id（取消分配時用）

}
