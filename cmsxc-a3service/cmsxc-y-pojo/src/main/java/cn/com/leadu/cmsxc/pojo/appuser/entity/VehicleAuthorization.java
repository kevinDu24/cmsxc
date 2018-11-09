package cn.com.leadu.cmsxc.pojo.appuser.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 单车授权表
 */
@Data
public class VehicleAuthorization extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private Long taskId;// 工单Id

    private String userId;// 用户id

    private String applicantName;// 申请人姓名

    private String applicantPhone;// 申请人电话

    private String applicantIdentityNum; // 申请人身份证号

    private String photoUuid;// 汽车照片附件标识

    private String authorizationStatus;// 授权状态

    private String remark;// 备注

    private String approvalRemark;// 审批备注

    private Date applyStartDate;// 申请开始时间

    private Date applyEndDate;// 申请结束日期

    private Date operateDate;// 操作日期

    private Date authorizationOutTimeDate;// 授权失效时间

    private String authorizationPaperUrl;// 授权书url

    private String cancelPaperUrl;// 取消证书url

    private String cancelReason;// 取消原因

    private String isDelay;// 是否延期过 0：没有延期、1：延期

    private Date delayDate;// 延期日期

    private String delayRemark;// 审批备注

    private String address;// 位置

    private String lon;// 经度

    private String lat;// 纬度

}
