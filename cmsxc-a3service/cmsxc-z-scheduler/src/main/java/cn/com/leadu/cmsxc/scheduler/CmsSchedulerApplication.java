package cn.com.leadu.cmsxc.scheduler;

import cn.com.leadu.cmsxc.scheduler.config.SchedulerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableFeignClients
@Import({SchedulerConfig.class})
public class CmsSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CmsSchedulerApplication.class, args);
	}
}
