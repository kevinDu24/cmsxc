package cn.com.leadu.cmsxc.scheduler.business.job;

import cn.com.leadu.cmsxc.scheduler.business.service.SystemService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 定时分析申请中的数据是否失效.
 * Created by wangxue on 2018/2/8.
 */
public class ApplyOutTimeJob implements Job {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ApplyOutTimeJob.class);

    @Autowired
    private SystemService systemService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOG.info("execute applyOutTimeJob");
        systemService.applyOutTimeHandler();
    }
}
