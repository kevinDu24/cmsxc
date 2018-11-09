package cn.com.leadu.cmsxc.data.appbusiness.repository;

import cn.com.leadu.cmsxc.pojo.appbusiness.entity.MessageLog;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryCompany;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 短信获取验证码日志
 */
public interface MessageLogRepository {
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    List<MessageLog> selectByExampleList(Example example);
    /**
     * 登录短信日志信息
     * @param messageLog
     */
    void insertOne(MessageLog messageLog);
}
