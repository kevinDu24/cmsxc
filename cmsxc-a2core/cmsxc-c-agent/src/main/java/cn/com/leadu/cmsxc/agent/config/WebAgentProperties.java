package cn.com.leadu.cmsxc.agent.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by qiaohao on 2017/10/26.
 */
@Data
@Component
@ConfigurationProperties(prefix = "cmsxc.agent")
public class WebAgentProperties {


}
