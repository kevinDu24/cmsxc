package cn.com.leadu.cmsxc.data.system.repository;


import cn.com.leadu.cmsxc.common.entity.PageQuery;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.pojo.system.entity.SysUser;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author qiaomengnan
 * @ClassName: SysUserRepository
 * @Description: 系统用户数据操作层
 * @date 2018/1/9
 */

public interface SysUserRepository {

    /**
     * @Title:
     * @Description: 保存用户信息
     * @param sysUser
     * @return String
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 10:26:22
     */
    String insertOne(SysUser sysUser);

    /**
     * @Title:
     * @Description:  通过条件批量查询
     * @param example
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 04:23:21
     */
    List<SysUser> selectByExampleList(Example example);

    /**
     * @Title:
     * @Description: 分页查询
     * @param example,pageQuery
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 04:49:17
     */
    PageInfoExtend selectListByExamplePageInfo(Example example, PageQuery pageQuery);

    /**
     * @Title:
     * @Description:  更新用户
     * @param sysUser
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:53:16
     */
    void updateByPrimaryKeySelective(SysUser sysUser);

    /**
     * @Title:
     * @Description:  删除用户
     * @param id
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:53:28
     */
    void delete(Object id);


    /**
     * @Title:
     * @Description:  根据id获取用户
     * @param id
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 06:31:34
     */
    SysUser selectByPrimaryKey(Object id);


}
