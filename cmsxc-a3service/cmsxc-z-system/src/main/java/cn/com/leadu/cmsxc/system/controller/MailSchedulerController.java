package cn.com.leadu.cmsxc.system.controller;

import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.system.service.MailSchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangxue on 2018/2/8.
 *
 * 邮件发送的定时处理的controller的实现类.
 */
@RestController
@RequestMapping("mail")
public class MailSchedulerController {

    private static final Logger logger = LoggerFactory.getLogger(MailSchedulerController.class);

    @Autowired
    private MailSchedulerService mailSchedulerService;

    /**
     * 分析授权成功邮件发送失败的处理
     * @return
     * */
    @RequestMapping(value = "authMailSendFailHandler", method = RequestMethod.POST)
    public String applyOutTimeHandler() {
        try {
            return mailSchedulerService.authMailSendFailHandler();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("分析授权成功邮件发送失败的处理error", ex);
            return ResponseEnum.FAILURE.getMark();
        }
    }
    /**
     * 分析任务取消邮件发送失败的定时任务处理
     * @return
     * */
    @RequestMapping(value = "cancelMailSendFailHandler", method = RequestMethod.POST)
    public String cancelMailSendFailHandler() {
        try {
            return mailSchedulerService.cancelMailSendFailHandler();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("分析任务取消邮件发送失败的定时任务处理error", ex);
            return ResponseEnum.FAILURE.getMark();
        }
    }
}
