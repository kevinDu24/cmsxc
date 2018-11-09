package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/1/22.
 *
 * 授权列表用vo
 */
@Data
public class AuthorizationListVo {
    private String authorizationId;// 授权id
    private String plate;// 车牌号码
    private String authorizationStatus;// 授权状态
    private Date applyDate;// 申请时间
    private String applyRemark;// 申请备注
    private String leasePhone;// 委托方联系方式
    private Date approvalDate;// 审批时间
    private Date authOutTimeDate;// 授权失效时间
    private String approvalRemark;// 审批备注
    private String isDelay;// 是否延期过 0：没有延期、1：延期
    private Date delayDate;// 延期时间
    private String delayRemark;// 延期备注
    private Date finishDate;// 完成时间
    private Long elapseTime;// 耗时时长
    private Date cancelDate;// 取消时间
    private String authorizationPaperUrl;// 授权书url
    private String cancelPaperUrl;// 取消证书url
    private Date operateDate;//申请时间

}
