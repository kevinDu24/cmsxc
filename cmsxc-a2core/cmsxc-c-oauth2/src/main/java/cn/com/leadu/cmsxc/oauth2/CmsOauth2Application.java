package cn.com.leadu.cmsxc.oauth2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author qiaomengnan
 * @ClassName: OAtuah2Application
 * @Description: 鉴权启动类
 * @date 2018/1/7
 */

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@MapperScan(basePackages = "cn.com.leadu.cmsxc.oauth2.dao")
@ComponentScan(basePackages = "cn.com.leadu.cmsxc")
public class CmsOauth2Application {

    public static void main(String[] args) {
        SpringApplication.run(CmsOauth2Application.class,args);
    }

}
