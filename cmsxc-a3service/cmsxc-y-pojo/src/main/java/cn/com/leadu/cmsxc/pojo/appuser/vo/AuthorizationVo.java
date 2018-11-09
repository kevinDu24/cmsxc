package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 申请授权画面传参用vo
 */
@Data
public class AuthorizationVo {
    private String vehicleLisensePlate;// 车牌号码
    private String applicantName;// 申请人姓名
    private String applicantPhone;// 申请人电话
    private String applicantIdentityNum; // 申请人身份证号
    private String remark;// 备注
}
