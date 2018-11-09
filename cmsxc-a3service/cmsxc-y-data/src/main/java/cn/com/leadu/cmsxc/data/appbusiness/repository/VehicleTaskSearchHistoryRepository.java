package cn.com.leadu.cmsxc.data.appbusiness.repository;

import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTask;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskSearchHistory;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/17.
 *
 * 工单查询历史
 */
public interface VehicleTaskSearchHistoryRepository {
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    List<VehicleTaskSearchHistory> selectByExampleList(Example example);
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    VehicleTaskSearchHistory selectOneByExample(Example example);
    /**
     * 登录任务工单查询历史信息
     * @param vehicleTaskSearchHistory
     */
    void insertOne(VehicleTaskSearchHistory vehicleTaskSearchHistory);
    /**
     * 根据主键更新表
     * @param vehicleTaskSearchHistory
     */
    void updateByPrimaryKey(VehicleTaskSearchHistory vehicleTaskSearchHistory);
}
