package cn.com.leadu.cmsxc.webclient.system.rpc;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author qiaomengnan
 * @ClassName: SysResourceRpc
 * @Description:
 * @date 2018/1/14
 */
@FeignClient("${cmsxc.feigns.serverNames.cmsCAgent}")
public interface SysResourceRpc {

    /**
     * @Title:
     * @Description: 根据当前登录用户返回用户的菜单
     * @param
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 12:48:19
     */
    @RequestMapping(value = "api/system/sys_resource/findSysResByUser",method = RequestMethod.GET)
    ResponseEntity<RestResponse> findSysResByUser();

    /**
     * @Title:
     * @Description:  查询所有菜单列表, 根据sort资源排序
     * @param
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 03:58:27
     */
    @RequestMapping(value = "api/system/sys_resource/findSysResAll",method = RequestMethod.GET)
    ResponseEntity<RestResponse> findSysResAll();


    /**
     * @Title:
     * @Description: 分页查询菜单信息
     * @param sysResourceVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 05:15:08
     */
    @RequestMapping(value = "api/system/sys_resource/findSysResourceByPage" ,method = RequestMethod.GET)
    ResponseEntity<RestResponse> findSysResourceByPage(@RequestParam Map<String,Object> sysResourceVo);

    /**
     * @Title:
     * @Description: 根据角色id返回角色的菜单
     * @param
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 12:48:19
     */
    @RequestMapping(value = "api/system/sys_resource/findSysResBySysRoleId",method = RequestMethod.GET)
    ResponseEntity<RestResponse> findSysResBySysRoleId(@RequestParam("roleId") String roleId);

}
