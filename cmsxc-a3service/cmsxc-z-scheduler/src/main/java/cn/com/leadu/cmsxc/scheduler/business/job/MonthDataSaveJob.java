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
 * 月度数据统计ob
 */
public class MonthDataSaveJob implements Job {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(MonthDataSaveJob.class);

    @Autowired
    private SystemService systemService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOG.info("execute monthDataSaveJob");
        systemService.monthDataSaveHandler();
    }

}
