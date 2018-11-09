package cn.com.leadu.cmsxc.extend.util;

import cn.com.leadu.cmsxc.extend.config.WebProperties;
import cn.com.leadu.cmsxc.common.entity.BaseUser;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

/**
 * @author qiaomengnan
 * @ClassName: UserInfoUtil
 * @Description: 对当前登录用户进行操作
 * @date 2018/1/7
 */

@Component
public class UserInfoUtil {

	public static UserInfoUtil userInfoUtil = null;

	@Autowired
	private WebProperties webProperties;

	public Object getUser(Class entityClass) {
		// get userinfo from request hearder access-system
		String userInfoStr = getUserStr();
		if(StringUtil.isNull(userInfoStr))
			return null;
		try {
			userInfoStr = URLDecoder.decode(userInfoStr, "utf-8");
			Object userInfo = JSON.toJavaObject(JSON.parseObject(userInfoStr), entityClass);
			return userInfo;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public String getUserStr(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String loginUser = request.getHeader(webProperties.getLoginUserHeader());
		return loginUser;
	}

	public String getChainHeader(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String chainHeader = request.getHeader(webProperties.getChainHeader());
		return chainHeader;
	}


	public static BaseUser getUser(){
		BaseUser user = (BaseUser) userInfoUtil.getUser(BaseUser.class);
		return user;
	}

	public static String getChain(){
		return userInfoUtil.getChainHeader();
	}

	public static String getUserName(){
		BaseUser coreUserInfo = getUser();
		if(coreUserInfo == null)
			return null;
		else
			return coreUserInfo.getUsername();
	}

}
