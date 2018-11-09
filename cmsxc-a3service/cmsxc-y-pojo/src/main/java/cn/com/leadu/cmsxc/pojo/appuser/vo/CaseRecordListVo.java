package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/4/14.
 *
 * 案件管理列表返回值用vo
 */
@Data
public class CaseRecordListVo {
    private String taskId;// 任务id

    private String plate;// 车牌号

    private Integer serviceFee;// 服务费

    private String priceRange;// 服务费等级

    private String vehicleType;// 车型

    private String hasGpsFlag;// gps有无

    private String volitionFlag;// 违章有无

    private String clueFlag;// 线索有无

    private String vehicleIdentifyNum;// 车架号

    private String status;// 工单状态  01:待收车，02：共享中，03：短期授权，04：自己完成，05：他人完成，06：委托方取消，07：授权失效。

    private Date operateDate; // 操作时间

    private String groupName;// 小组名称

    private String groupId;// 分组id

    private String taskStatus;// 任务状态 1：跟进中，2：已完成

    private String authorizationUserId;// 获得授权用户id

}
