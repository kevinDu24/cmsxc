package cn.com.leadu.cmsxc.pojo.assistant.vo;

import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTask;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.ClueDetail;
import cn.com.leadu.cmsxc.pojo.appuser.vo.applydetail.AuthInfoVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/17.
 *
 * 审批详情页返回用vo
 */
@Data
public class ApprovalDetailVo {
    private Long taskId;// 工单id

    private String name;// 姓名

    private String plate;// 车牌号

    private Double serviceFee;// 服务费

    private String sendCarAddress;// 送车地址

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

    private String authorizationId;// 授权id

    private List<ClueDetail> clueDetails = new ArrayList<>();// 车辆线索详情

    private String appDownLoadUrl;// app下载二维码url

    private String authorizationStatus;// 悬赏授权状态(01:申请中;02:已拒绝;03:已授权;04:已失效;05:已完成;06:已取消)

    private String brand;//品牌

    private String priceRange;// 价格区间

    private String userName;// 申请人账号对应姓名

    private String userId;// 申请人账号

    private String userRole;// 申请人角色

    private String recoveryCompanyName; //申请账号所属收车公司名称

    private String recoveryCompanyPhone;//申请账号所属收车公司电话

    private String applicantPhone;//申请授权时填写的联系电话

    private List<AuthInfoVo> authInfo;//授权信息

    public ApprovalDetailVo(VehicleTask task){
        this.taskId = task.getId();
        this.name = task.getName();
        this.plate = task.getPlate();
        this.serviceFee = task.getServiceFee();
        this.sendCarAddress = task.getSendCarAddress();
        this.vehicleProvince = task.getVehicleProvince();
        this.vehicleCity = task.getVehicleCity();
        this.vehicleIdentifyNum = task.getVehicleIdentifyNum();
        this.engineNo = task.getEngineNo();
        this.vehicleType = task.getBrand() == null ? task.getVehicleType() : task.getBrand() + " "
                + (task.getVehicleType() == null ? "" : task.getVehicleType());
        this.vehicleColor = task.getVehicleColor();
        this.insuranceInfo = task.getInsuranceInfo();
        this.violationInfo = task.getViolationInfo();
        this.gpsSystemUserName = task.getGpsSystemUserName();
        this.gpsSystemUserPassword = task.getGpsSystemUserPassword();
        this.brand = task.getManufacturer();//将db中存储的制造商的值作为品牌返回给客户端
        this.priceRange = task.getPriceRange();// 价格区间
    }
    public ApprovalDetailVo(){}
}
