package cn.com.leadu.cmsxc.system.controller;

import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.system.service.MonthDataSchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangxue on 2018/2/8.
 *
 * 月度数据统计的controller的实现类.
 */
@RestController
@RequestMapping("dataSave")
public class MonthDataSchedulerController {

    private static final Logger logger = LoggerFactory.getLogger(MonthDataSchedulerController.class);

    @Autowired
    private MonthDataSchedulerService monthDataSchedulerService;

    /**
     * 月度数据统计处理
     * @return
     * */
    @RequestMapping(value = "monthDataSaveHandler", method = RequestMethod.POST)
    public String monthDataSaveHandler() {
        try {
            return monthDataSchedulerService.monthDataSaveHandler();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("月度数据统计error", ex);
            return ResponseEnum.FAILURE.getMark();
        }
    }
}
