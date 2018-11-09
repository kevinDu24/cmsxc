package cn.com.leadu.cmsxc.system.service;

import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.system.entity.SysRole;
import cn.com.leadu.cmsxc.pojo.system.vo.SysRoleDeleteVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SysRoleVo;
import cn.com.leadu.cmsxc.system.validator.sysrole.vo.SysRoleModifyVo;
import cn.com.leadu.cmsxc.system.validator.sysrole.vo.SysRoleSaveVo;
import org.springframework.http.ResponseEntity;

/**
 * @author qiaomengnan
 * @ClassName: SysRoleService
 * @Description: 角色业务层
 * @date 2018/1/12
 */
public interface SysRoleService {

    /**
     * @Title:
     * @Description: 保存角色
     * @param sysRoleSaveVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 10:17:25
     */
    ResponseEntity<RestResponse> saveSysRole(SysRoleSaveVo sysRoleSaveVo);


    /**
     * @Title:
     * @Description: 根据角色名分页查询角色,按照新增时间分页
     * @param sysRoleVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 04:31:12
     */
    PageInfoExtend<SysRole> findSysRoleByPage(SysRoleVo sysRoleVo);


    /**
     * @Title:
     * @Description: 修改角色
     * @param sysRoleModifyVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:50:35
     */
    void modifySysRole(SysRoleModifyVo sysRoleModifyVo);

    /**
     * @Title:
     * @Description:  通过id删除角色
     * @param sysRoleDeleteVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:55:21
     */
    void deleteSysRole(SysRoleDeleteVo sysRoleDeleteVo);

    /**
     * @Title:
     * @Description:  根据id获取角色
     * @param id
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 06:32:58
     */
    SysRole findSysRoleById(String id);

}
