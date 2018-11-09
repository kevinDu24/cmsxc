package cn.com.leadu.cmsxc.agent.config;

import cn.com.leadu.cmsxc.agent.util.AuthUserUtil;
import cn.com.leadu.cmsxc.extend.config.WebExtendConfigurer;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * Created by qiaohao on 2017/10/23.
 */
@Configuration
public class WebAgentConfig extends WebExtendConfigurer {

    @Bean
    public WebAgentProperties webAgentProperties(){
        return new WebAgentProperties();
    }

    @Bean
    public AuthUserUtil authUserUtil(){
        return new AuthUserUtil();
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(){
        return new EmbeddedServletContainerCustomizer(){
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"));
                container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"));
                container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/error/400"));
                container.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/401"));
                container.addErrorPages(new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, "/error/405"));
                container.addErrorPages(new ErrorPage(Throwable.class,"/error/500"));
            }
        };
    }
}
