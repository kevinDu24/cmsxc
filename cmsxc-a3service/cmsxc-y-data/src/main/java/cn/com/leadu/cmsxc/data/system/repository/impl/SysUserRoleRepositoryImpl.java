package cn.com.leadu.cmsxc.data.system.repository.impl;

import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.data.system.dao.SysUserRoleDao;
import cn.com.leadu.cmsxc.data.system.repository.SysUserRoleRepository;
import cn.com.leadu.cmsxc.pojo.system.entity.SysUserRole;
import org.springframework.stereotype.Component;

/**
 * @author qiaomengnan
 * @ClassName: SysRoleResourceRepositoryImpl
 * @Description:
 * @date 2018/1/14
 */
@Component
public class SysUserRoleRepositoryImpl extends AbstractBaseRepository<SysUserRoleDao,SysUserRole> implements SysUserRoleRepository {

    /**
     * @Title:
     * @Description:  保存用户角色
     * @param sysUserRole
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 06:04:42
     */
    public SysUserRole insertOne(SysUserRole sysUserRole){
        return super.insert(sysUserRole);
    }

}
