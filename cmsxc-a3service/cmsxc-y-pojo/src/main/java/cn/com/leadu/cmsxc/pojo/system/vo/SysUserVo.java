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
public class SysUserVo extends PageQuery {

    private String id;

    private String userName;

    private String password;

    private String phone;

    private String realName;

    private String userRole;

    private List<SysResource> resources;

}
