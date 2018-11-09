package cn.com.leadu.cmsxc.appuser.service.impl;

import cn.com.leadu.cmsxc.appbusiness.service.RecoveryCompanyService;
import cn.com.leadu.cmsxc.appuser.service.SystemUserService;
import cn.com.leadu.cmsxc.appuser.util.constant.enums.ScoreCodeEnums;
import cn.com.leadu.cmsxc.appuser.validator.sysuser.vo.RegisterVo;
import cn.com.leadu.cmsxc.common.constant.enums.GroupLeaderEnum;
import cn.com.leadu.cmsxc.common.constant.enums.UserRoleEnums;
import cn.com.leadu.cmsxc.common.redis.RedisRepository;
import cn.com.leadu.cmsxc.common.util.ArrayUtil;
import cn.com.leadu.cmsxc.common.util.CommonUtil;
import cn.com.leadu.cmsxc.common.util.DateUtil;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.appbusiness.repository.RecoveryGroupRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.AppStartCheckRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.SystemUserFeedbackRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.SystemUserScoreRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.UserDeviceInfoRepository;
import cn.com.leadu.cmsxc.data.system.repository.SysUserRoleRepository;
import cn.com.leadu.cmsxc.data.system.repository.SystemUserRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryCompany;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryGroup;
import cn.com.leadu.cmsxc.pojo.appuser.entity.AppStartCheck;
import cn.com.leadu.cmsxc.pojo.appuser.entity.SystemUserFeedback;
import cn.com.leadu.cmsxc.pojo.appuser.entity.SystemUserScore;
import cn.com.leadu.cmsxc.pojo.appuser.entity.UserDeviceInfo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.UserInfoVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SysUserRole;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private SystemUserScoreRepository systemUserScoreRepository;
    @Autowired
    private RecoveryCompanyService recoveryCompanyService;
    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;
    @Autowired
    private SystemUserFeedbackRepository systemUserFeedbackRepository;
    @Autowired
    private AppStartCheckRepository appStartCheckRepository;
    @Autowired
    private UserDeviceInfoRepository userDeviceInfoRepository;
    @Autowired
    private RecoveryGroupRepository recoveryGroupRepository;
    @Autowired
    private HttpServletRequest request;

    private static int FEEDBACKNUM = 5;
    /**
     * 根据用户id 和 密码登录
     * @param userId  用户id
     * @param userPassword 用户密码
     * @return
     */
    public ResponseEntity<RestResponse> login(String userId, String userPassword){
        SystemUser sysUser = selectSystemUserByUserId(userId);
        if(sysUser == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","用户不存在"),
                    HttpStatus.OK);
        }else if(!userPassword.equals(sysUser.getUserPassword())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","密码不正确"),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","登录成功"),
                HttpStatus.OK);
    }
    /**
     * 用户注册
     * @param registerVo 注册信息
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> register(RegisterVo registerVo){
        Date nowDate = new Date();
        String userRole = "";
        RecoveryCompany recoveryCompany = null;
        if(StringUtil.isNull(registerVo.getRecoComRegCode())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","注册码不可为空！"),
                    HttpStatus.OK);
        }
        // 根据手机号取得用户信息
        SystemUser sysUser = selectSystemUserByUserId(registerVo.getUserId());
        if(sysUser != null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该账户已注册，请直接登录"),
                    HttpStatus.OK);
        }
        // redis中取得验证码
        if(redisRepository.get(registerVo.getUserId()) == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","验证码过期,请重新获取验证码"),
                    HttpStatus.OK);
        // 填写验证码是否正确
        }else if(!redisRepository.get(registerVo.getUserId()).equals(registerVo.getVerficationCode())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","验证码不正确,请重新填写"),
                    HttpStatus.OK);
        }
        // 填写的公司注册码是否为业务员注册码
        recoveryCompany = recoveryCompanyService.selectRecoveryCompanyBySalasmanRegisterCode(registerVo.getRecoComRegCode().toUpperCase());
        if(recoveryCompany != null){
            // 角色为业务员
            userRole = UserRoleEnums.RECOVERY_MEMBER.getType();
        } else{
            // 填写的公司注册码是否为主管注册码
            recoveryCompany = recoveryCompanyService.selectRecoveryCompanyByManagerRegisterCode(registerVo.getRecoComRegCode().toUpperCase());
            if(recoveryCompany != null){
                // 根据收车公司id和角色获取此公司的内勤人员，判断内勤人员是否已存在
                Example example = new Example(SystemUser.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("recoveryCompanyId", recoveryCompany.getId());
                criteria.andEqualTo("userRole", UserRoleEnums.RECOVERY_MANAGER.getType());
                SystemUser user = systemUserRepository.selectOneByExample(example);
                if(user != null){
                    return new ResponseEntity<RestResponse>(
                            RestResponseGenerator.genResponse(ResponseEnum.FAILURE, "", "此收车公司内勤人员已存在，不可重复注册！"),
                            HttpStatus.OK);
                }
                // 如果内勤人员不存在
                // 角色为主管
                userRole = UserRoleEnums.RECOVERY_MANAGER.getType();
            }else{
                // 填写的公司注册码是否为总经理注册码
                recoveryCompany = recoveryCompanyService.selectRecoveryCompanyByBossRegisterCode(registerVo.getRecoComRegCode().toUpperCase());
                if(recoveryCompany != null) {
                    // 角色为总经理
                    userRole = UserRoleEnums.RECOVERY_BOSS.getType();
                }else{
                    // 如果填写的注册码既不是主管注册码也不是业务员注册码，返回错误信息
                    return new ResponseEntity<RestResponse>(
                            RestResponseGenerator.genResponse(ResponseEnum.FAILURE, "", "公司注册码填写错误"),
                            HttpStatus.OK);
                }
            }

        }
        // 新注册用户信息登录到用户表中
        SystemUser sysUserVo = new SystemUser();
        // 用户id
        sysUserVo.setUserId(registerVo.getUserId());
        // 用户密码
        sysUserVo.setUserPassword(registerVo.getUserPassword());
        // 用户名称---去除姓名中的表情符号
        sysUserVo.setUserName(CommonUtil.filterEmoji(registerVo.getUserName()));
        // 收车公司id
        if (recoveryCompany != null){
            sysUserVo.setRecoveryCompanyId(recoveryCompany.getId());
        }
        // 用户角色
        sysUserVo.setUserRole(userRole);
        // 新注册用户赠送500积分
        sysUserVo.setTotalScore(500);
        sysUserVo.setCreator(registerVo.getUserId());
        sysUserVo.setUpdater(registerVo.getUserId());
        sysUserVo.setCreateTime(nowDate);
        sysUserVo.setUpdateTime(nowDate);
        sysUserVo.setUserPhone(registerVo.getUserId());
        systemUserRepository.insertOne(sysUserVo);
        //插入用户角色表
        SysUserRole sysUserRole = new SysUserRole();
        // 用户id
        sysUserRole.setUserId(sysUserVo.getId());
        // 角色id
        sysUserRole.setRoleId(userRole);
        sysUserRoleRepository.insertOne(sysUserRole);
        // 新注册用户赠送500积分，登录到积分流水表中
        SystemUserScore systemUserScore = new SystemUserScore();
        // 用户id
        systemUserScore.setUserId(registerVo.getUserId());
        // 积分code
        systemUserScore.setScoreCode(ScoreCodeEnums.REGISTER.getType());
        // 积分值
        systemUserScore.setScoreValue(500);
        // 备注
        systemUserScore.setRemark(ScoreCodeEnums.REGISTER.getValue());
        systemUserScore.setScoreTime(nowDate);
        systemUserScore.setCreator(registerVo.getUserId());
        systemUserScore.setUpdater(registerVo.getUserId());
        systemUserScore.setUpdateTime(nowDate);
        systemUserScore.setCreateTime(nowDate);
        systemUserScoreRepository.insertOne(systemUserScore);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","注册成功"),
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
    /**
     * 根据委托公司id和角色获取用户信息
     *
     * @param leaseId 委托公司id
     * @param userRole 用户角色
     * @return
     */
    public SystemUser selectByUserIdAndUserRole(String leaseId, String userRole){
        if(StringUtil.isNotNull(leaseId)) {
            Example example = new Example(SystemUser.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("leaseId", leaseId);
            criteria.andEqualTo("userRole", userRole);
            return systemUserRepository.selectOneByExample(example);
        }
        return null;
    }

    /**
     * 根据委托公司id及工单所在省份，获取审核人员信息
     *
     * @param leaseId 委托公司id
     * @param taskId 工单id
     * @return
     */
    public List<SystemUser> selectLeaseAdminUserList(String leaseId, String taskId){
        if(StringUtil.isNotNull(leaseId) && StringUtil.isNotNull(taskId)) {
            return systemUserRepository.selectLeaseAdminUserList(leaseId,taskId);
        }
        return null;
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
     * 找回密码
     *
     * @param userId 用户id
     * @param verficationCode 验证码
     * @param newPassword 新密码
     * @return
     */
    public ResponseEntity<RestResponse> findBackPassword( String userId,String verficationCode, String newPassword){
        // 根据手机号取得用户信息
        SystemUser sysUser = selectSystemUserByUserId(userId);
        if(sysUser == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","账户不存在"),
                    HttpStatus.OK);
        }
        // redis中取得验证码
        if(redisRepository.get(userId) == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","验证码过期,请重新获取验证码"),
                    HttpStatus.OK);
        // 判断验证码是否正确
        }else if(!redisRepository.get(userId).equals(verficationCode)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","验证码不正确,请重新填写"),
                    HttpStatus.OK);
        }
        // 修改用户密码
        sysUser.setUserPassword(newPassword);
        sysUser.setUpdateTime(new Date());
        systemUserRepository.updateByPrimaryKey(sysUser);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","找回成功"),
                HttpStatus.OK);
    }
    /**
     * 用户意见反馈
     *
     * @param userId 用户id
     * @param feedBackContent 意见内容
     * @param connectWay 联系方式
     * @return
     */
    public ResponseEntity<RestResponse> editUserFeedback( String userId,String feedBackContent, String connectWay){
        Date nowDate = new Date();
        if(userId == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","账户不存在"),
                    HttpStatus.OK);
        }
        // 获取此用户在当天凌晨后提交的所有意见信息
        List<SystemUserFeedback> systemUserFeedbackList = selectByUserIdAndCreateTime(userId, DateUtil.getDateTime(DateUtil.getStringDate(nowDate).concat("000000")));
        if(ArrayUtil.isNullOrLengthZero(systemUserFeedbackList) || (ArrayUtil.isNotNullAndLengthNotZero(systemUserFeedbackList) && systemUserFeedbackList.size() < FEEDBACKNUM) ){
            SystemUserFeedback systemUserFeedback = new SystemUserFeedback();
            // 用户id
            systemUserFeedback.setUserId(userId);
            // 意见内容
            systemUserFeedback.setFeedbackContent(CommonUtil.filterEmoji(feedBackContent));
            // 联系方式
            systemUserFeedback.setConnectWay(connectWay);
            systemUserFeedback.setUpdateTime(nowDate);
            systemUserFeedback.setCreateTime(nowDate);
            systemUserFeedback.setCreator(userId);
            systemUserFeedback.setUpdater(userId);
            systemUserFeedbackRepository.insertOne(systemUserFeedback);
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","意见反馈成功"),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","抱歉，每天最多提交5条反馈意见！"),
                HttpStatus.OK);
    }
    /**
     * 根据委托公司id和角色获取用户信息
     *
     * @param userId 用户id
     * @param createTime 创建时间
     * @return
     */
    public List<SystemUserFeedback> selectByUserIdAndCreateTime(String userId, Date createTime){
        if(StringUtil.isNotNull(userId)) {
            Example example = new Example(SystemUserFeedback.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId", userId);
            criteria.andGreaterThanOrEqualTo("createTime", createTime);
            return systemUserFeedbackRepository.selectByExampleList(example);
        }
        return null;
    }

    /**
     * 获取用户角色和用户所属收车公司
     *
     * @param systemUser 用户信息
     * @return
     */
    public ResponseEntity<RestResponse> userInfo( SystemUser systemUser){
        UserInfoVo userInfoVo = new UserInfoVo();
        if(systemUser == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","账户不存在"),
                    HttpStatus.OK);
        }
        // 设置用户角色
        userInfoVo.setUserRole(UserRoleEnums.getEnum(systemUser.getUserRole()).getValue());
        userInfoVo.setUserRoleCode(systemUser.getUserRole());
        userInfoVo.setUserPhoto(systemUser.getUserPhoto());
        userInfoVo.setGroupLeaderFlag(GroupLeaderEnum.NO.getCode());
        // 判断是否为组长
        if(UserRoleEnums.RECOVERY_MEMBER.getType().equals(systemUser.getUserRole())){
            Example example = new Example(RecoveryGroup.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("groupLeaderId", systemUser.getUserId());
            RecoveryGroup recoveryGroup =  recoveryGroupRepository.selectOneByExample(example);
            //若在分组表中通过组长id可以匹配到，则是组长
            if(recoveryGroup != null){
                userInfoVo.setGroupLeaderFlag(GroupLeaderEnum.YES.getCode());
            }
        }
        // 获取收车公司信息
        RecoveryCompany recoveryCompany = recoveryCompanyService.selectRecoveryCompanyById(systemUser.getRecoveryCompanyId());
        if(recoveryCompany != null){
            // 设置收车公司全称
            userInfoVo.setRecoveryCompanyName(recoveryCompany.getRecoveryFullName());
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,userInfoVo,""),
                HttpStatus.OK);
    }

    /**
     * app启动时判断是否显示敏感信息（app审核用）
     *
     * @return
     */
    public ResponseEntity<RestResponse> appCheck(){
        List<AppStartCheck> resultList = appStartCheckRepository.selectAll();
        if(resultList == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"",""),
                    HttpStatus.OK);
        }
        Map result = new HashMap();
        AppStartCheck appStartCheck = resultList.get(0);
        result.put("showFlag",appStartCheck.getShowFlag());
        result.put("version",appStartCheck.getVersion());
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,result,""),
                HttpStatus.OK);
    }

    /**
     * 单点登录判断
     *
     * @return
     */
    public boolean singleLoginCheck(String userId){
        String deviceId = request.getHeader("deviceId"); //取得当前设备号
        Example example = new Example(UserDeviceInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        UserDeviceInfo userDeviceInfo = userDeviceInfoRepository.selectOneByExample(example);
        if(userDeviceInfo == null){
            return false;
        }
        String newestDeviceId = userDeviceInfo.getDeviceId(); //获得登录设备信息表中最新的设备号
        //判断该账号的最新设备是否和当前设备一致
        if(deviceId.equals(newestDeviceId)){
            return true;
        }
        //不一致
        return false;
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
    public ResponseEntity<RestResponse> uploadUserPhoto( SystemUser systemUser, String userPhotoUrl){
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
     * 覆盖更新时，若是登录状态更新最新的deviceToken
     *
     * @param userId 用户名
     * @return
     */
    public ResponseEntity<RestResponse> pushNewestDeviceToken(String userId){
        String deviceId = request.getHeader("deviceId");
        String deviceToken = request.getHeader("deviceToken");
        if(StringUtil.isNull(userId) || StringUtil.isNull(deviceToken) || StringUtil.isNull(deviceId)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"",""),
                    HttpStatus.OK);
        }
        //取得该用户名对应的用户设备信息
        Example example = new Example(UserDeviceInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        UserDeviceInfo userDeviceInfo = userDeviceInfoRepository.selectOneByExample(example);
        if(userDeviceInfo == null || StringUtil.isNull(userDeviceInfo.getDeviceId())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"",""),
                    HttpStatus.OK);
        }
        //如果设备号一致，则更新传入的devicetoken
        if(deviceId.equals(userDeviceInfo.getDeviceId())){
            userDeviceInfo.setDeviceToken(deviceToken);
            userDeviceInfoRepository.updateByPrimaryKey(userDeviceInfo);
        }
        //若不一致，什么也不做
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"",""),
                HttpStatus.OK);
    }

    /**
     * 根据收车公司id和角色获取用户信息
     *
     * @param recoveryCompanyId 收车公司id
     * @param userRole 用户角色
     * @return
     */
    public List<SystemUser> selectByRecoveryCompanyIdAndUserRole(String recoveryCompanyId, String userRole){
        if(StringUtil.isNotNull(recoveryCompanyId) && StringUtil.isNotNull(userRole)) {
            Example example = new Example(SystemUser.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("recoveryCompanyId", recoveryCompanyId);
            criteria.andEqualTo("userRole", userRole);
            return systemUserRepository.selectByExampleList(example);
        }
        return null;
    }
    /**
     * 根据收车公司id取用户信息
     *
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    public List<SystemUser> selectByRecoveryCompanyId(String recoveryCompanyId){
        if(StringUtil.isNotNull(recoveryCompanyId)) {
            Example example = new Example(SystemUser.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("recoveryCompanyId", recoveryCompanyId);
            return systemUserRepository.selectByExampleList(example);
        }
        return null;
    }

}
