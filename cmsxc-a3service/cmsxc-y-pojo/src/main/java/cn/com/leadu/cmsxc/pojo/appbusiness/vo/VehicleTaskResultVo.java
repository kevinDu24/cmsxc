package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTask;
import cn.com.leadu.cmsxc.pojo.appuser.vo.applydetail.AuthInfoVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/17.
 *
 * 返回任务工单参数
 */
@Data
public class VehicleTaskResultVo {
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

    private String applyAuthStatus;//0:可授权、1:自己已授权、2:他人已授权、3：申请审批中

    private String authorizationStatus;// 悬赏授权状态(01:申请中;02:已拒绝;03:已授权;04:已失效;05:已完成;06:已取消)

    private String remark;// 备注

    private String approveRemark;// 审批备注

    private Date applyStartDate;// 申请开始时间

    private Date applyEndDate;// 申请结束时间

    private String leasePhone;// 委托方联系电话

    private Date authStartDate;// 授权生效时间

    private Date authOutTimeDate;// 授权失效时间

    private String cancelPaperUrl;// 取消证书url

    private String cancelReason;// 取消原因

    private String authorizationPaperUrl;// 授权书url

    private String leaseCompanyUserName;// 委托方用户名

    private Date operateDate;// 操作时间

    private String isDelay;//是否授权延期 0：无 1：有

    private String brand;//品牌

    private String priceRange;// 价格区间

    private List<AuthInfoVo> authInfo;

    private String gpsOnLineFlag;// GPS是否在线  0：不在线，1：在线

    //.........................赏金寻车3期追加.......................//

    private String idCard; //身份证号码

    private String workAddress; //家庭地址

    private String homeAddress; //家庭地址

    private String lifeAddress; //常住地址

    private String phoneNum;// 手机号

    public VehicleTaskResultVo(VehicleTask task){
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
        this.gpsOnLineFlag = StringUtil.isNotNull(task.getGpsOnline()) && "1".equals(task.getGpsOnline()) ? "1" : "0";
        //.........................赏金寻车3期追加.......................//
        this.idCard = task.getIdCard();
        this.workAddress = task.getWorkAddress();
        this.homeAddress = task.getHomeAddress();
        this.lifeAddress = task.getLifeAddress();
        this.phoneNum = task.getPhoneNum();
    }
    public VehicleTaskResultVo(){}
}
