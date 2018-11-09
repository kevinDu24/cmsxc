package cn.com.leadu.cmsxc.pojo.assistant.vo;

import cn.com.leadu.cmsxc.pojo.appuser.vo.ScanInfoVo;
import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/4/13.
 *
 * 展示停车场人员已上传资料用vo
 */
@Data
public class SubmitedInfoVo {
    private String leaseRefuseRemark;// 驳回备注
    private ScanInfoVo vehicleInfo;// 车辆信息
    private ParkingDataVo attachment;// 附件信息
}
