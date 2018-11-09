package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/4/13.
 *
 * 出示二维码页面所有信息vo
 */
@Data
public class ParkingInfoVo {
    private ParkingInfoSubVo parkingInfo;// 停车场信息
    private ScanInfoVo scanInfo;// 二维码扫描信息
}
