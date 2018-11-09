package cn.com.leadu.cmsxc.external;

import cn.com.leadu.cmsxc.common.constant.Constants;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created with IntelliJ IDEA.
 * User: qiaohao
 * Date: 2018/1/4
 * Time: 下午4:27
 * Description:
 */
@ComponentScan(basePackages = Constants.COMPONENT_SCAN)
@EnableFeignClients(basePackages = Constants.ENABLE_FEIGN_CLIENTS)
@MapperScan(basePackages = Constants.MAPPER_SCAN)
@SpringBootApplication
public class CmsExternalApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmsExternalApplication.class,args);
    }

}
