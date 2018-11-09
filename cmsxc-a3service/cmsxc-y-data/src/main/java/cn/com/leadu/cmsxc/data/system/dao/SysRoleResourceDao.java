package cn.com.leadu.cmsxc.data.system.dao;

import cn.com.leadu.cmsxc.data.base.config.BaseDao;
import cn.com.leadu.cmsxc.pojo.system.entity.SysRoleResource;
import org.apache.ibatis.annotations.Param;

/**
 * @author qiaomengnan
 * @ClassName: SysRoleResourceDao
 * @Description: 角色资源dao
 * @date 2018/1/14
 */
public interface SysRoleResourceDao extends BaseDao<SysRoleResource> {
    void deleteByRoleId(@Param("roleId") String roleId);
}
