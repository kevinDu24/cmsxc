package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.appuser.dao.MessageCenterDao;
import cn.com.leadu.cmsxc.data.appuser.repository.MessageCenterRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.GroupUserClientVo;
import cn.com.leadu.cmsxc.pojo.appuser.entity.MessageCenter;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenixa on 2018/1/19.
 *
 * 消息中心Repository
 */
@Component
public class MessageCenterRepositoryImpl extends AbstractBaseRepository<MessageCenterDao,MessageCenter> implements MessageCenterRepository {

    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    public List<MessageCenter> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }
    /**
     * 登录分组表信息
     *
     * @param messageCenter
     */
    public void insertOne(MessageCenter messageCenter){
        super.insert(messageCenter);
    }
    /**
     * 根据主键更新表
     * @param messageCenter
     */
    public void updateByPrimaryKey(MessageCenter messageCenter){
        super.updateByPrimaryKey(messageCenter);
    }
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    public MessageCenter selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }
    /**
     * 批量插入数据
     * @param messageCenters
     */
    public void insertMore(List<MessageCenter> messageCenters){
        super.insertListByMapper(messageCenters);
    }

    /**
     * 分页获取消息中心列表
     *
     * @param userId 用户id
     * @param type 类型 "0":系统消息、"1":任务消息、"2":授权消息
     * @param page 当前页数
     * @param size 每页个数
     * @return
     */
    public List<MessageCenter> selectMessageList(String userId, String type, int page, int size){
        return baseDao.selectMessageList(userId, type, page, size);
    }

    /**
     * 更新消息为已读
     *
     * @param type 类型 "0":系统消息、"1":任务消息、"2":授权消息
     * @param userId 用户id
     * @return
     */
    public List<MessageCenter> updateMessageList(String userId, String type){
        return baseDao.updateMessageList(userId, type);
    }

    /**
     * 通过主键获取消息
     * @param id
     * @return
     */
    public MessageCenter selectByPrimaryKey(String id){
        return super.selectByPrimaryKey(id);
    }

    /**
     * 获取委托公司一级管理员信息(含消息推送)
     *
     * @param userId 用户名
     * @return
     */
    public GroupUserClientVo findByLeaseAdminUserId(String userId){
        if(StringUtil.isNotNull(userId)) {
            return baseDao.selectByLeaseAdminUserId(userId);
        }
        return null;
    }
}
