package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appuser.entity.MessagePushHistory;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 消息推送日志
 */
public interface MessagePushHistoryRepository {
    /**
     * 登录表
     *
     * @param messagePushHistory
     */
    MessagePushHistory insertOne(MessagePushHistory messagePushHistory);

}
