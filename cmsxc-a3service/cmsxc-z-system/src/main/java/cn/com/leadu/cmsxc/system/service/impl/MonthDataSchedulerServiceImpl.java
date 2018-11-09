package cn.com.leadu.cmsxc.system.service.impl;

import cn.com.leadu.cmsxc.data.appuser.repository.MonthDataInfoRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.system.service.MonthDataSchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wangxue on 2018/2/7.
 * 月度数据统计定时任务处理service的实现类.
 */
@Service
public class MonthDataSchedulerServiceImpl implements MonthDataSchedulerService {
    private static final Logger logger = LoggerFactory.getLogger(MonthDataSchedulerServiceImpl.class);

    @Autowired
    private MonthDataInfoRepository monthDataInfoRepository;

    /**
     * 月度数据统计定时任务处理
     * @return
     * */
    public String monthDataSaveHandler() {
        logger.info("月度数据统计定时任务处理开始");
        monthDataInfoRepository.insertMonthData();
        logger.info("月度数据统计定时任务处理结束");
        return ResponseEnum.SUCCESS.getMark();
    }
}
