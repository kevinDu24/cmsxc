package cn.com.leadu.cmsxc.oauth2.config;

import cn.com.leadu.cmsxc.common.constant.enums.UserRoleEnums;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.oauth2.common.TokenConstant;
import cn.com.leadu.cmsxc.oauth2.common.utils.ArrayUtil;
import cn.com.leadu.cmsxc.oauth2.dao.SystemUserDao;
import cn.com.leadu.cmsxc.oauth2.dao.UserDeviceInfoDao;
import cn.com.leadu.cmsxc.oauth2.entity.SystemUser;
import cn.com.leadu.cmsxc.oauth2.entity.UserDeviceInfo;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author qiaomengnan
 * @ClassName: AuthenticationProviderCustomer
 * @Description: 获取token时的业务逻辑处理,用户名验证码或者密码等验证
 * @date 2018/1/7
 */

@RefreshScope
public class CmsAuthenticationProviderUser implements AuthenticationProvider {

	private final Logger LOGGER = LoggerFactory.getLogger(CmsAuthenticationProviderUser.class);

	@Autowired
	private TokenConstant tokenConstant;

	private final UserDetailsService userDetailsService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserDeviceInfoDao userDeviceInfoDao;

	@Autowired
	private SystemUserDao systemUserDao;

	public CmsAuthenticationProviderUser(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	/*
	curl -i —system 'leadu_cms:leadu_cms_1' -d 'grant_type=password&username=leaduadmin&msgCode=test' -X POST http://localhost:9090/oauth/token
	POST /oauth/token HTTP/1.1
	Host: 192.168.1.134:9090
	Authorization: Basic bGVhZHVfY21zOmxlYWR1X2Ntc18x  (—system 'leadu_cms:leadu_cms_1' 的加密)
	Cache-Control: no-cache
	Content-Type: application/x-www-form-urlencoded
	grant_type=password&username=leaduadmin&pwd=1234&&msgCode=1234
	*验证获取tonken的逻辑
	* */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		Map details = (Map) authentication.getDetails();

		String grant_type =details.get("grant_type")==null?"":details.get("grant_type").toString();

		if (grant_type == null) {
			throw new UsernameNotFoundException("请输入真确的登录方式");
		}

		if(grant_type.equals("password")&&StringUtil.isNull(details.get("pwd"))){
			throw new BadCredentialsException("请输入密码");
		}

		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		String username = token.getName();
		if (StringUtil.isNull(username)) {
			throw new BadCredentialsException("请输入用户名");
		}

		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		if (userDetails == null) {
			throw new UsernameNotFoundException("用户名不存在");
		}

		if(grant_type.indexOf("password")>-1&&!StringUtil.isNull(details.get("pwd"))){
			boolean res = details.get("pwd").toString().equals(userDetails.getPassword());
			if(!res){
				throw new UsernameNotFoundException("密码错误");
			}
		}else{
			throw new UsernameNotFoundException("请输入密码");
		}

		LOGGER.info("send message flag is {}", tokenConstant.getSendMsgFlag());

		/**-------------------单点登录操作-----------------**/
		String deviceId = request.getHeader("deviceId"); //设备号
		String deviceToken = request.getHeader("deviceToken"); //设备标识
		Object client = request.getHeader("client"); //客户端类型 ios/android
		//设备号不为空，操作用户设备信息表
		if(StringUtil.isNotNull(deviceId)){
			roleControl(details,username); //指定app只能指定校色登录
			UserDeviceInfo userDeviceInfo = null;
			Example example = new Example(UserDeviceInfo.class);
			example.createCriteria()
					.andEqualTo("userId",username);
			List<UserDeviceInfo> userDeviceInfos = userDeviceInfoDao.selectByExample(example);
			Date nowDate = new Date();
			// 取到的话，更新
			if(ArrayUtil.isNotNullAndLengthNotZero(userDeviceInfos)){
				userDeviceInfo = userDeviceInfos.get(0);
				userDeviceInfo.setDeviceId(deviceId);
				userDeviceInfo.setDeviceToken(deviceToken);
				userDeviceInfo.setUpdateTime(nowDate);
				if (client != null){
					userDeviceInfo.setClient(Integer.valueOf(client.toString()));
				}
				userDeviceInfoDao.updateByPrimaryKey(userDeviceInfo);
			} else {
				//取不到，新增一条记录
				userDeviceInfo = new UserDeviceInfo();
				userDeviceInfo.setUserId(username);
				userDeviceInfo.setDeviceId(deviceId);
				userDeviceInfo.setDeviceToken(deviceToken);
				userDeviceInfo.setCreateTime(nowDate);
				userDeviceInfo.setUpdateTime(nowDate);
				if (client != null){
					userDeviceInfo.setClient(Integer.valueOf(client.toString()));
				}
				userDeviceInfoDao.insert(userDeviceInfo);
			}
		}

		return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());


	}

	@Override
	public boolean supports(Class<?> aClass) {
		return UsernamePasswordAuthenticationToken.class.equals(aClass);
	}

	/**
	 * 指定app只能指定用户角色登录的判断
	 * @param details
	 * @param username
	 */
	private void roleControl(Map details, String username){
		SystemUser systemUser = null;
		Example example1 = new Example(SystemUser.class);
		example1.createCriteria()
				.andEqualTo("userId",username);
		List<SystemUser> systemUsers = systemUserDao.selectByExample(example1);
		if(ArrayUtil.isNotNullAndLengthNotZero(systemUsers))
			systemUser = systemUsers.get(0);
		else{
			throw new UsernameNotFoundException("用户名不存在");
		}
		String appType =details.get("app_type")==null?"":details.get("app_type").toString();
		if("sjxc".equals(appType)){ //如果是赏金寻寻车app请求接口
			if(!UserRoleEnums.RECOVERY_MEMBER.getType().equals(systemUser.getUserRole()) //不是收车人员
					&& !UserRoleEnums.RECOVERY_MANAGER.getType().equals(systemUser.getUserRole()) //不是内勤
					&& !UserRoleEnums.RECOVERY_BOSS.getType().equals(systemUser.getUserRole())){ //不是总经理
				throw new UsernameNotFoundException("用户名不存在");
			}
		} else if("xczs".equals(appType)){ //如果是寻车助手app请求接口
			if(!UserRoleEnums.LEASE_AUDITOR.getType().equals(systemUser.getUserRole())
					&& !UserRoleEnums.LEASE_ADMIN.getType().equals(systemUser.getUserRole())
					&& !UserRoleEnums.PARKING_ADMIN.getType().equals(systemUser.getUserRole())
					&& !UserRoleEnums.PARKING_MANAGER.getType().equals(systemUser.getUserRole())){
				throw new UsernameNotFoundException("用户名不存在");
			}
		}
	}
}
