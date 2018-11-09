package cn.com.leadu.cmsxc.scheduler.business.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wangxue on 2018/2/8.
 * 调用cmsxc-z-system项目的微服务.
 */
@FeignClient(name = "system", url = "${request.systemServerUrl}")
public interface SystemService {

    /**
     * 调用分析申请中的数据是否失效接口
     * @return
     */
    @RequestMapping(value = "authorizationscheduler/applyOutTimeHandler", method = RequestMethod.POST)
    @ResponseBody
    String applyOutTimeHandler();

    /**
     * 调用分析授权是否失效的接口
     * @return
     */
    @RequestMapping(value = "authorizationscheduler/authorizationOutTimeHandler", method = RequestMethod.POST)
    @ResponseBody
    String authrizationOutTimeHandler();

    /**
     * 分析授权成功邮件发送失败的接口
     * @return
     */
    @RequestMapping(value = "mail/authMailSendFailHandler", method = RequestMethod.POST)
    @ResponseBody
    String authMailSendFailHandler();
    /**
     * 分析授权成功邮件发送失败的接口
     * @return
     */
    @RequestMapping(value = "mail/cancelMailSendFailHandler", method = RequestMethod.POST)
    @ResponseBody
    String cancelMailSendFailHandler();

    /**
     * 月度数据统计接口
     * @return
     */
    @RequestMapping(value = "dataSave/monthDataSaveHandler", method = RequestMethod.POST)
    @ResponseBody
    String monthDataSaveHandler();


}
