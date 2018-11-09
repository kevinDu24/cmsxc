package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appuser.entity.StorageHistory;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 入库日志表Repository
 */
public interface StorageHistoryRepository {
    /**
     * 通过主键获取
     * @param id
     * @return
     */
    StorageHistory selectByPrimaryKey(String id);
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<StorageHistory> selectByExampleList(Example example);

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    StorageHistory selectOneByExample(Example example);

    /**
     * 登录表
     * @param storageHistorys
     */
    public void insertOne(StorageHistory storageHistorys);

    /**
     * 根据主键更新表
     * @param storageHistory
     */
    void updateByPrimaryKey(StorageHistory storageHistory);

    /**
     * 批量插入数据
     * @param storageHistorys
     */
    void insertMore(List<StorageHistory> storageHistorys);

}
