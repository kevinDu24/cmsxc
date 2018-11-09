package cn.com.leadu.cmsxc.pojo.appbusiness.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 车辆任务工单表
 */
@Data
public class VehicleTask extends BaseEntity {
    @Id
    @GeneratedValue(generator= "JDBC")
    private Long id;

    private String name;// 姓名

    private String plate;// 车牌号

    private Double serviceFee;// 服务费

    private String sendCarAddress;// 送车地址

    private String dockingPeople;// 接车对接人员

    private String vehicleProvince;// 车辆所在省份

    private String vehicleCity;// 车辆所在城市

    private String vehicleIdentifyNum;// 车架号

    private String engineNo;// 发动机号

    private String vehicleType;// 车型

    private String vehicleColor;// 车辆颜色

    private String insuranceInfo;// 保险信息

    private String violationInfo;// 违章信息

    private String gpsSystemUserName;// gps系统账号

    private String gpsSystemUserPassword;// gps系统密码

    private String leaseCompanyUserName;// 委托公司用户名

    private String leaseCompanyName;// 委托公司名称

    private String hasGpsFlag;// gps有无

    private String volitionFlag;// 违章有无

    private String clueFlag;// 线索有无

    private String priceRange;// 价格区间

    private String taskStatus;// 工单状态

    private String brand;// 车辆品牌

    private String manufacturer;//制造商

    private String gpsOnline;//"0":离线 "1":在线

    private Date receiveDate;//赏金系统接收到工单的服务器系统时间

    private String cancelReason;// 取消原因

    private String taskPdfUrl; //主系统的催收服务任务单url路径

    private String idCard; //身份证号码

    private String phoneNum; //车主手机号码

    private String workAddress; //家庭地址

    private String homeAddress; //家庭地址

    private String lifeAddress; //常住地址

    private String fpProvince; //FP所在省份
}
