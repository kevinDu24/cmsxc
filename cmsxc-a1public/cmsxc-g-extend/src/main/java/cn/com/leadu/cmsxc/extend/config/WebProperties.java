package cn.com.leadu.cmsxc.extend.config;

import cn.com.leadu.cmsxc.common.util.StringUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author qiaomengnan
 * @ClassName: WebProperties
 * @Description: 项目配置属性获取
 * @date 2018/1/7
 */

@Data
@Component
public class WebProperties {

    @Value("${web.check.token.url}")
    private String checkTokenUrl;

    @Value("${web.client.id}")
    private String clientId;

    @Value("${web.client.secret}")
    private String clientSecret;

    @Value("${web.login.user.header}")
    private String loginUserHeader;

    @Value("${web.chain.header}")
    private String chainHeader;

    @Value("${web.notAuthUrl}")
    private String notAuthUrl;

    @Value("${web.app.header.client}")
    private String appHeaderClient;

    @Value("${web.app.header.version}")
    private String appHeaderVersion;

    private String [] notAuthUrls;

    public String[] getNotAuthUrls(){
        if( (notAuthUrls == null || notAuthUrls.length == 0)
                && StringUtil.isNotNull(notAuthUrl)){
            notAuthUrls = notAuthUrl.split(",");
        }
        return notAuthUrls;
    }

}
