package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/2/1.
 *
 * 收车公司注册用vo
 */
@Data
public class RecoveryCompanyVo {

    private String shortName;// 收车公司简称

    private String fullName;// 收车公司全称

    private String contactName;// 联系人姓名

    private String contactPhone;// 联系人电话

    private String contactAddress;// 联系人地址

    private String contactEmail;// 联系人邮箱

}
