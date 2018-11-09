package cn.com.leadu.cmsxc.pojo.system.vo;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import lombok.Data;

import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/5/25.
 *
 * 账户注册信息看及导出报表用vo
 */
@Data
public class SysUserSearchVo extends PageQuery {
    private String userId;// 用户id
    private String userName;// 用户姓名
    private String startDate;// 开始时间
    private String endDate;// 结束时间
    private String recoveryCompanyId;// 收车公司id
    private String userRole;// 用户角色
}
