package cn.com.leadu.cmsxc.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created with IntelliJ IDEA.
 * User: qiaohao
 * Date: 2018/1/4
 * Time: 上午10:49
 * Description:
 */
@EnableEurekaServer
@SpringBootApplication
public class CmsCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmsCenterApplication.class,args);
    }

}
