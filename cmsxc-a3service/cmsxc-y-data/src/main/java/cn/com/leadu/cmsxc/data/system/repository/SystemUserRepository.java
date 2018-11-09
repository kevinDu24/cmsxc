package cn.com.leadu.cmsxc.data.system.repository;


import cn.com.leadu.cmsxc.common.entity.PageQuery;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.pojo.appuser.vo.ScanInfoVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.UserRoleListVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserSearchListVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserSearchVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SystemUserVo;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/15.
 *
 * 用户信息
 */
public interface SystemUserRepository {
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    List<SystemUser> selectByExampleList(Example example);
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    SystemUser selectOneByExample(Example example);
    /**
     * 登录用户信息
     * @param sysUser
     */
    SystemUser insertOne(SystemUser sysUser);

    /**
     * 根据主键更新表
     * @param sysUser
     */
    void updateByPrimaryKey(SystemUser sysUser);

    /**
     * @Title:
     * @Description: 分页查询
     * @param sysUserVo,pageQuery
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 04:49:17
     */
    PageInfoExtend findSysUserByPage(SystemUserVo sysUserVo, PageQuery pageQuery, SystemUser sysUser);

    /**
     * @Title:
     * @Description:  更新用户
     * @param sysUser
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:53:16
     */
    void updateByPrimaryKeySelective(SystemUser sysUser);

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
    SystemUser selectByPrimaryKey(Object id);
    /**
     *  获取注册账户信息
     *
     * @param sysUserSearchVo 画面信息
     * @param role 用户角色
     * @param leaseId 委托公司id
     * @param pageQuery 分页信息
     * @return
     */
    PageInfoExtend<SysUserSearchListVo> findSysUserListByPage(SysUserSearchVo sysUserSearchVo, String role, String leaseId , PageQuery pageQuery);
    /**
     * 获取所有注册账户信息--- 导出报表用
     *
     * @param sysUserSearchVo  画面信息
     * @param role   用户角色
     * @param leaseId  委托公司id
     * @return
     */
    List<SysUserSearchListVo> getSysUserListAll(SysUserSearchVo sysUserSearchVo, String role, String leaseId);

    /**
     * 获取角色列表
     *
     * @param leaseId 委托公司id
     * @return
     */
    List<UserRoleListVo> selectUserList(String leaseId);

    /**
     * 根据委托公司id及工单所在省份，获取审核人员信息
     *
     * @param leaseId 委托公司id
     * @param taskId 任务id
     * @return
     */
    List<SystemUser> selectLeaseAdminUserList(String leaseId, String taskId);

    /**
     * 根据业务员用户名获取其手机号、姓名、所在收车公司
     *
     * @param userId 用户名
     * @return
     */
    public ScanInfoVo selectInfoByUserId(String userId);
}
