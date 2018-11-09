package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/4/13.
 *
 * 收车完成时上传资料用vo
 */
@Data
public class RecoveryDataVo {
    private Long taskId; //工单id
    private String vehicleRecoveryReport; //车辆回收报告
    private String vehicleNormalInfo; //车辆基本情况
    private String storageFinanceList; //入库财务交接单
    private String vehicleAppearance; //车辆外观照
    private String vehicleGood; //车上物品
    private String otherImages; //其他附件
    private String video; //视频
}
