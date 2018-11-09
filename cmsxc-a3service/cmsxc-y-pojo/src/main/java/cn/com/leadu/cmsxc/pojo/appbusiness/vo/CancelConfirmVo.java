package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/3/5.
 *
 * 取消确认返回用vo
 */
@Data
public class CancelConfirmVo {
    private String recoveryCompanyName;// 收车公司全称
    private String telPhoneNum;// 联系方式
    private Date startDate;// 授权开始时间
    private Date outTimeDate;// 授权失效时间
    private String contactEmail;// 收车公司邮箱
}
