package cn.com.leadu.cmsxc.webclient.system.controller;

import cn.com.leadu.cmsxc.common.exception.CmsRpcException;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserSaveVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserSearchVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserVo;
import cn.com.leadu.cmsxc.webclient.system.rpc.SysUserRpc;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    private SysUserRpc sysUserRpc;

    /**
     * @Title:
     * @Description: 保存用户
     * @param sysUserSaveVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 02:21:22
     */
    @RequestMapping(value="saveSysUser",method = RequestMethod.POST)
    public ResponseEntity<RestResponse<String>> saveSysUser(@RequestBody SysUserSaveVo sysUserSaveVo){
        return sysUserRpc.saveSysUser(sysUserSaveVo);
    }



    /**
     * @Title:
     * @Description: 分页查询用户信息
     * @param sysUserVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 05:15:08
     */
    @RequestMapping(value="findSysUserByPage",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findSysUserByPage(SysUserVo sysUserVo) throws CmsRpcException{
        Map sysUserVoMap = sysUserVo == null?null:(Map)JSON.toJSON(sysUserVo);
        return sysUserRpc.findSysUserByPage(sysUserVoMap);
    }

    /**
     * @Title:
     * @Description:  修改用户
     * @param sysUserVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:46:05
     */
    @RequestMapping(value="modifySysUser",method = RequestMethod.PUT)
    public ResponseEntity<RestResponse> modifySysUser(@RequestBody SysUserVo sysUserVo){
        return sysUserRpc.modifySysUser(sysUserVo);
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
    @RequestMapping(value="deleteSysUser",method = RequestMethod.DELETE)
    public ResponseEntity<RestResponse> deleteSysUser(String id){
        return sysUserRpc.deleteSysUser(id);
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
        return sysUserRpc.findSysUserById(id);
    }

    /**
     * @Title:
     * @Description:  获取当前登录用户的详细信息
     * @param
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 02:26:33
     */
    @RequestMapping(value = "findSysUserDetail", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findSysUserDetail(){
        return sysUserRpc.findSysUserDetail();
    }
    /**
     * @Title:
     * @Description:  获取注册账户信息
     * @param
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 02:26:33
     */
    @RequestMapping(value = "findSysUserListByPage", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findSysUserListByPage(SysUserSearchVo sysUserSearchVo){
        Map sysUserVoMap = sysUserSearchVo == null?null:(Map)JSON.toJSON(sysUserSearchVo);
        return sysUserRpc.findSysUserListByPage(sysUserVoMap);
    }
    /**
     * @Title:
     * @Description:  根据委托公司id获取合作收车公司信息
     * @param
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 02:26:33
     */
    @RequestMapping(value = "getRecoveryCompanys", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getRecoveryCompanys(){
        return sysUserRpc.getRecoveryCompanys();
    }


}
