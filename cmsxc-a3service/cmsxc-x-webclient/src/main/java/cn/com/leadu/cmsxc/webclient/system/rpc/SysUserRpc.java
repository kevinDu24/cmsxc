package cn.com.leadu.cmsxc.webclient.system.rpc;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserSaveVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserSearchListVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserSearchVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author qiaomengnan
 * @ClassName: SysUserRpc
 * @Description: 系统用户远程Rpc调用
 * @date 2018/1/9
 */
@FeignClient("${cmsxc.feigns.serverNames.cmsCAgent}")
public interface SysUserRpc {

    /**
     * @Title:
     * @Description: 远程调用注册用户
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 11:26:47
     */
    @RequestMapping(value = "api/system/sys_user/saveSysUser" , method = RequestMethod.POST)
    ResponseEntity<RestResponse<String>> saveSysUser(@RequestBody SysUserSaveVo sysUserSaveVo);

    /**
     * @Title:
     * @Description: 分页查询用户信息
     * @param sysUserVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 05:15:08
     */
    @RequestMapping(value = "api/system/sys_user/findSysUserByPage" , method = RequestMethod.GET)
    ResponseEntity<RestResponse> findSysUserByPage(@RequestParam Map<String,Object> sysUserVo);

    /**
     * @Title:
     * @Description:  修改用户
     * @param sysUserVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:46:05
     */
    @RequestMapping(value = "api/system/sys_user/modifySysUser" , method = RequestMethod.PUT)
    ResponseEntity<RestResponse> modifySysUser(@RequestBody SysUserVo sysUserVo);

    /**
     * @Title:
     * @Description:  删除用户
     * @param id
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:47:25
     */
    @RequestMapping(value = "api/system/sys_user/deleteSysUser" , method = RequestMethod.DELETE)
    ResponseEntity<RestResponse> deleteSysUser(@RequestParam("id") String id);

    /**
     * @Title:
     * @Description:  根据id获取用户
     * @param id
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:47:25
     */
    @RequestMapping(value = "api/system/sys_user/findSysUserById", method = RequestMethod.GET)
    ResponseEntity<RestResponse> findSysUserById(@RequestParam("id") String id);


    /**
     * @Title:
     * @Description:  获取当前登录用户的详细信息
     * @param
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 02:26:33
     */
    @RequestMapping(value = "api/system/sys_user/findSysUserDetail", method = RequestMethod.GET)
    ResponseEntity<RestResponse> findSysUserDetail();
    /**
     * @Title:
     * @Description:  获取注册账户信息
     * @param
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 02:26:33
     */
    @RequestMapping(value = "api/system/sys_user/findSysUserListByPage", method = RequestMethod.GET)
    ResponseEntity<RestResponse> findSysUserListByPage(@RequestParam Map<String,Object> sysUserSearchVo);
    /**
     * @Title:
     * @Description:  根据委托公司id获取合作收车公司信息
     * @param
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 02:26:33
     */
    @RequestMapping(value = "api/system/sys_user/getRecoveryCompanys", method = RequestMethod.GET)
    ResponseEntity<RestResponse> getRecoveryCompanys();

    /**
     * @Title:
     * @Description: 导出注册账户信息
     * @param
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 02:26:33
     */
    @RequestMapping(value = "api/system/sys_user/findSysUserList", method = RequestMethod.GET)
    List<SysUserSearchListVo> findSysUserList(@RequestParam Map<String,Object> sysUserSearchVo);


}
