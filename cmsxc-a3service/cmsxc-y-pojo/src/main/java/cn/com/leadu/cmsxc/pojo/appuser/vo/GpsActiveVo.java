package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/2/24.
 *
 * 贴gps激活用vo
 */
@Data
public class GpsActiveVo {
    private String vehicleIdentifyNum; //车架号
    private String plate; //车牌号
    private String simCode; //sn号
    private String vehicleType; //车机类型
    private String installAddress; //安装地址
    private String installPhoneNum; //安装人员手机号
 }
