package cn.com.leadu.cmsxc.pojo.system.vo;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import cn.com.leadu.cmsxc.pojo.system.entity.SysResource;
import lombok.Data;

import java.util.List;

/**
 * @author qiaomengnan
 * @ClassName: SysUserSelectVo
 * @Description: 用户信息载体
 * @date 2018/1/9
 */
@Data
public class SystemUserVo extends PageQuery {

    private String id;

    private String userId; //用户名

    private String password; //密码

    private String userRole; //用户角色

    private String userName; //用户姓名

    private String recoveryCompanyId;// 收车公司id

    private String recoveryFullName;// 收车名称

    private String leaseId;// 金融机构id

    private String leaseFullName;// 金融机构名称

    private List<SysResource> resources; //用户菜单资源

}
