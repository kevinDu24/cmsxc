package cn.com.leadu.cmsxc.webclient;

import cn.com.leadu.cmsxc.common.constant.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created with IntelliJ IDEA.
 * User: qiaohao
 * Date: 2018/1/4
 * Time: 下午12:23
 * Description:
 */
@ComponentScan(basePackages = Constants.COMPONENT_SCAN)
@EnableFeignClients(basePackages = Constants.ENABLE_FEIGN_CLIENTS)
@SpringBootApplication
@EnableEurekaClient
public class CmsWebClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmsWebClientApplication.class,args);
    }

}
