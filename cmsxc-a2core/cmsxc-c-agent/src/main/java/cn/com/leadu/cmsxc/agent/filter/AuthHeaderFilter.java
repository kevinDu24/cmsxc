package cn.com.leadu.cmsxc.agent.filter;

import cn.com.leadu.cmsxc.agent.util.AuthUserUtil;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.common.util.UUIDUtil;
import cn.com.leadu.cmsxc.common.util.UrlMatching;
import cn.com.leadu.cmsxc.extend.config.WebProperties;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;

/**
 * Created by qiaohao on 2017/9/5.
 */
@Component
public class AuthHeaderFilter extends ZuulFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthHeaderFilter.class);

	@Autowired
	private AuthUserUtil authUserUtil;

	@Autowired
	private WebProperties webProperties;

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext requestContext = RequestContext.getCurrentContext();
		String requestUri = requestContext.getRequest().getRequestURI();
		String [] notAuthUrls = webProperties.getNotAuthUrls();
		boolean result = UrlMatching.matching(requestUri,notAuthUrls);
		if(!result){
			String chainId = UUIDUtil.getUUID();
			requestContext.addZuulRequestHeader(authUserUtil.getWebProperties().getChainHeader(), chainId);
		}
		return result;
	}

	@Override
	public Object run() {

		String msg = "";
		boolean shouldFilterAuthResult = true;
		RequestContext requestContext = RequestContext.getCurrentContext();
		String chainId = UUIDUtil.getUUID();
		try{
			// 获取当前登录人信息
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String authName = authentication.getName();

			if (StringUtil.isNull(authName)) {
				shouldFilterAuthResult = false;
				msg = "登录用户不能为空";
				return null;
			}
			String userInfo = authUserUtil.getUser();
			if (StringUtil.isNull(userInfo)) {
				shouldFilterAuthResult = false;
				msg = "用户不存在";
				return null;
			}
			try {
				if(StringUtil.isNotNull(userInfo)) {
					requestContext.addZuulRequestHeader(authUserUtil.getWebProperties().getChainHeader(), chainId);
					requestContext.addZuulRequestHeader(authUserUtil.getWebProperties().getLoginUserHeader(), URLEncoder.encode(userInfo, "utf-8"));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				shouldFilterAuthResult = false;
				msg = "发送用户信息失败";
				return null;
			}
			if (StringUtil.isNull(requestContext.getZuulRequestHeaders().get(authUserUtil.getWebProperties().getLoginUserHeader()))) {
				shouldFilterAuthResult = false;
				msg = "用户不存在";
				return null;
			}

//			if (!authUserUtil.urlAuthority(authName, requestContext.getRequest().getRequestURI())) {
//				shouldFilterAuthResult = false;
//				msg = "无权限访问当前url";
//				return null;
//			}
		}catch (Exception ex){
			shouldFilterAuthResult = false;
			LOGGER.error("AuthHeaderFilter异常：",ex);
			ex.printStackTrace();
		}finally {
			requestContext.addZuulRequestHeader(authUserUtil.getWebProperties().getChainHeader(), chainId);
			if (!shouldFilterAuthResult) {
				requestContext.setSendZuulResponse(false);
				requestContext.setResponseStatusCode(401);
				requestContext.addZuulResponseHeader("Content-type","application/json;charset=UTF-8");
				requestContext.setResponseBody(JSON.toJSONString(RestResponseGenerator.genFailResponse(msg)));
				requestContext.set("shouldFilterAuthResult", shouldFilterAuthResult);
			}else{
				LOGGER.info(">>>>>>>>>>>>>>This request for the chainId corresponding to the token,token is - {} -"+" ,chainId is - {} -",authUserUtil.getToken(),chainId);
			}
			return null;
		}


	}

}
