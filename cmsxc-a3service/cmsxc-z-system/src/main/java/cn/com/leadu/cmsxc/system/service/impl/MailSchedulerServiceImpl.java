package cn.com.leadu.cmsxc.system.service.impl;

import cn.com.leadu.cmsxc.common.constant.enums.MailTypeEnum;
import cn.com.leadu.cmsxc.data.appuser.repository.AuthMailRecordRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.pojo.appuser.entity.AuthMailRecord;
import cn.com.leadu.cmsxc.system.service.MailSchedulerService;
import cn.com.leadu.cmsxc.system.util.constant.MailUtils;
import cn.com.leadu.cmsxc.common.constant.enums.MailSendStatusEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * Created by wangxue on 2018/2/7.
 * 邮件发送的定时处理的service的实现类.
 */
@Service
public class MailSchedulerServiceImpl implements MailSchedulerService {
    private static final Logger logger = LoggerFactory.getLogger(MailSchedulerServiceImpl.class);

    @Autowired
    private AuthMailRecordRepository authMailRecordRepository;

    @Autowired
    private MailUtils mailUtils;


    private static final long DAY_MILLISECOND = 24*60*60*1000L;

    /**
     * 析授权成功邮件发送失败的定时任务处理
     * @return
     * */
    public String authMailSendFailHandler() {
        logger.info("分析已授权的单子邮件是否发送成功处理开始");
        // 取出邮件发送失败且重试次数小于两次的数据
        Example example = new Example(AuthMailRecord.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andGreaterThanOrEqualTo("finishDate", new Date());
        criteria.andEqualTo("sendStatus", MailSendStatusEnums.NOT_SEND.getType());
        criteria.andEqualTo("mailType", MailTypeEnum.AUTHORIZATION.getCode());
        example.setOrderByClause(" create_time asc ");
        List<AuthMailRecord> resultList = authMailRecordRepository.selectByExampleList(example);
        logger.info("分析需要重新发送邮件的件数：" + resultList.size());
        if (resultList != null && resultList.size() > 0) {
            for (AuthMailRecord result : resultList) {
                Integer times = result.getRetryTimes();
                times = times + 1;
                result.setRetryTimes(times);
                try {
                    //发邮件操作
                    mailUtils.sendAuthMail(result);
                    //重试发送成功，更新状态为已发送，重试次数+1
                    result.setSendStatus(MailSendStatusEnums.SENTED.getType());
                    authMailRecordRepository.updateByPrimaryKey(result);
                } catch (Exception e) {
                    e.printStackTrace();
                    //重试发送失败，重试次数+1
                    authMailRecordRepository.updateByPrimaryKey(result);
                }
            }
        }
        logger.info("分析已授权的单子邮件是否发送成功处理结束");
        return ResponseEnum.SUCCESS.getMark();
    }
    /**
     * 分析任务取消邮件发送失败的定时任务处理
     * @return
     * */
    public String cancelMailSendFailHandler() {
        logger.info("分析已取消的单子邮件是否发送成功处理开始");
        // 取出邮件发送失败且重试次数小于两次的数据
        Example example = new Example(AuthMailRecord.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sendStatus", MailSendStatusEnums.NOT_SEND.getType());
        criteria.andEqualTo("mailType", MailTypeEnum.CANCEL.getCode());
        criteria.andLessThanOrEqualTo("retryTimes", 3);
        example.setOrderByClause(" create_time asc ");
        List<AuthMailRecord> resultList = authMailRecordRepository.selectByExampleList(example);
        logger.info("分析需要重新发送邮件的件数：" + resultList.size());
        if (resultList != null && resultList.size() > 0) {
            for (AuthMailRecord result : resultList) {
                Integer times = result.getRetryTimes();
                times = times + 1;
                result.setRetryTimes(times);
                try {
                    //发邮件操作
                    mailUtils.sendAuthMail(result);
                    //重试发送成功，更新状态为已发送，重试次数+1
                    result.setSendStatus(MailSendStatusEnums.SENTED.getType());
                    result.setUpdateTime(new Date());
                    authMailRecordRepository.updateByPrimaryKey(result);
                } catch (Exception e) {
                    e.printStackTrace();
                    //重试发送失败，重试次数+1
                    authMailRecordRepository.updateByPrimaryKey(result);
                }
            }
        }
        logger.info("分析已取消的单子邮件是否发送成功处理结束");
        return ResponseEnum.SUCCESS.getMark();
    }
}
