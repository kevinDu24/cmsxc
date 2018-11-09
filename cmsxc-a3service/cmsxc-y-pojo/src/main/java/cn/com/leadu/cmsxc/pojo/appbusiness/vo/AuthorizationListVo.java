package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/2/5.
 *
 * 授权列表返回用vo
 */
@Data
public class AuthorizationListVo {
    private String id;// 授权Id

    private Long taskId;// 工单Id

    private String taskNum;// 工单号（页面显示）

    private String userId;// 用户id

    private String authorizationStatus;// 授权状态

    private String applicantName;// 申请人姓名

    private String applicantPhone;// 申请人电话

    private String applicantIdentityNum; // 申请人身份证号

    private String remark;// 备注

    private String approvalRemark;// 审批备注

    private Date applyStartDate;// 申请开始时间

    private Date applyEndDate;// 审批日期

    private Date OperateDate;// 操作日期

    private Date authorizationOutTimeDate;// 操作日期

    private String authorizationPaperUrl;// 姓名

    private String name;// 姓名

    private String plate;// 车牌号

    private Integer serviceFee;// 服务费

    private String serviceFeeValue;// 服务费(画面显示)

    private String vehicleProvince;// 车辆所在省份

    private String vehicleCity;// 车辆所在城市

    private String vehicleIdentifyNum;// 车架号

    private String engineNo;// 发动机号

    private String vehicleType;// 车型

    private String vehicleColor;// 车辆颜色

    private String applyStartString;// 申请开始时间

    private String applyEndString;// 审批日期

    private String OperateString;// 操作日期

    private String outTimeDateString;// 失效日期
    
    private String photoUuid; // 图片的UUID

    private String address; // 申请地址
}
