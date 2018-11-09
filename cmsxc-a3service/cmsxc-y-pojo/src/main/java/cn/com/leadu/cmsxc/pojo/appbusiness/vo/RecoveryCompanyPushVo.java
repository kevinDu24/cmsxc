package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/1/17.
 *
 * 金融机构推送收车公司信息
 */
@Data
public class RecoveryCompanyPushVo {

    private String id;// 主系统收车公司id (废弃)

    private String recoveryShortName;// 收车公司简称

    private String recoveryFullName;// 收车公司全称

    private String contactName;// 联系人姓名

    private String contactPhone;// 联系人电话

    private String contactAddress;// 联系人地址

    private String contactEmail;// 联系人地址

    public RecoveryCompanyPushVo(){}
}
