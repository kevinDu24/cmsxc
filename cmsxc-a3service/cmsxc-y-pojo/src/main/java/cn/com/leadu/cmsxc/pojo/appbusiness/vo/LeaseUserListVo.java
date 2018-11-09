package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/2/2.
 *
 * 委托公司列表画面参数用vo
 */
@Data
public class LeaseUserListVo extends PageQuery {

    private String leaseShortName;// 委托公司简称
    private String leaseFullName;// 委托公司全称
}
