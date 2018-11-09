package cn.com.leadu.cmsxc.agent.util;

import cn.com.leadu.cmsxc.agent.rpc.cmsoauth2.CmsOauth2Rpc;
import cn.com.leadu.cmsxc.agent.rpc.cmsuser.CmsSystemRpc;
import cn.com.leadu.cmsxc.common.exception.CmsRpcException;
import cn.com.leadu.cmsxc.extend.config.WebProperties;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.util.ResponseEntityUtil;
import com.alibaba.fastjson.JSON;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qiaohao on 2017/10/23.
 */
@Component
public class AuthUserUtil {

	@Autowired
	private WebProperties webProperties;

	@Autowired
	private CmsOauth2Rpc cmsOauth2Rpc;

	private static final Integer timeOut = 0;

	private static final String bearer = "Bearer";

	@Autowired
	private CmsSystemRpc cmsSystemRpc;


	public List<String> getRes(String authUserName){
		try {
			List<String> data = ResponseEntityUtil.getRestResponseData(cmsSystemRpc.findSysResByUsername(authUserName));
			return data;
		}catch (CmsRpcException ex){
			ex.printStackTrace();
		}
		return null;
	}


	public String getToken(){
		String authorization = RequestContext.getCurrentContext().getRequest().getHeader("Authorization");
		String token = replaceIgnoreCase(authorization, bearer, "").trim();
		return token;
	}

	public String getUser() {
		String userInfo = null;
		try {
			String authUserName = SecurityContextHolder.getContext().getAuthentication().getName();
			ResponseEntity<RestResponse<Map<String,Object>>> map  = cmsSystemRpc.findSysUserByUsername(authUserName);
			Map<String,Object> value =  ResponseEntityUtil.getRestResponseData(map);
			if(value != null && authUserName.equals(value.get("userId"))){
				userInfo = JSON.toJSONString(value);
			}
			return userInfo;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	private String replaceIgnoreCase(String input, String regex, String replacement) {
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(input);
		String result = m.replaceAll(replacement);
		return result;
	}

	public boolean urlAuthority(String authUserName, String url) {
		List<String> res = getRes(authUserName);
		// if(ArrayUtil.isNullOrLengthZero(res) || ArrayUtil.notEqualsContains(res,url))
		// return false;
		// return true;
		if (null != res && res.size() > 0) {
			for (String str : res) {
				int result = str.indexOf("/**");
				if (result != -1) {
					if (result == 0)
						return true;
					else {
						String str1 = str.replace("/**", "");
						if (url.indexOf(str1) == 0)
							return true;
					}
				}
				if (url.contains(str)) {
					return true;
				}
			}
		}
		return false;
	}

	public Integer getTokenExpiresIn(String token) {
		ResponseEntity<Integer> result = cmsOauth2Rpc.getTokenExpiresIn(token);
		if (result != null) {
			Integer expireIn = result.getBody();
			return expireIn;
		}
		return 0;
	}

	public WebProperties getWebProperties() {
		return webProperties;
	}

}
