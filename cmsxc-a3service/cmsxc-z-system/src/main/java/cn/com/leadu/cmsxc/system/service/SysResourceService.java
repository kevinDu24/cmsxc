package cn.com.leadu.cmsxc.system.service;

import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.pojo.system.entity.SysResource;
import cn.com.leadu.cmsxc.pojo.system.vo.SysResourceVo;

import java.util.List;

/**
 * @author qiaomengnan
 * @ClassName: SysResourceService
 * @Description: 用户菜单
 * @date 2018/1/14
 */
public interface SysResourceService {

    /**
     * @Title:
     * @Description: 根据用户id返回拥有的菜单
     * @param userId
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 12:48:19
     */
    List<SysResource> findSysResByUser(String userId);

    /**
     * @Title:
     * @Description:  查询所有菜单列表, 根据sort资源排序
     * @param
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 03:58:27
     */
    List<SysResourceVo> findSysResAll();

    /**
     * @Title:
     * @Description: 分页查询菜单 并按录入时间倒序
     * @param sysResourceVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 04:31:12
     */
    PageInfoExtend<SysResource> findSysUserByPage(SysResourceVo sysResourceVo);

    /**
     * @Title:
     * @Description: 根据角色id获取角色拥有的资源
     * @param sysRoleId
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 05:15:08
     */
    List<SysResource> findSysResBySysRoleId(String sysRoleId);
}
