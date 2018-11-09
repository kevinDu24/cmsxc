package cn.com.leadu.cmsxc.webclient.system.rpc;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.system.vo.SysRoleDeleteVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SysRoleVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author qiaomengnan
 * @ClassName: SysRoleController
 * @Description: 角色相关接口
 * @date 2018/1/12
 */
@FeignClient("${cmsxc.feigns.serverNames.cmsCAgent}")
public interface SysRoleRpc {

    /**
     * @Title:
     * @Description: 分页查询角色信息
     * @param sysRoleVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 05:15:08
     */
    @RequestMapping(value = "api/system/sys_role/findSysRoleByPage" ,method = RequestMethod.GET)
    ResponseEntity<RestResponse> findSysRoleByPage(@RequestParam Map<String,Object> sysRoleVo);

    /**
     * @Title:
     * @Description: 保存角色
     * @param sysRoleVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 05:42:12
     */
    @RequestMapping(value = "api/system/sys_role/saveSysRole",method = RequestMethod.POST)
    ResponseEntity<RestResponse> saveSysRole(@RequestBody SysRoleVo sysRoleVo);

    /**
     * @Title:
     * @Description:  修改角色
     * @param sysRoleVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:46:05
     */
    @RequestMapping(value = "api/system/sys_role/modifySysRole",method = RequestMethod.PUT)
    ResponseEntity<RestResponse> modifySysRole(@RequestBody SysRoleVo sysRoleVo);

    /**
     * @Title:
     * @Description:  删除角色
     * @param id
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:47:25
     */
    @RequestMapping(value = "api/system/sys_role/deleteSysRole",method = RequestMethod.POST)
    ResponseEntity<RestResponse> deleteSysRole(@RequestBody SysRoleDeleteVo sysRoleDeleteVo);

    /**
     * @Title:
     * @Description:  根据id获取角色
     * @param id
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:47:25
     */
    @RequestMapping(value = "api/system/sys_role/findSysRoleById", method = RequestMethod.GET)
    ResponseEntity<RestResponse> findSysRoleById(@RequestParam("id") String id);

}
