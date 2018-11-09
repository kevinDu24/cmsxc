package cn.com.leadu.cmsxc.data.system.repository;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.pojo.system.entity.SysResource;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author qiaomengnan
 * @ClassName: SysResourceRepository
 * @Description:
 * @date 2018/1/14
 */
public interface SysResourceRepository {

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
    List<SysResource> selectSysResMenuBySysRoleId(List<String> roleIds,Integer type);

    /**
     * @Title:
     * @Description:  根据条件查询资源
     * @param example
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 03:55:41
     */
    List<SysResource> selectListByExample(Example example);

    /**
     * @Title:
     * @Description: 分页查询
     * @param example,pageQuery
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 04:49:17
     */
    PageInfoExtend<SysResource> selectListByExamplePageInfo(Example example, PageQuery pageQuery);

}
