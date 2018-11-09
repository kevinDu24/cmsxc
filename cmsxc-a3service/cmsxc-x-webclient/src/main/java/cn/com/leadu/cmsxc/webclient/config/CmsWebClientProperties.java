package cn.com.leadu.cmsxc.webclient.config;

import lombok.Data;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author qiaomengnan
 * @ClassName: CmsWebClientConfig
 * @Description: WebClient项目配置
 * @date 2018/1/9
 */
@Configurable
@Component
@Data
public class CmsWebClientProperties {

    @Value("${cmsxc.web.client.clientId}")
    private String clientId;

    @Value("${cmsxc.web.client.clientSecret}")
    private String clientSecret;

    @Value("${cmsxc.web.client.grantType}")
    private String grantType;

    private String basic;

    public String getBasic(){
        try{
            if(basic == null) {
                String clientId = getClientId();
                String clientSecret = getClientSecret();
                byte[] basicByte = (clientId + ":" + clientSecret).getBytes("utf-8");
                basic = "Basic " + new String(Base64.encodeBase64(basicByte),"utf-8");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            return basic;
        }
    }

}
