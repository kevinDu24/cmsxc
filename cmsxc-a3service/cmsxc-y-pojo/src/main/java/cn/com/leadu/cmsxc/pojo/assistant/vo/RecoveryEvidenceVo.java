package cn.com.leadu.cmsxc.pojo.assistant.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/4/13.
 *
 * 收车完成时上传资料用vo
 */
@Data
public class RecoveryEvidenceVo {
    private Long taskId;// 工单id
    private String parkingId; //停车场id
    private String vehicleUser;// 车辆使用人
    private String finishImages; //收车完成时的图片记录
    private String finishVideo; //收车完成时视频
    private String finishRemark; //收车完成时备注
}
