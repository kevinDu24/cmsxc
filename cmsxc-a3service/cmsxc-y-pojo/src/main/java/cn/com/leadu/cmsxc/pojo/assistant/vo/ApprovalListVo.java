package cn.com.leadu.cmsxc.pojo.assistant.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/2/5.
 *
 * 审批列表app返回用vo
 */
@Data
public class ApprovalListVo {
    private String plate; // 车牌号码
    private String vehicleIdentifyNum; // 车架号
    private String taskId;// 工单id
    private String userId;//申请人账号
    private String authorizationStatus; // 授权状态
    private String remark; //申请备注
    private Date applyStartDate; //申请时间
    private Date applyEndDate; //（申请结束时间）授权开始时间
    private String authorizationId; //授权id
    private String approvalRemark; //审批备注、延期备注
    private Date operateDate; //完成时间、申请取消时间
    private Date authorizationOutTimeDate; //授权结束时间、失效时间
    private String cancelReason; //委托方取消原因
    private String address; //中文地址
    private String delayRemark; //延期备注
    private Date delayDate; //延期时间
    private String isDelay;//是否延期（0：未延期，1：已延期）

}
