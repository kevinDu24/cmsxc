package cn.com.leadu.cmsxc.system.service.impl;

import cn.com.leadu.cmsxc.common.constant.enums.UserRoleEnums;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.appbusiness.repository.LeaseCompanyRepository;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.data.system.repository.SysUserRoleRepository;
import cn.com.leadu.cmsxc.data.system.repository.SystemUserRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.LeaseCompany;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.LeaseCompanyVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.LeaseUserListVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SysUserRole;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import cn.com.leadu.cmsxc.system.service.LeaseUserService;
import cn.com.leadu.cmsxc.system.service.SysUserService;
import cn.com.leadu.cmsxc.system.util.constant.EncodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/2/2.
 *
 * 委托公司用户管理实现类
 */
@Service
public class LeaseUserServiceImpl implements LeaseUserService{
    @Autowired
    private LeaseCompanyRepository leaseCompanyRepository;
    @Autowired
    private SystemUserRepository systemUserRepository;
    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 委托公司用户注册
     *
     * @param  leaseCompanyVo 委托公司用户注册信息
     * @return
     */
    @Transactional
   public ResponseEntity<RestResponse> register(LeaseCompanyVo leaseCompanyVo){
        // 根据委托公司简称获取公司信息
        LeaseCompany leaseCompany = findByName(leaseCompanyVo.getFullName());
        // 如果委托公司已经存在，返回“委托公司已存在！”
        if(leaseCompany != null ){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","委托公司已存在"),
                    HttpStatus.OK);
        }
        // 如果用户名已经存在，返回“用户名已存在！”
        if(sysUserService.findSysUserByUserId(leaseCompanyVo.getName()) != null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","用户名已存在"),
                    HttpStatus.OK);
        }
        // 如果委托公司不存在，将注册信息登录到委托公司表中
        LeaseCompany newLeaseCompany = new LeaseCompany();
        // 委托公司简称
        newLeaseCompany.setLeaseShortName(leaseCompanyVo.getShortName());
        // 委托公司全称
        newLeaseCompany.setLeaseFullName(leaseCompanyVo.getFullName());
        // 联系人姓名
        newLeaseCompany.setContactName(leaseCompanyVo.getContactName());
        // 联系人电话
        newLeaseCompany.setContactPhone(leaseCompanyVo.getContactPhone());
        newLeaseCompany = leaseCompanyRepository.insertOne(newLeaseCompany);

        //在用户表中插入该委托公司的一级管理员
        SystemUser systemUser = new SystemUser();
        systemUser.setLeaseId(newLeaseCompany.getId());
        systemUser.setUserId(leaseCompanyVo.getName());
        systemUser.setUserPassword(EncodeUtils.MD5(EncodeUtils.getBase64(leaseCompanyVo.getPassword())));
        systemUser.setUserName(leaseCompanyVo.getFullName());
        systemUser.setUserRole(UserRoleEnums.LEASE_ADMIN.getType());
        systemUser.setUserPhone(leaseCompanyVo.getContactPhone());
        systemUser = systemUserRepository.insertOne(systemUser);
        //插入用户角色表
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(systemUser.getId());
        sysUserRole.setRoleId(UserRoleEnums.LEASE_ADMIN.getType());
        sysUserRoleRepository.insertOne(sysUserRole);

        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","注册成功"),
                HttpStatus.OK);
    }
    /**
     * 根据委托公司简称或全称，分页获取委托公司信息
     *
     * @param leaseUserListVo 画面参数
     * @return
    */
    public  ResponseEntity<RestResponse> findLeaseUserByPage(LeaseUserListVo leaseUserListVo){
        PageInfoExtend<LeaseCompany> leaseCompanyList = leaseCompanyRepository.selectLeaseCompany( leaseUserListVo, leaseUserListVo.getPageQuery());
        return new ResponseEntity<RestResponse>(
              RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,leaseCompanyList,""),
              HttpStatus.OK);
    }
    /**
     * 获取委托公司信息
     *
     * @return
     */
    public  ResponseEntity<RestResponse> getLeaseUser(){
        Example example = new Example(LeaseCompany.class);
        example.setOrderByClause(" create_time asc ");
        List<LeaseCompany> leaseCompanyList = leaseCompanyRepository.selectByExampleList(example);
        if(leaseCompanyList != null && !leaseCompanyList.isEmpty()){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,leaseCompanyList,""),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未获取到金融公司信息"),
                HttpStatus.OK);
    }
    /**
     * 根据委托公司全称获取委托公司信息
     *
     * @param name 用户名
     * @return
     */
    public LeaseCompany findByName(String name){
        if(StringUtil.isNotNull(name)) {
            Example example = new Example(LeaseCompany.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("leaseFullName", name);
            return leaseCompanyRepository.selectOneByExample(example);
        }
        return null;
    }

    /**
     * 根据工单表中的金融公司用户名获取委托公司信息
     *
     * @pame 用户名
     * @return
     */
    public LeaseCompany findByUserName(String userName){
        if(StringUtil.isNotNull(userName)) {
            SystemUser systemUser = sysUserService.findSysUserByUserId(userName);
            if(systemUser == null || StringUtil.isNull(systemUser.getLeaseId())){
                return null;
            }
            //获得对应的金融公司信息
            return leaseCompanyRepository.selectByPrimaryKey(systemUser.getLeaseId());
        }
        return null;
    }
    /**
     * 根据委托公司id获取委托公司信息
     *
     * @param id 委托公司id
     * @return
     */
    public LeaseCompany findById(String id){
        if(StringUtil.isNotNull(id)) {
            return leaseCompanyRepository.selectByPrimaryKey(id);
        }
        return null;
    }
    /**
     * 根据委托公司id删除委托公司
     * @param leaseId
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> deleteLeaseCompany(String leaseId){
        // 根据委托公司id获取委托公司信息
        LeaseCompany leaseCompany = findById(leaseId);
        if(leaseCompany == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未获取到金融公司信息"),
                    HttpStatus.OK);
        }
        leaseCompanyRepository.delete(leaseId);
        // 根据委托公司id和用户角色获取用户信息
//List<SystemUser> leaseUser = findSysUserByLeaseId();
        return null;
    }
}

