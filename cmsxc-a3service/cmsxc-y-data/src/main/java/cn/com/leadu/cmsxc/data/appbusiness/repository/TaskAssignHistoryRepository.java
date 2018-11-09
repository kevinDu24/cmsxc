package cn.com.leadu.cmsxc.data.appbusiness.repository;

import cn.com.leadu.cmsxc.pojo.appbusiness.entity.TaskAssignHistory;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/9.
 *
 * 小组任务分配日志表Respository
 */
public interface TaskAssignHistoryRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<TaskAssignHistory> selectByExampleList(Example example);
    /**
     * 登录分组表信息
     *
     * @param taskAssignHistory
     */
    void insertOne(TaskAssignHistory taskAssignHistory);
    /**
     * 根据主键更新表
     * @param taskAssignHistory
     */
    void updateByPrimaryKey(TaskAssignHistory taskAssignHistory);
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    TaskAssignHistory selectOneByExample(Example example);
    /**
     * 批量插入数据
     * @param taskAssignHistories
     */
    public void insertMore(List<TaskAssignHistory> taskAssignHistories);
}
