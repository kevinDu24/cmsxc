package cn.com.leadu.cmsxc.data.system.dao;

import cn.com.leadu.cmsxc.data.base.config.BaseDao;
import cn.com.leadu.cmsxc.pojo.system.entity.SysRole;

import java.util.List;

/**
 * @author qiaomengnan
 * @ClassName: SysRoleDao
 * @Description: 角色dao
 * @date 2018/1/12
 */
public interface SysRoleDao extends BaseDao<SysRole> {

    List<SysRole> selectSysRoleBySysUserId(String userId);

    List<String> selectSysRoleIdsBySysUserId(String userId);

}
