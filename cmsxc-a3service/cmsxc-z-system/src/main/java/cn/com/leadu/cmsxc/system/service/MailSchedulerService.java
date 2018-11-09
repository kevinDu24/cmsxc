package cn.com.leadu.cmsxc.system.service;

/**
 * Created by wangxue on 2018/2/7.
 * 邮件发送的定时处理的service
 */
public interface MailSchedulerService {

    /**
     * 分析授权成功邮件发送失败的处理
     * @return
     * */
    String authMailSendFailHandler();
    /**
     * 分析任务取消邮件发送失败的定时任务处理
     * @return
     * */
    String cancelMailSendFailHandler();
}
