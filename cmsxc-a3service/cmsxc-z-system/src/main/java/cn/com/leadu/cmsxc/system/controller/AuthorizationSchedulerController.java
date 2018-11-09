package cn.com.leadu.cmsxc.system.controller;

import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.system.service.AuthorizationSchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangxue on 2018/2/8.
 *
 * 授权状态定时处理的controller的实现类.
 */
@RestController
@RequestMapping("authorizationscheduler")
public class AuthorizationSchedulerController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationSchedulerController.class);

    @Autowired
    private AuthorizationSchedulerService authorizationSchedulerService;

    /**
     * 分析申请任务是否失效处理
     * @return
     * */
    @RequestMapping(value = "applyOutTimeHandler", method = RequestMethod.POST)
    public String applyOutTimeHandler() {
        try {
            return authorizationSchedulerService.applyOutTimeHandler();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("分析申请任务是否失效处理error", ex);
            return ResponseEnum.FAILURE.getMark();
        }
    }
    /**
     * 分析授权任务是否失效处理
     * @return
     * */
    @RequestMapping(value = "authorizationOutTimeHandler", method = RequestMethod.POST)
    public String authorizationOutTimeHandler() {
        try {
            return authorizationSchedulerService.authorizationOutTimeHandler();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("分析授权任务是否失效处理error", ex);
            return ResponseEnum.FAILURE.getMark();
        }
    }
}
