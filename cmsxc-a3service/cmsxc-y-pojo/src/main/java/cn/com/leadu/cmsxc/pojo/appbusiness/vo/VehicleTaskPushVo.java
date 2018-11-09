package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/1/17.
 *
 * 金融机构推送工单信息
 */
@Data
public class VehicleTaskPushVo {
    private String name;// 车主姓名

    private String plate;// 车牌号

    private Double serviceFee;// 服务费

    private String vehicleProvince;// 车辆所在省份

    private String vehicleCity;// 车辆所在城市

    private String vehicleIdentifyNum;// 车架号

    private String engineNo;// 发动机号

    private String vehicleType;// 车型

    private String vehicleColor;// 车辆颜色

    private String insuranceInformation;// 保险信息

    private String violationInformation;// 违章信息

    private String gpsSystemUserName;// gps系统账号

    private String gpsSystemUserPassword;// gps系统密码

    private String leaseCompanyUserName;// 委托公司用户名

    private String leaseCompanyName;// 委托公司名称

    private String gpsOnline; //gps是否在线：1：在线  0：离线（超过1天算离线）

    private String recoveryCompanyId; //赏金系统收车公司主键

    private String pushDate;//收车任务发起时间（yyyyMMddHHmmss）

    private String brand;//车辆品牌

    private String manufacturer;//制造商

    private String url; //催收服务任务单url

    private String idCard; //身份证号码

    private String phoneNum; //车主手机号码

    private String workAddress; //家庭地址

    private String homeAddress; //家庭地址

    private String lifeAddress; //常住地址

    private String fpProvince; //FP所在省份

    public VehicleTaskPushVo(){}
}
