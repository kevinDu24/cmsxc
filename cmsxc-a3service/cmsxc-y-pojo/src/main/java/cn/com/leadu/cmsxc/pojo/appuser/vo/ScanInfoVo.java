package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/4/13.
 *
 * 扫码信息vo
 */
@Data
public class ScanInfoVo {
    private String plate; //车牌号
    private String brand; //车型
    private String manufacturer;//品牌
    private String vehicleColor;// 车辆颜色
    private String engineNo;// 发动机号
    private String vehicleIdentifyNum;// 车架号

    private String name; //送车人姓名
    private String phoneNum; //送车人手机号
    private String recoveryCompany; //所属收车公司
}
