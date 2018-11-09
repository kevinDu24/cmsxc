package cn.com.leadu.cmsxc.appuser.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appuser.entity.SystemUserFeedback;
import org.springframework.http.ResponseEntity;
import cn.com.leadu.cmsxc.appuser.validator.sysuser.vo.RegisterVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;

import java.util.Date;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/15.
 *
 * 系统登录service
 */
public interface SystemUserService {

    /**
     * 用户注册
     *
     * @param registerVo 用户注册信息
     * @return
     */
    ResponseEntity<RestResponse> register(RegisterVo registerVo);
    /**
     * 根据用户名密码登录
     *
     * @param userId 用户id
     * @param userPassword 用户密码
     * @return
     */
    ResponseEntity<RestResponse> login(String userId, String userPassword);

    /**
     * 根据手机号获取用户信息
     *
     * @param userId 用户id
     * @return
     */
    SystemUser selectSystemUserByUserId(String userId);
    /**
     * 根据委托公司id和角色获取用户信息
     *
     * @param leaseId 委托公司id
     * @param userRole 用户角色
     * @return
     */
    SystemUser selectByUserIdAndUserRole(String leaseId, String userRole);
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
     * 找回密码
     *
     * @param userId 用户id
     * @param verficationCode 验证码
     * @param newPassword 新密码
     * @return
     */
    ResponseEntity<RestResponse> findBackPassword( String userId,String verficationCode, String newPassword);

    /**
     * 用户意见反馈
     *
     * @param userId 用户id
     * @param feedBackContent 意见内容
     * @param connectWay 联系方式
     * @return
     */
    ResponseEntity<RestResponse> editUserFeedback( String userId,String feedBackContent, String connectWay);

    /**
     * 获取用户角色和用户所属收车公司
     *
     * @param systemUser 用户信息
     * @return
     */
    ResponseEntity<RestResponse> userInfo( SystemUser systemUser);
    /**
     * 根据委托公司id和角色获取用户信息
     *
     * @param userId 用户id
     * @param createTime 创建时间
     * @return
     */
    List<SystemUserFeedback> selectByUserIdAndCreateTime(String userId, Date createTime);
    /**
     * app启动时判断是否显示敏感信息（app审核用）
     *
     * @return
     */
    ResponseEntity<RestResponse> appCheck();
    /**
     * 单点登录判断
     *
     * @return
     */
    boolean singleLoginCheck(String userId);
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
    ResponseEntity<RestResponse> uploadUserPhoto( SystemUser systemUser, String userPhotoUrl);
    /**
     * 覆盖更新时，若是登录状态更新最新的deviceToken
     *
     * @param userId 用户名
     * @return
     */
    ResponseEntity<RestResponse> pushNewestDeviceToken(String userId);
    /**
     * 根据收车公司id和角色获取用户信息
     *
     * @param recoveryCompanyId 收车公司id
     * @param userRole 用户角色
     * @return
     */
    List<SystemUser> selectByRecoveryCompanyIdAndUserRole(String recoveryCompanyId, String userRole);
    /**
     * 根据收车公司id取用户信息
     *
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    List<SystemUser> selectByRecoveryCompanyId(String recoveryCompanyId);

    /**
     * 根据委托公司id及工单所在省份，获取审核人员信息
     *
     * @param leaseId 委托公司id
     * @param taskId 工单id
     * @return
     */
    List<SystemUser> selectLeaseAdminUserList(String leaseId, String taskId);
}
