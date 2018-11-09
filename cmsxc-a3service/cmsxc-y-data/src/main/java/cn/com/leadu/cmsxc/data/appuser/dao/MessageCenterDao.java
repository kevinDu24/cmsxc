package cn.com.leadu.cmsxc.data.appuser.dao;

import cn.com.leadu.cmsxc.data.base.config.BaseDao;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.GroupUserClientVo;
import cn.com.leadu.cmsxc.pojo.appuser.entity.MessageCenter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 消息中心Dao
 */
public interface MessageCenterDao extends BaseDao<MessageCenter> {

    /**
     * 分页查询消息中心列表
     *
     * @param userId 用户id
     * @param type 类型 "0":系统消息、"1":任务消息、"2":授权消息
     * @param page 当前页
     * @param size 每页数目
     * @return
     */
    List<MessageCenter> selectMessageList(@Param("userId") String userId, @Param("type") String type,
                                        @Param("page") int page, @Param("size") int size);

    /**
     * 更新消息为已读
     *
     * @param type 类型 "0":系统消息、"1":任务消息、"2":授权消息
     * @param userId 用户id
     * @return
     */
    List<MessageCenter> updateMessageList(@Param("userId") String userId, @Param("type") String type);

    /**
     * 获取委托公司一级管理员信息(含消息推送)
     *
     * @param userId 用户名
     * @return
     */
    GroupUserClientVo selectByLeaseAdminUserId(@Param("userId") String userId);
}
