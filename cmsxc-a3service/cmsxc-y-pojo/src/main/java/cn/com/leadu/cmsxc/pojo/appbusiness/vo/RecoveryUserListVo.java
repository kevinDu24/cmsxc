package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/2/2.
 *
 * 收车公司列表画面传参用vo
 */
@Data
public class RecoveryUserListVo extends PageQuery{

    private String recoveryShortName;// 收车公司简称

    private String recoveryFullName;// 收车公司全称

    private String managerRegisterCode;// 主管注册码

    private String salesmanRegisterCode;// 业务员注册码


}
