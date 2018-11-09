package cn.com.leadu.cmsxc.webclient.config;

import cn.com.leadu.cmsxc.common.util.UrlMatching;
import cn.com.leadu.cmsxc.extend.config.WebProperties;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author qiaomengnan
 * @ClassName: CmsWebClientFeignConfig
 * @Description: 放入本次用户的token
 * @date 2018/1/14
 */
@Component
public class CmsWebClientFeignConfig implements RequestInterceptor {

    @Autowired
    private WebProperties webProperties;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestUri = requestTemplate.request().url().split("\\?")[0];
        /**
         * 不在权限过滤中的url需要带上token
         */
        if(UrlMatching.matching(requestUri,webProperties.getNotAuthUrls())) {
            for (Cookie cookie : request.getCookies()) {
                if (webProperties.getLoginUserHeader().equals(cookie.getName())) {
                    requestTemplate.header("Authorization", "Bearer " + cookie.getValue());
                }
            }
        }
    }

}
