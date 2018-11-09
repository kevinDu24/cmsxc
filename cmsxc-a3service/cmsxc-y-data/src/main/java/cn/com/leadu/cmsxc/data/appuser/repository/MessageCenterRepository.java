package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appbusiness.vo.GroupUserClientVo;
import cn.com.leadu.cmsxc.pojo.appuser.entity.MessageCenter;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 消息中心
 */
public interface MessageCenterRepository {

    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<MessageCenter> selectByExampleList(Example example);
    /**
     * 登录分组表信息
     *
     * @param messageCenter
     */
    void insertOne(MessageCenter messageCenter);
    /**
     * 根据主键更新表
     * @param messageCenter
     */
    void updateByPrimaryKey(MessageCenter messageCenter);
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    MessageCenter selectOneByExample(Example example);

    /**
     * 批量插入数据
     * @param messageCenters
     */
    void insertMore(List<MessageCenter> messageCenters);

    /**
     * 分页获取消息中心列表
     *
     * @param userId 用户id
     * @param type 类型 "0":系统消息、"1":任务消息、"2":授权消息
     * @param page 当前页数
     * @param size 每页个数
     * @return
     */
    public List<MessageCenter> selectMessageList(String userId, String type, int page, int size);

    /**
     * 更新消息为已读
     *
     * @param type 类型 "0":系统消息、"1":任务消息、"2":授权消息
     * @param userId 用户id
     * @return
     */
    public List<MessageCenter> updateMessageList(String userId, String type);

    /**
     * 通过主键获取消息
     * @param id
     * @return
     */
    public MessageCenter selectByPrimaryKey(String id);

    /**
     * 根据委托公司一级管理员用户名获取委托公司审批人员信息(含消息推送)
     *
     * @param userId 用户名
     * @return
     */
    public GroupUserClientVo findByLeaseAdminUserId(String userId);
}
