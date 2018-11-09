package cn.com.leadu.cmsxc.agent.filter;

import cn.com.leadu.cmsxc.agent.rpc.cmsxcappuser.CmsxcAppUserRpc;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.common.util.UrlMatching;
import cn.com.leadu.cmsxc.extend.config.WebProperties;
import cn.com.leadu.cmsxc.extend.response.ResponseFailEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by qiaohao on 2017/12/26.
 */
@Component
public class AppUserPreFilter extends ZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppUserPreFilter.class);

    @Autowired
    private WebProperties webProperties;

    @Autowired
    CmsxcAppUserRpc cmsxcAppUserRpc;

    @Autowired
    private HttpServletRequest request;

    private static final String APPUSERFLAG = "/appuser";

    private static final String ASSISTANTFLAG = "/assistant";

    @Override
    public Object run() {
        // 获取当前登录人信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authName = authentication.getName();

        if (StringUtil.isNull(authName)) {
            return null;
        }
        String deviceId = request.getHeader("deviceId"); //设备号
        if(deviceId == null){
            return null;
        }
        boolean result = cmsxcAppUserRpc.singleLoginCheck(authName, deviceId);
        if(result){
            return null;
        }
        RequestContext requestContext = RequestContext.getCurrentContext();
        //当前设备号与登录设备信息表中的设备号不一致，中断后去请求，返回code"95000000"的错误。
        requestContext.setSendZuulResponse(false);
        requestContext.addZuulResponseHeader("Content-type","application/json;charset=UTF-8");
        requestContext.setResponseBody(JSON.toJSONString(RestResponseGenerator.genFailResponse(ResponseFailEnum.SINGLE_LOGIN_ERROR)));
        return null;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        String requestUri = requestContext.getRequest().getRequestURI();
        //如果不是app相关接口，直接跳过过滤器
        if(requestUri.indexOf(APPUSERFLAG) == -1 && requestUri.indexOf(ASSISTANTFLAG) == -1){
            return false;
        }
        String [] notAuthUrls = webProperties.getNotAuthUrls();
        boolean result = UrlMatching.matching(requestUri,notAuthUrls);
        // 不在免认证url集合内，需要走过滤器
        return result;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public String filterType() {
        return "pre";
    }

}
