package cn.com.leadu.cmsxc.data.system.repository;

import cn.com.leadu.cmsxc.pojo.system.entity.SysUserRole;

/**
 * @author qiaomengnan
 * @ClassName: SysRoleResourceRepository
 * @Description:
 * @date 2018/1/14
 */
public interface SysUserRoleRepository {


    /**
     * @Title:
     * @Description:  保存用户角色
     * @param sysUserRole
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 06:04:42
     */
    SysUserRole insertOne(SysUserRole sysUserRole);

}
