package cn.com.leadu.cmsxc.assistant.service.impl;

import cn.com.leadu.cmsxc.assistant.service.SystemUserService;
import cn.com.leadu.cmsxc.common.constant.enums.UserRoleEnums;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.appbusiness.repository.LeaseCompanyRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.UserDeviceInfoRepository;
import cn.com.leadu.cmsxc.data.system.repository.SystemUserRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.LeaseCompany;
import cn.com.leadu.cmsxc.pojo.appuser.entity.UserDeviceInfo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.UserInfoVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/1/15.
 *
 * 系统登录
 */
@Service
public class SystemUserServiceImpl implements SystemUserService {
    private static final Logger logger = LoggerFactory.getLogger(SystemUserServiceImpl.class);
    @Autowired
    private SystemUserRepository systemUserRepository;
    @Autowired
    private LeaseCompanyRepository leaseCompanyRepository;
    @Autowired
    private UserDeviceInfoRepository userDeviceInfoRepository;
    @Autowired
    private HttpServletRequest request;

    /**
     * 获取用户角色、头像和用户所属委托公司
     *
     * @param systemUser 用户信息
     * @return
     */
    public ResponseEntity<RestResponse> userInfo( SystemUser systemUser){
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setUserRole(UserRoleEnums.getEnum(systemUser.getUserRole()).getValue()); // 设定用户角色
        userInfoVo.setUserRoleCode(systemUser.getUserRole()); //设定角色code
        userInfoVo.setUserPhoto(systemUser.getUserPhoto()); //设定用户头像
        //获取所属委托公司
        String leaseId = systemUser.getLeaseId();
        if(StringUtil.isNull(leaseId)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","当前用户角色有误"),
                    HttpStatus.OK);
        }
        //通过leaseId查询所属委托公司
        LeaseCompany leaseCompany = leaseCompanyRepository.selectByPrimaryKey(leaseId);
        if(leaseCompany == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","当前用户角色有误"),
                    HttpStatus.OK);
        }
        userInfoVo.setLeaseCompanyName(leaseCompany.getLeaseFullName()); //设定委托公司全称
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,userInfoVo,""),
                HttpStatus.OK);
    }


    /**
     * 退出登录
     *
     * @param userId 用户名
     * @return
     */
    public ResponseEntity<RestResponse> logout(String userId){
        String deviceId = request.getHeader("deviceId");
        if(StringUtil.isNull(deviceId)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"",""),
                    HttpStatus.OK);
        }
        Example example = new Example(UserDeviceInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        UserDeviceInfo userDeviceInfo = userDeviceInfoRepository.selectOneByExample(example);
        if(userDeviceInfo != null && deviceId.equals(userDeviceInfo.getDeviceId())){
            userDeviceInfoRepository.delete(userDeviceInfo.getId());
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"",""),
                HttpStatus.OK);
    }
    /**
     * 上传用户头像
     *
     * @param systemUser 用户信息
     * @param userPhotoUrl 用户头像
     * @return
     */
    public ResponseEntity<RestResponse> uploadUserPhoto(SystemUser systemUser, String userPhotoUrl){
        if(systemUser == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","账户不存在"),
                    HttpStatus.OK);
        }
        if(StringUtil.isNull(userPhotoUrl)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","头像信息为空"),
                    HttpStatus.OK);
        }
        systemUser.setUserPhoto(userPhotoUrl);
        systemUser.setUpdater(systemUser.getUserId());
        systemUser.setUpdateTime(new Date());
        systemUserRepository.updateByPrimaryKeySelective(systemUser);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","头像上传成功"),
                HttpStatus.OK);
    }

    /**
     * 修改密码
     *
     * @param userId  用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return
     */
    public ResponseEntity<RestResponse> modifyPassword( String userId,String oldPassword, String newPassword){
        // 根据手机号取得用户信息
        SystemUser sysUser = selectSystemUserByUserId(userId);
        if(sysUser == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","账户不存在"),
                    HttpStatus.OK);
        }
        if(!oldPassword.equals(sysUser.getUserPassword())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","原密码不正确"),
                    HttpStatus.OK);
        }
        // 设置新密码
        sysUser.setUserPassword(newPassword);
        sysUser.setUpdateTime(new Date());
        systemUserRepository.updateByPrimaryKey(sysUser);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","修改成功"),
                HttpStatus.OK);
    }

    /**
     * 根据用户id（手机号）获取用户信息
     * @param userId 用户id
     * @return
     */
    public SystemUser selectSystemUserByUserId(String userId){
        if(StringUtil.isNotNull(userId)) {
            Example example = new Example(SystemUser.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId", userId);
            return systemUserRepository.selectOneByExample(example);
        }
        return null;
    }

}
