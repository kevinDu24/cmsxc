package cn.com.leadu.cmsxc.system.controller;

import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.data.system.repository.SystemUserRepository;
import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserSearchListVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserSearchVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SystemUserVo;
import cn.com.leadu.cmsxc.system.service.SysUserService;
import cn.com.leadu.cmsxc.system.validator.sysuser.vo.SysUserModifyVo;
import cn.com.leadu.cmsxc.system.validator.sysuser.vo.SysUserSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author qiaomengnan
 * @ClassName: SysUserController
 * @Description: 系统用户相关接口
 * @date 2018/1/9
 */
@RestController
@RequestMapping("sys_user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SystemUserRepository systemUserRepository;

    /**
     * @Title:
     * @Description: 分页查询用户信息
     * @param sysUserVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 05:15:08
     */
    @RequestMapping(value = "findSysUserByPage" ,method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findSysUserByPage(SystemUserVo sysUserVo, @AuthUserInfo SystemUser sysUser){
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genSuccessResponse(sysUserService.findSysUserByPage(sysUserVo, sysUser)),
                HttpStatus.OK);
    }
    
    /**
     * @Title:
     * @Description: 保存用户
     * @param sysUserSaveVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 05:42:12
     */
    @RequestMapping(value = "saveSysUser",method = RequestMethod.POST)
    public ResponseEntity<RestResponse> saveSysUser(@Valid @RequestBody SysUserSaveVo sysUserSaveVo, @AuthUserInfo SystemUser sysUser){
        return sysUserService.saveSysUser(sysUserSaveVo, sysUser);
    }

    /**
     * @Title:  
     * @Description:  修改用户
     * @param sysUserModifyVo
     * @return 
     * @throws 
     * @author qiaomengnan 
     * @date 2018/01/11 05:46:05
     */
    @RequestMapping(value = "modifySysUser",method = RequestMethod.PUT)
    public ResponseEntity<RestResponse> modifySysUser(@Valid @RequestBody SysUserModifyVo sysUserModifyVo){
        sysUserService.modifyUser(sysUserModifyVo);
        return new ResponseEntity<RestResponse>(RestResponseGenerator.genSuccessResponse(ResponseEnum.SUCCESS.getMark()), HttpStatus.OK);
    }
    
    /**
     * @Title:  
     * @Description:  删除用户
     * @param id
     * @return 
     * @throws 
     * @author qiaomengnan 
     * @date 2018/01/11 05:47:25
     */
    @RequestMapping(value = "deleteSysUser",method = RequestMethod.DELETE)
    public ResponseEntity<RestResponse> deleteSysUser(String id){
        sysUserService.deleteUser(id);
        return new ResponseEntity<RestResponse>(RestResponseGenerator.genSuccessResponse(ResponseEnum.SUCCESS.getMark()), HttpStatus.OK);
    }

    /**
     * @Title:
     * @Description:  根据id获取用户
     * @param id
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:47:25
     */
    @RequestMapping(value = "findSysUserById", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findSysUserById(String id){
        return new ResponseEntity<RestResponse>(RestResponseGenerator.genSuccessResponse(sysUserService.findSysUserById(id)), HttpStatus.OK);
    }

    /**
     * @Title:
     * @Description:  根据username获取用户
     * @param username
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:47:25
     */
    @RequestMapping(value = "findSysUserByUsername", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findSysUserByUsername(String username){
        return new ResponseEntity<RestResponse>(RestResponseGenerator.genSuccessResponse(sysUserService.findSysUserByUserId(username)), HttpStatus.OK);
    }

    /**
     * @Title:
     * @Description:  获取当前登录用户的详细信息
     * @param sysUser
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 02:26:33
     */
    @RequestMapping(value = "findSysUserDetail", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findSysUserDetail(@AuthUserInfo SystemUser sysUser){
        return new ResponseEntity<RestResponse>(RestResponseGenerator.genSuccessResponse(sysUserService.findSysUserDetail(sysUser)), HttpStatus.OK);
    }

    /**
     * 获取注册账户信息
     *
     * @param sysUserSearchVo 画面信息
     * @param sysUser 用户信息
     * @return
     */
    @RequestMapping(value = "findSysUserListByPage" ,method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findSysUserListByPage(SysUserSearchVo sysUserSearchVo, @AuthUserInfo SystemUser sysUser){
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genSuccessResponse(sysUserService.findSysUserListByPage(sysUserSearchVo, sysUser.getUserRole(),sysUser.getLeaseId())),
                HttpStatus.OK);
    }
    /**
     * 根据委托公司id获取合作收车公司信息
     *
     * @param systemUser 用户信息
     * @return
     */
    @RequestMapping(value = "/getRecoveryCompanys", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getRecoveryCompanys(@AuthUserInfo SystemUser systemUser) {
        try{
            return sysUserService.getRecoveryCompanys(systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            throw new CmsServiceException("获取合作收车公司信息失败");
        }
    }
    /**
     * 获取注册账户信息 --  导出报表用
     *
     * @param sysUserSearchVo 画面信息
     * @param sysUser 用户信息
     * @return
     */
    @RequestMapping(value = "findSysUserList" ,method = RequestMethod.GET)
    public List<SysUserSearchListVo> findSysUserList(SysUserSearchVo sysUserSearchVo, @AuthUserInfo SystemUser sysUser){
        return systemUserRepository.getSysUserListAll(sysUserSearchVo , sysUser.getUserRole(), sysUser.getLeaseId());
    }


}
