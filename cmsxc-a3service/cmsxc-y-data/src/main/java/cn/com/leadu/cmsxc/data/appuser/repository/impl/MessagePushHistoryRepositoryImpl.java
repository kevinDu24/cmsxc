package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.data.appuser.dao.MessagePushHistoryDao;
import cn.com.leadu.cmsxc.data.appuser.repository.MessagePushHistoryRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.MessagePushHistory;
import org.springframework.stereotype.Component;

/**
 * Created by yuanzhenixa on 2018/1/19.
 *
 * 消息推送日志
 */
@Component
public class MessagePushHistoryRepositoryImpl extends AbstractBaseRepository<MessagePushHistoryDao,MessagePushHistory> implements MessagePushHistoryRepository {
    /**
     * 登录表
     * @param messagePushHistory
     */
    public MessagePushHistory insertOne(MessagePushHistory messagePushHistory){
        return super.insert(messagePushHistory);
    }

}
