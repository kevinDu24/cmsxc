package cn.com.leadu.cmsxc.assistant.service;


import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.springframework.http.ResponseEntity;

/**
 * Created by yuanzhenxia on 2018/1/15.
 *
 * 系统登录service
 */
public interface SystemUserService {

    /**
     * 获取用户角色、头像和用户所属委托公司
     *
     * @param systemUser 用户信息
     * @return
     */
    ResponseEntity<RestResponse> userInfo(SystemUser systemUser);
    /**
     * 退出登录
     *
     * @param userId 用户名
     * @return
     */
    ResponseEntity<RestResponse> logout(String userId);
    /**
     * 上传用户头像
     *
     * @param systemUser 用户信息
     * @param userPhotoUrl 用户头像
     * @return
     */
    ResponseEntity<RestResponse> uploadUserPhoto(SystemUser systemUser, String userPhotoUrl);
    /**
     * 修改密码
     *
     * @param userId 用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return
     */
    ResponseEntity<RestResponse> modifyPassword( String userId,String oldPassword, String newPassword);
    /**
     * 根据用户id（手机号）获取用户信息
     * @param userId 用户id
     * @return
     */
    SystemUser selectSystemUserByUserId(String userId);

}
