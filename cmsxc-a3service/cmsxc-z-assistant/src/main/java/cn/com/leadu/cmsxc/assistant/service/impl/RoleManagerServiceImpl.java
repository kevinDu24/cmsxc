package cn.com.leadu.cmsxc.assistant.service.impl;

import cn.com.leadu.cmsxc.assistant.service.RoleManagerService;
import cn.com.leadu.cmsxc.assistant.service.SystemUserService;
import cn.com.leadu.cmsxc.assistant.util.constant.EncodeUtils;
import cn.com.leadu.cmsxc.common.constant.Constants;
import cn.com.leadu.cmsxc.common.util.ArrayUtil;
import cn.com.leadu.cmsxc.data.appuser.repository.AuditorAreaRepository;
import cn.com.leadu.cmsxc.data.system.repository.SysUserRoleRepository;
import cn.com.leadu.cmsxc.data.system.repository.SystemUserRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appuser.entity.AuditorArea;
import cn.com.leadu.cmsxc.pojo.assistant.vo.EditAuditorVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.UserRoleListVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SysUserRole;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 角色管理Service实现类
 */
@Service
public class RoleManagerServiceImpl implements RoleManagerService {
    private static final Logger logger = LoggerFactory.getLogger(RoleManagerServiceImpl.class);

    @Autowired
    private SystemUserRepository systemUserRepository;
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;
    @Autowired
    private AuditorAreaRepository auditorAreaRepository;


    /**
     * 获取角色列表
     * @param leaseId 用户id
     * @return
     */
    public ResponseEntity<RestResponse> getUserList(String leaseId){
        List<UserRoleListVo> resultList =  systemUserRepository.selectUserList(leaseId);
        if(resultList == null || resultList.isEmpty()){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","数据加载完毕"),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, resultList,""),
                HttpStatus.OK);
    }

    /**
     * 获取已选择的省份集合
     * @param id 用户表主键
     * @return
     */
    public ResponseEntity<RestResponse> getProvinces(String id){
        List<String> resultList =  auditorAreaRepository.selectProvinces(id);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, resultList,""),
                HttpStatus.OK);
    }

    /**
     * 新增审核人员
     * @param vo 审核人员信息参数
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> addAuditor(EditAuditorVo vo, SystemUser loginUser){
        // 打印新添加审核人员的姓名，查看姓名是否正确
        logger.info("打印新添加审核人员的姓名",vo.getUserName());
        SystemUser systemUser = systemUserService.selectSystemUserByUserId(vo.getUserId());
        if(systemUser != null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该用户已存在"),
                    HttpStatus.OK);
        }
        SystemUser user = new SystemUser();
        Date now = new Date();
        //登录用户表
        user.setUserId(vo.getUserId()); //用户名
        user.setUserPassword(EncodeUtils.MD5(EncodeUtils.getBase64(Constants.ORIGIN_PASSWORD))); //初始密码进行加密
        user.setUserRole(vo.getUserRole()); //角色code
        user.setUserName(vo.getUserName()); //用户姓名
        user.setUserPhone(vo.getUserId()); //用户手机号
        user.setEnableFlag(vo.getEnableFlag()); //禁用/启用状态
        user.setLeaseId(loginUser.getLeaseId()); //委托公司id
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setCreator(loginUser.getUserId());
        user.setUpdater(loginUser.getUserId());
        logger.info("打印存入DB新添加审核人员的姓名",user.getUserName());
        //登录用户角色表
        user = systemUserRepository.insertOne(user);
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(user.getId());
        sysUserRole.setRoleId(user.getUserRole());
        sysUserRoleRepository.insertOne(sysUserRole);
        //登录审核人员片区表
        List<String> provinces = vo.getProvinces();
        //如果所选省份不为空，则登录审核人员片区表
        if(ArrayUtil.isNotNullAndLengthNotZero(provinces)){
            List<AuditorArea> auditorAreas = new ArrayList();
            AuditorArea area;
            for(String item : provinces){
                area = new AuditorArea();
                area.setUserId(user.getId()); //用户表主键
                area.setProvince(item); //省份名称
                auditorAreas.add(area);
            }
            auditorAreaRepository.insertMore(auditorAreas);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, "","添加成功"),
                HttpStatus.OK);
    }

    /**
     * 编辑审核人员
     * @param vo 审核人员信息参数
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> editAuditor(EditAuditorVo vo, SystemUser loginUser){
        SystemUser user = systemUserService.selectSystemUserByUserId(vo.getUserId());
        if(user == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该用户不存在"),
                    HttpStatus.OK);
        }
        Date now = new Date();
        //更新用户表
        user.setUserName(vo.getUserName()); //用户姓名
        user.setEnableFlag(vo.getEnableFlag()); //禁用/启用状态
        user.setUpdateTime(now);
        user.setUpdater(loginUser.getUserId());
        systemUserRepository.updateByPrimaryKey(user);
        //登录审核人员片区表
        List<String> provinces = vo.getProvinces();
        //如果所选省份不为空，则操作审核人员片区表
        if(ArrayUtil.isNotNullAndLengthNotZero(provinces)){
            //首先删除之前的片区信息
            auditorAreaRepository.deleteByUserKey(user.getId());
            //重新登陆审核人员片区表
            List<AuditorArea> auditorAreas = new ArrayList();
            AuditorArea area;
            for(String item : provinces){
                area = new AuditorArea();
                area.setUserId(user.getId()); //用户表主键
                area.setProvince(item); //省份名称
                auditorAreas.add(area);
            }
            auditorAreaRepository.insertMore(auditorAreas);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, "","编辑成功"),
                HttpStatus.OK);
    }
    /**
     * 禁用接口
     * @param disableUserId 被禁用审核人员账号
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> disableAuditor( SystemUser loginUser,String disableUserId){
        SystemUser user = systemUserService.selectSystemUserByUserId(disableUserId);
        if(user == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该用户不存在"),
                    HttpStatus.OK);
        }
        Date now = new Date();
        //更新用户表
        user.setEnableFlag("0"); //禁用/启用状态
        user.setUpdateTime(now);
        user.setUpdater(loginUser.getUserId());
        systemUserRepository.updateByPrimaryKey(user);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, "","禁用成功"),
                HttpStatus.OK);
    }

}
