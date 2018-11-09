package cn.com.leadu.cmsxc.appuser;

import cn.com.leadu.cmsxc.common.constant.Constants;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.SpringApplication;
/**
 * Created by 37399 on 2018/1/15.
 */
@ComponentScan(basePackages = Constants.COMPONENT_SCAN)
@EnableFeignClients(basePackages = Constants.ENABLE_FEIGN_CLIENTS)
@MapperScan(basePackages = Constants.MAPPER_SCAN)
@SpringBootApplication
@EnableEurekaClient
public class CmsAppUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsAppUserApplication.class,args);
    }
}
