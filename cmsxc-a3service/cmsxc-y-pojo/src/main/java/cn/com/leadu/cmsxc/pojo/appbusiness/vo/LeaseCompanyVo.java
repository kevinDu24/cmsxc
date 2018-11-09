package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/2/2.
 *
 * 委托公司注册用vo
 */
@Data
public class LeaseCompanyVo {

    private String name;//用户名

    private String password;// 密码

    private String shortName;// 委托公司简称

    private String fullName;// 委托公司全称

    private String contactName;// 联系人姓名

    private String contactPhone;// 联系人电话

}
