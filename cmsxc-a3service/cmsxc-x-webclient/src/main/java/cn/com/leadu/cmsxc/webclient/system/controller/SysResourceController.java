package cn.com.leadu.cmsxc.webclient.system.controller;

/**
 * @author qiaomengnan
 * @ClassName: SysResourceController
 * @Description:
 * @date 2018/1/14
 */

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.system.vo.SysResourceVo;
import cn.com.leadu.cmsxc.webclient.system.rpc.SysResourceRpc;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author qiaomengnan
 * @ClassName: SysResourceController
 * @Description:
 * @date 2018/1/14
 */
@RestController
@RequestMapping("sys_resource")
public class SysResourceController {

    @Autowired
    private SysResourceRpc sysResourceRpc;

    /**
     * @Title:
     * @Description: 根据当前登录用户返回用户的菜单
     * @param
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 12:48:19
     */
    @RequestMapping(value = "findSysResByUser",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findSysResByUser(){
        return sysResourceRpc.findSysResByUser();
    }

    /**
     * @Title:
     * @Description:  查询所有菜单列表, 根据sort资源排序
     * @param
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 03:58:27
     */
    @RequestMapping(value = "findSysResAll",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findSysResAll(){
        return sysResourceRpc.findSysResAll();
    }


    /**
     * @Title:
     * @Description: 分页查询菜单信息
     * @param sysResourceVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 05:15:08
     */
    @RequestMapping(value = "findSysResourceByPage" ,method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findSysResourceByPage(SysResourceVo sysResourceVo){
        Map sysUserVoMap = sysResourceVo == null?null:(Map) JSON.toJSON(sysResourceVo);
        return sysResourceRpc.findSysResourceByPage(sysUserVoMap);
    }

    /**
     * @Title:
     * @Description: 根据角色id返回角色的菜单
     * @param
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 12:48:19
     */
    @RequestMapping(value = "findSysResBySysRoleId",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findSysResBySysRoleId(String roleId){
        return sysResourceRpc.findSysResBySysRoleId(roleId);
    }

}
