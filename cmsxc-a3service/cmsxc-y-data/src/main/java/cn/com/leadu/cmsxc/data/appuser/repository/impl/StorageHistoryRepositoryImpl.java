package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.data.appuser.dao.StorageHistoryDao;
import cn.com.leadu.cmsxc.data.appuser.repository.StorageHistoryRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.StorageHistory;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 入库日志RepositoryImpl
 */
@Component
public class StorageHistoryRepositoryImpl extends AbstractBaseRepository<StorageHistoryDao,StorageHistory> implements StorageHistoryRepository {
    /**
     * 通过主键获取工单
     * @param id
     * @return
     */
    public StorageHistory selectByPrimaryKey(String id){
        return super.selectByPrimaryKey(id);
    }

    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    public List<StorageHistory> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    public StorageHistory selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }

    /**
     * 登录表
     * @param storageHistory
     */
    public void insertOne(StorageHistory storageHistory){
        super.insert(storageHistory);
    }

    /**
     * 根据主键更新表
     * @param storageHistory
     */
    public void updateByPrimaryKey(StorageHistory storageHistory) {
        super.updateByPrimaryKey(storageHistory);
    }

    /**
     * 批量插入数据
     * @param storageHistorys
     */
    public void insertMore(List<StorageHistory> storageHistorys){
        super.insertListByMapper(storageHistorys);
    }

}
