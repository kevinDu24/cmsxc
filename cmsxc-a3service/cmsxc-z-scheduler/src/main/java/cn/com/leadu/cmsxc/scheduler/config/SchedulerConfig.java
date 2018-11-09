package cn.com.leadu.cmsxc.scheduler.config;

import cn.com.leadu.cmsxc.scheduler.business.config.CommonConfig;
import cn.com.leadu.cmsxc.scheduler.business.job.AuthMailSendFailJob;
import cn.com.leadu.cmsxc.scheduler.business.job.AuthorizationOutTimeJob;
import cn.com.leadu.cmsxc.scheduler.business.job.CancelMailSendFailJob;
import cn.com.leadu.cmsxc.scheduler.business.job.MonthDataSaveJob;
import cn.com.leadu.cmsxc.scheduler.quartz.AutowiringSpringBeanJobFactory;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by wangxue on 2018/02/17.
 * 注册job任务
 */
@Configuration
@ConditionalOnProperty(name = "quartz.enabled")
public class SchedulerConfig {

    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory,
                                                     @Value("${quartz.scheduler.startup.delay}") Integer startupDelay,
//                                                     @Qualifier("applyOutTimeJobTrigger") Trigger applyOutTimeJobTrigger,
                                                     @Qualifier("authorizationOutTimeJobTrigger") Trigger authorizationOutTimeJobTrigger,
                                                     @Qualifier("authMailSendFailJobTrigger") Trigger authMailSendFailJobTrigger,
                                                     @Qualifier("cancelMailSendFailJobTrigger") Trigger cancelMailSendFailJobTrigger,
                                                     @Qualifier("monthDataSaveJobTrigger") Trigger monthDataSaveJobTrigger


    ) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        //用于quartz集群,QuartzScheduler 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        //factory.setOverwriteExistingJobs(true);
        //用于quartz集群,加载quartz数据源
        factory.setDataSource(dataSource);
        factory.setJobFactory(jobFactory);
        //QuartzScheduler 延时启动，应用启动完20秒后 QuartzScheduler 再启动
        factory.setStartupDelay(startupDelay);
        //用于quartz集群,加载quartz数据源配置
        factory.setQuartzProperties(quartzProperties());
        factory.setTriggers(authorizationOutTimeJobTrigger/**, applyOutTimeJobTrigger*/,authMailSendFailJobTrigger,cancelMailSendFailJobTrigger,monthDataSaveJobTrigger);
        return factory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }
   // 定时分析申请中的数据是否失效.
//    @Bean
//    public JobDetailFactoryBean applyOutTimeJobDetail(){
//        return CommonConfig.createJobDetail(ApplyOutTimeJob.class);
//    }
//
//    @Bean(name = "applyOutTimeJobTrigger")
//    public CronTriggerFactoryBean applyOutTimeJobTrigger(
//            @Qualifier("applyOutTimeJobDetail") JobDetail jobDetail,
//            @Value("${apply.outtime.job.cron.expression}") String cronExpression) {
//        return CommonConfig.createCronTrigger(jobDetail, cronExpression);
//    }
  // 定时分析授权任务是否失效。
    @Bean
    public JobDetailFactoryBean authorizationOutTimeJobDetail(){
        return CommonConfig.createJobDetail(AuthorizationOutTimeJob.class);
    }

    @Bean(name = "authorizationOutTimeJobTrigger")
    public CronTriggerFactoryBean authorizationOutTimeJobTrigger(
            @Qualifier("authorizationOutTimeJobDetail") JobDetail jobDetail,
            @Value("${authorization.outtime.job.cron.expression}") String cronExpression) {
        return CommonConfig.createCronTrigger(jobDetail, cronExpression);
    }

    // 定时分析授权成功邮件发送失败的处理
    @Bean
    public JobDetailFactoryBean authMailSendFailJobDetail(){
        return CommonConfig.createJobDetail(AuthMailSendFailJob.class);
    }

    @Bean(name = "authMailSendFailJobTrigger")
    public CronTriggerFactoryBean authMailSendFailJobTrigger(
            @Qualifier("authMailSendFailJobDetail") JobDetail jobDetail,
            @Value("${mail.auth.job.cron.expression}") String cronExpression) {
        return CommonConfig.createCronTrigger(jobDetail, cronExpression);
    }
    // 定时分析任务取消成功邮件发送失败的处理
    @Bean
    public JobDetailFactoryBean cancelMailSendFailJobDetail(){
        return CommonConfig.createJobDetail(CancelMailSendFailJob.class);
    }

    @Bean(name = "cancelMailSendFailJobTrigger")
    public CronTriggerFactoryBean cancelMailSendFailJobTrigger(
            @Qualifier("cancelMailSendFailJobDetail") JobDetail jobDetail,
            @Value("${mail.auth.job.cron.expression}") String cronExpression) {
        return CommonConfig.createCronTrigger(jobDetail, cronExpression);
    }

    // 月度数据统计,每月初1号凌晨6点执行一次JOB
    @Bean
    public JobDetailFactoryBean monthDataSaveJobDetail(){
        return CommonConfig.createJobDetail(MonthDataSaveJob.class);
    }

    @Bean(name = "monthDataSaveJobTrigger")
    public CronTriggerFactoryBean monthDataSaveJobTrigger(
            @Qualifier("monthDataSaveJobDetail") JobDetail jobDetail,
            @Value("${data.save.job.cron.expression}") String cronExpression) {
        return CommonConfig.createCronTrigger(jobDetail, cronExpression);
    }
}
