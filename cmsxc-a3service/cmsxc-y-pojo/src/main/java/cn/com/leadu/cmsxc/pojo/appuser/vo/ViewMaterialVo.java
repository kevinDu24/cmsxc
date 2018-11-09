package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/22.
 *
 * 资料预览用vo
 */
@Data
public class ViewMaterialVo {
    private String applicantName;// 申请人姓名
    private String applicantPhone;// 申请人手机号
    private String applicantIdentityNum;// 申请人身份证号
    private String remark;// 备注
    private String photoUrl;// 资料图片路径
    private List<String> photoUrls = new ArrayList<>();// 资料图片路径
    private String applyStatus;// 是否可以申请 0：可以；1：不可以
}
