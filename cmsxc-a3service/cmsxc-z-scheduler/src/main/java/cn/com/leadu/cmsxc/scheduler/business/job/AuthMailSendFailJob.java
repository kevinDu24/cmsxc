package cn.com.leadu.cmsxc.scheduler.business.job;

import cn.com.leadu.cmsxc.scheduler.business.service.SystemService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yuanzhenxia on 2018/2/13.
 *
 * 析授权成功邮件发送失败的处理job
 */
public class AuthMailSendFailJob implements Job {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(AuthMailSendFailJob.class);

    @Autowired
    private SystemService systemService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOG.info("execute authMailSendFailJob");
        systemService.authMailSendFailHandler();
    }

}
