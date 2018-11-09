package cn.com.leadu.cmsxc.system.service;

/**
 * Created by wangxue on 2018/2/7.
 * 月度数据统计service
 */
public interface MonthDataSchedulerService {
    /**
     * 月度数据统计定时任务处理
     * @return
     * */
    String monthDataSaveHandler();
}
