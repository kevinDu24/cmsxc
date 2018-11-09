package cn.com.leadu.cmsxc.data.system.repository;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.pojo.system.entity.SysRole;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author qiaomengnan
 * @ClassName: SysRoleRepository
 * @Description: 角色Repository
 * @date 2018/1/12
 */
public interface SysRoleRepository {

    /**
     * @Title:
     * @Description: 保存角色信息
     * @param sysRole
     * @return String
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 10:26:22
     */
    String insertOne(SysRole sysRole);

    /**
     * @Title:
     * @Description:  通过条件批量查询
     * @param example
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 04:23:21
     */
    List<SysRole> selectByExampleList(Example example);

    /**
     * @Title:
     * @Description: 分页查询
     * @param example,pageQuery
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 04:49:17
     */
    PageInfoExtend<SysRole> selectListByExamplePageInfo(Example example, PageQuery pageQuery);

    /**
     * @Title:
     * @Description:  更新角色
     * @param sysRole
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:53:16
     */
    void updateByPrimaryKeySelective(SysRole sysRole);

    /**
     * @Title:
     * @Description:  删除角色
     * @param id
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:53:28
     */
    void delete(Object id);


    /**
     * @Title:
     * @Description:  根据id获取角色
     * @param id
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 06:31:34
     */
    SysRole selectByPrimaryKey(Object id);


    /**
     * @Title:
     * @Description:  根据用户id获取对应的角色id集合
     * @param userId
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 01:00:27
     */
    List<String> selectSysRoleIdsBySysUserId(String userId);


}
