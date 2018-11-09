package cn.com.leadu.cmsxc.extend.config;

import cn.com.leadu.cmsxc.extend.util.UserInfoUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author qiaomengnan
 * @ClassName: WebFeignConfig
 * @Description: feign配置 传入当前登录用户信息
 * @date 2018/1/7
 */

@Component
public class WebFeignConfig implements RequestInterceptor {

    @Autowired
    private WebProperties webProperties;

    @Autowired
    private UserInfoUtil userInfoUtil;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(webProperties.getLoginUserHeader(),userInfoUtil.getUserStr());
        requestTemplate.header(webProperties.getChainHeader(),userInfoUtil.getChainHeader());
    }
}
