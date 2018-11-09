package cn.com.leadu.cmsxc.system.controller;

import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.system.vo.SysRoleDeleteVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SysRoleVo;
import cn.com.leadu.cmsxc.system.service.SysRoleService;
import cn.com.leadu.cmsxc.system.validator.sysrole.vo.SysRoleModifyVo;
import cn.com.leadu.cmsxc.system.validator.sysrole.vo.SysRoleSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author qiaomengnan
 * @ClassName: SysRoleController
 * @Description: 角色相关接口
 * @date 2018/1/12
 */
@RestController
@RequestMapping("sys_role")
public class SysRoleController {

    /**
     * @Fields  : 角色service
     */
    @Autowired
    private SysRoleService sysRoleService;


    /**
     * @Title:
     * @Description: 分页查询角色信息
     * @param sysRoleVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 05:15:08
     */
    @RequestMapping(value = "findSysRoleByPage" ,method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findSysRoleByPage(SysRoleVo sysRoleVo){
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genSuccessResponse(sysRoleService.findSysRoleByPage(sysRoleVo)),
                HttpStatus.OK);
    }

    /**
     * @Title:
     * @Description: 保存角色
     * @param sysRoleSaveVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 05:42:12
     */
    @RequestMapping(value = "saveSysRole",method = RequestMethod.POST)
    public ResponseEntity<RestResponse> saveSysRole(@Valid @RequestBody SysRoleSaveVo sysRoleSaveVo){
        return sysRoleService.saveSysRole(sysRoleSaveVo);
    }

    /**
     * @Title:
     * @Description:  修改角色
     * @param sysRoleModifyVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:46:05
     */
    @RequestMapping(value = "modifySysRole",method = RequestMethod.PUT)
    public ResponseEntity<RestResponse> modifySysRole(@Valid @RequestBody SysRoleModifyVo sysRoleModifyVo){
        sysRoleService.modifySysRole(sysRoleModifyVo);
        return new ResponseEntity<RestResponse>(RestResponseGenerator.genSuccessResponse(ResponseEnum.SUCCESS.getMark()), HttpStatus.OK);
    }

    /**
     * @Title:
     * @Description:  删除角色
     * @param sysRoleDeleteVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:47:25
     */
    @RequestMapping(value = "deleteSysRole",method = RequestMethod.POST)
    public ResponseEntity<RestResponse> deleteSysRole(@RequestBody SysRoleDeleteVo sysRoleDeleteVo){
        sysRoleService.deleteSysRole(sysRoleDeleteVo);
        return new ResponseEntity<RestResponse>(RestResponseGenerator.genSuccessResponse(ResponseEnum.SUCCESS.getMark()), HttpStatus.OK);
    }

    /**
     * @Title:
     * @Description:  根据id获取角色
     * @param id
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:47:25
     */
    @RequestMapping(value = "findSysRoleById", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findSysRoleById(String id){
        return new ResponseEntity<RestResponse>(RestResponseGenerator.genSuccessResponse(sysRoleService.findSysRoleById(id)), HttpStatus.OK);
    }




}
