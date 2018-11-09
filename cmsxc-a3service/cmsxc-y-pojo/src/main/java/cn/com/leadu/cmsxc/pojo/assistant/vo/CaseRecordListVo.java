package cn.com.leadu.cmsxc.pojo.assistant.vo;

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

    private String vehicleType;// 车型

    private String hasGpsFlag;// gps有无

    private String volitionFlag;// 违章有无

    private String clueFlag;// 线索有无

    private String vehicleIdentifyNum;// 车架号

    private String taskStatus;// 工单状态  01:未受理，02：已受理，03：已完成，04：已取消

    private String recoveryCompanyName;// 收车公司名称

    private String recoveryCompanyId;// 收车公司id

}
