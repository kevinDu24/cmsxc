package cn.com.leadu.cmsxc.data.appbusiness.repository.impl;

import cn.com.leadu.cmsxc.data.appbusiness.dao.MessageLogDao;
import cn.com.leadu.cmsxc.data.appbusiness.repository.MessageLogRepository;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.MessageLog;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 短信获取验证码日志
 */
@Component
public class MessageLogRepositoryImpl extends AbstractBaseRepository<MessageLogDao,MessageLog> implements MessageLogRepository{
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    @Override
    public List<MessageLog> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }
    /**
     * 登录短信验证码日志信息
     * @param messageLog
     */
    public void insertOne(MessageLog messageLog){
        super.insert(messageLog);
    }
}
