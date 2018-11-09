package cn.com.leadu.cmsxc.data.appbusiness.repository.impl;

import cn.com.leadu.cmsxc.data.appbusiness.dao.TaskAssignHistoryDao;
import cn.com.leadu.cmsxc.data.appbusiness.repository.TaskAssignHistoryRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.TaskAssignHistory;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/9.
 *
 * 小组任务表RepositoryImpl
 */
@Component
public class TaskAssignHistoryRepositoryImpl extends AbstractBaseRepository<TaskAssignHistoryDao,TaskAssignHistory> implements TaskAssignHistoryRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    public List<TaskAssignHistory> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }
    /**
     * 登录分组表信息
     *
     * @param taskAssignHistory
     */
    public void insertOne(TaskAssignHistory taskAssignHistory){
        super.insert(taskAssignHistory);
    }
    /**
     * 根据主键更新表
     * @param taskAssignHistory
     */
    public void updateByPrimaryKey(TaskAssignHistory taskAssignHistory){
        super.updateByPrimaryKey(taskAssignHistory);
    }
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    public TaskAssignHistory selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }
    /**
     * 批量插入数据
     * @param taskAssignHistories
     */
    public void insertMore(List<TaskAssignHistory> taskAssignHistories){
        super.insertListByMapper(taskAssignHistories);
    }
}
