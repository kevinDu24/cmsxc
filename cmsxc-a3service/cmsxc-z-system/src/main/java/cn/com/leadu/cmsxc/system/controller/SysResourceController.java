package cn.com.leadu.cmsxc.system.controller;

import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.system.entity.SysUser;
import cn.com.leadu.cmsxc.pojo.system.vo.SysResourceVo;
import cn.com.leadu.cmsxc.system.service.SysResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
    private SysResourceService sysResourceService;

    /**
     * @Title:
     * @Description:  根据用户名提供用户的资源,此处提供给鉴权使用
     * @param username
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 12:42:08
     */
    @RequestMapping(value = "findSysResByUsername",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findSysResByUsername(String username){
        List<String> res =  new ArrayList<>();
        res.add("/api/**");
        return new ResponseEntity<RestResponse>(RestResponseGenerator.genSuccessResponse(res), HttpStatus.OK);
    }

    /**
     * @Title:
     * @Description: 根据当前登录用户返回用户的菜单
     * @param sysUser
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 12:48:19
     */
    @RequestMapping(value = "findSysResByUser",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findSysResByUser(@AuthUserInfo SysUser sysUser){
        return new ResponseEntity<RestResponse>(RestResponseGenerator.genSuccessResponse(sysResourceService.findSysResByUser(sysUser.getId())), HttpStatus.OK);
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
        return new ResponseEntity<RestResponse>(RestResponseGenerator.genSuccessResponse(sysResourceService.findSysResAll()), HttpStatus.OK);
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
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genSuccessResponse(sysResourceService.findSysUserByPage(sysResourceVo)),
                HttpStatus.OK);
    }


    /**
     * @Title:
     * @Description: 根据角色id获取角色拥有的资源
     * @param roleId
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 05:15:08
     */
    @RequestMapping(value = "findSysResBySysRoleId" ,method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findSysResBySysRoleId(String roleId){
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genSuccessResponse(sysResourceService.findSysResBySysRoleId(roleId)),
                HttpStatus.OK);
    }




}
