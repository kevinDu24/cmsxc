package cn.com.leadu.cmsxc.data.system.repository;

import cn.com.leadu.cmsxc.pojo.system.entity.SysRoleResource;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author qiaomengnan
 * @ClassName: SysRoleResourceRepository
 * @Description:
 * @date 2018/1/14
 */
public interface SysRoleResourceRepository {


    /**
     * @Title:
     * @Description:  批量保存角色资源
     * @param sysRoleResources
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 06:04:42
     */
    List<SysRoleResource> insertListByMapper(List<SysRoleResource> sysRoleResources);

    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    public List<SysRoleResource> selectByExampleList(Example example);

    /**
     * 根据角色id批量删除数据
     * @param roleId
     * @return
     */
    public void deleteByRoleId(String roleId);

}
