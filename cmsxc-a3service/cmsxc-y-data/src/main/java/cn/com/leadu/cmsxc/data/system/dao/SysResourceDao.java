package cn.com.leadu.cmsxc.data.system.dao;

import cn.com.leadu.cmsxc.data.base.config.BaseDao;
import cn.com.leadu.cmsxc.pojo.system.entity.SysResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author qiaomengnan
 * @ClassName: SysResourceDao
 * @Description:
 * @date 2018/1/14
 */
public interface SysResourceDao extends BaseDao<SysResource> {

    /**
     * @Title:
     * @Description: 根据角色id以及资源类型查询接口资源
     * @param roleIds
     * @param type
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 12:39:33
     */
    List<String> selectSysResBySysUserId(@Param("roleIds") List<String> roleIds,@Param("type") String type);


    /**
     * @Title:
     * @Description: 根据角色id以及资源类型查询菜单资源
     * @param roleIds
     * @param type
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 12:39:33
     */
    List<SysResource> selectSysResMenuBySysRoleId(@Param("roleIds") List<String> roleIds, @Param("type") Integer type);


}
