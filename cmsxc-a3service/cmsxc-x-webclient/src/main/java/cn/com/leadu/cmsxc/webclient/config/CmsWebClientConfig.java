package cn.com.leadu.cmsxc.webclient.config;

import cn.com.leadu.cmsxc.extend.config.WebExtendConfigurer;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * @author qiaomengnan
 * @ClassName: CmsWebClientConfig
 * @Description: 项目配置
 * @date 2018/1/9
 */
@Configurable
public class CmsWebClientConfig extends WebExtendConfigurer {

    @Bean
    public CmsWebClientProperties cmsWebClientProperties(){
        return new CmsWebClientProperties();
    }


}
