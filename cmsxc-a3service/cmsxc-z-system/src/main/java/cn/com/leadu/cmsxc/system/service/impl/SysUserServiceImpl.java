package cn.com.leadu.cmsxc.system.service.impl;

import cn.com.leadu.cmsxc.common.constant.enums.UserRoleEnums;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.appbusiness.repository.RecoveryCompanyRepository;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.data.system.repository.SysUserRoleRepository;
import cn.com.leadu.cmsxc.data.system.repository.SystemUserRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.assistant.vo.RecoveryCompanysVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SysUser;
import cn.com.leadu.cmsxc.pojo.system.entity.SysUserRole;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserSearchListVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserSearchVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SystemUserVo;
import cn.com.leadu.cmsxc.system.service.SysResourceService;
import cn.com.leadu.cmsxc.system.service.SysUserService;
import cn.com.leadu.cmsxc.system.util.constant.EncodeUtils;
import cn.com.leadu.cmsxc.system.validator.sysuser.vo.SysUserModifyVo;
import cn.com.leadu.cmsxc.system.validator.sysuser.vo.SysUserSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author qiaomengnan
 * @ClassName: SysUserServiceImpl
 * @Description: 用户业务层
 * @date 2018/1/12
 */
@Component
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SystemUserRepository sysUserRepository;

    @Autowired
    private SysResourceService sysResourceService;

    @Autowired
    private RecoveryCompanyRepository recoveryCompanyRepository;
    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;

    /**
     * @Title:
     * @Description: 保存用户
     * @param sysUservo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 10:17:25
     */
    @Transactional
    public ResponseEntity<RestResponse> saveSysUser(SysUserSaveVo sysUservo, SystemUser sysUser) {
        if(UserRoleEnums.LEASE_AUDITOR.getType().equals(sysUservo.getUserRole())){
            // 根据委托公司id和角色获取此公司的审核人员，判断审核人员是否已存在
            Example example = new Example(SystemUser.class);
            Example.Criteria criteria = example.createCriteria();
            // 判断委托公司id
            String leaseId;
            if(UserRoleEnums.ADMIN.getType().equals(sysUser.getUserRole())){
                //如果是超级管理员，则为画面勾选值
                leaseId = sysUservo.getLeaseId();
            } else {
                //如果不是超级管理员，则和当前登录者所在委托公司相同
                leaseId = sysUser.getLeaseId();
            }
            criteria.andEqualTo("leaseId", leaseId);
            criteria.andEqualTo("userRole", UserRoleEnums.LEASE_AUDITOR.getType());
            SystemUser user = sysUserRepository.selectOneByExample(example);
            if(user != null){
                return new ResponseEntity<RestResponse>(
                        RestResponseGenerator.genResponse(ResponseEnum.FAILURE, "", "此委托公司审核人员已存在，不可重复添加！"),
                        HttpStatus.OK);
            }
        }
        SystemUser systemUser = sysUservo.getSysUser();
        //当前委托公司只能新增自己公司的用户，不是超级管理员新增用户的情况
        if(StringUtil.isNotNull(sysUser.getLeaseId())){
            systemUser.setLeaseId(sysUser.getLeaseId());
        }
        systemUser.setUserPassword(EncodeUtils.MD5(EncodeUtils.getBase64(sysUservo.getUserPassword())));
        sysUserRepository.insertOne(systemUser);

        //插入用户角色表
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(systemUser.getId());
        sysUserRole.setRoleId(sysUservo.getUserRole());
        sysUserRoleRepository.insertOne(sysUserRole);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, "", "添加成功"),
                HttpStatus.OK);
    }


    /**
     * @Title:
     * @Description: 根据用户名,手机号，角色分页查询用户 并按录入时间倒序
     * @param sysUserVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 04:31:12
     */
    public PageInfoExtend<SystemUserVo> findSysUserByPage(SystemUserVo sysUserVo,SystemUser sysUser){
        PageInfoExtend<SystemUserVo> pageInfo = sysUserRepository.findSysUserByPage(sysUserVo, sysUserVo.getPageQuery(), sysUser);
        return pageInfo;
    }

    /**
     * @Title:
     * @Description: 修改用户
     * @param sysUservo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:50:35
     */
    @Override
    public void modifyUser(SysUserModifyVo sysUservo) {
//        SysUser sysUser = sysUservo.getSysUser();
//        sysUserRepository.updateByPrimaryKeySelective(sysUser);
    }

    /**
     * @Title:
     * @Description:  通过id删除用户
     * @param id
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:55:21
     */
    @Override
    public void deleteUser(String id) {
//        sysUserRepository.delete(id);
    }

    /**
     * @Title:
     * @Description:  根据id获取用户
     * @param id
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 06:32:58
     */
    public SysUser findSysUserById(String id){
//        return sysUserRepository.selectByPrimaryKey(id);
        return null;
    }


    /**
     * @Title:
     * @Description:  根据用户名获取用户
     * @param username
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/12 12:37:31
     */
    public SystemUser findSysUserByUserId(String username){
        if(StringUtil.isNotNull(username)) {
            Example example = new Example(SystemUser.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId", username);
            return sysUserRepository.selectOneByExample(example);
        }
        return null;
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
    public SystemUserVo findSysUserDetail(SystemUser sysUser){
        SystemUserVo sysUserVo = new SystemUserVo();
        sysUserVo.setUserId(sysUser.getUserId());
        sysUserVo.setPassword(sysUser.getUserPassword());
        sysUserVo.setUserRole(sysUser.getUserRole());
        sysUserVo.setLeaseId(sysUser.getLeaseId());
        sysUserVo.setRecoveryCompanyId(sysUser.getRecoveryCompanyId());
        sysUserVo.setResources(sysResourceService.findSysResByUser(sysUser.getId()));
        return sysUserVo;
    }
    /**
     *  获取注册账户信息
     *
     * @param sysUserSearchVo 画面信息
     * @param role 用户角色
     * @param leaseId 委托公司id
     * @return
     */
    public PageInfoExtend<SysUserSearchListVo> findSysUserListByPage(SysUserSearchVo sysUserSearchVo, String role, String leaseId){
        PageInfoExtend<SysUserSearchListVo> pageInfo = sysUserRepository.findSysUserListByPage(sysUserSearchVo, role, leaseId, sysUserSearchVo.getPageQuery());
        return pageInfo;
    }
    /**
     * 根据委托公司id获取合作收车公司信息
     *
     * @param systemUser 用户信息
     * @return
     */
    public ResponseEntity<RestResponse> getRecoveryCompanys(SystemUser systemUser) {
        List<RecoveryCompanysVo> recoveryCompanysVoList = recoveryCompanyRepository.selectRecoveryCompanysByLeaseId(systemUser.getLeaseId());
        if(UserRoleEnums.ADMIN.getType().equals(systemUser.getUserRole())){
            // 所有合作的收车公司 + 其他
            RecoveryCompanysVo vo = new RecoveryCompanysVo();
            vo.setRecoveryCompanyId("000000");
            vo.setRecoveryCompanyName("其他");
            recoveryCompanysVoList.add(vo);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, recoveryCompanysVoList, ""),
                HttpStatus.OK);
    }
}
