package cn.com.leadu.cmsxc.pojo.assistant.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/4/13.
 *
 * 入库管理列表用vo
 */
@Data
public class StorageListVo {
    private String storageId;// 入库id
    private Long taskId;// 工单id
    private String parkingId;// 停车场id
    private String state;// 入库状态
    private String plate;// 车牌号
    private String vehicleIdentifyNum;// 车架号
    private String brand; //车型
    private String address;// 抵达地点
    private Date recoveryTime; //收车时间
    private Date arriveTime; //抵达停车场时间
    private Date finishTime; //完成入库时间
    private Date stopTime; //终止入库时间
}
