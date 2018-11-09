package cn.com.leadu.cmsxc.system.service;

import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import cn.com.leadu.cmsxc.pojo.system.entity.SysUser;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserSearchListVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserSearchVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SystemUserVo;
import cn.com.leadu.cmsxc.system.validator.sysuser.vo.SysUserModifyVo;
import cn.com.leadu.cmsxc.system.validator.sysuser.vo.SysUserSaveVo;
import org.springframework.http.ResponseEntity;

/**
 * @author qiaomengnan
 * @ClassName: SysUserService
 * @Description: 用户业务层
 * @date 2018/1/12
 */
public interface SysUserService {

    /**
     * @Title:
     * @Description: 保存用户
     * @param sysUserSaveVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 10:17:25
     */
    ResponseEntity<RestResponse> saveSysUser(SysUserSaveVo sysUserSaveVo, SystemUser sysUser);


    /**
     * @Title:
     * @Description: 根据用户名,手机号分页查询用户
     * @param sysUserVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 04:31:12
     */
    PageInfoExtend<SystemUserVo> findSysUserByPage(SystemUserVo sysUserVo, SystemUser sysUser);


    /**
     * @Title:
     * @Description: 修改用户
     * @param sysUserModifyVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:50:35
     */
    void modifyUser(SysUserModifyVo sysUserModifyVo);

    /**
     * @Title:
     * @Description:  通过id删除用户
     * @param id
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:55:21
     */
    void deleteUser(String id);

    /**
     * @Title:
     * @Description:  根据id获取用户
     * @param id
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 06:32:58
     */
    SysUser findSysUserById(String id);

    /**
     * @Title:
     * @Description:  根据用户名获取用户
     * @param username
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/12 12:37:31
     */
    SystemUser findSysUserByUserId(String username);


    /**
     * @Title:
     * @Description:  获取用户的详细信息,包括拥有的菜单等等
     * @param sysUser
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 02:26:33
     */
    SystemUserVo findSysUserDetail(SystemUser sysUser);
    /**
     *  获取注册账户信息
     *
     * @param sysUserSearchVo 画面信息
     * @param role 用户角色
     * @param leaseId 委托公司id
     * @return
     */
    PageInfoExtend<SysUserSearchListVo> findSysUserListByPage(SysUserSearchVo sysUserSearchVo, String role, String leaseId);

    /**
     * 根据委托公司id获取合作收车公司信息
     *
     * @param systemUser 用户信息
     * @return
     */
    ResponseEntity<RestResponse> getRecoveryCompanys(SystemUser systemUser);

}
