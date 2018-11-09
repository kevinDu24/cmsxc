package cn.com.leadu.cmsxc.assistant;

import cn.com.leadu.cmsxc.common.constant.Constants;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by 37399 on 2018/1/15.
 */
@ComponentScan(basePackages = Constants.COMPONENT_SCAN)
@EnableFeignClients(basePackages = Constants.ENABLE_FEIGN_CLIENTS)
@MapperScan(basePackages = Constants.MAPPER_SCAN)
@SpringBootApplication
@EnableEurekaClient
public class CmsAssistantApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsAssistantApplication.class,args);
    }
}
