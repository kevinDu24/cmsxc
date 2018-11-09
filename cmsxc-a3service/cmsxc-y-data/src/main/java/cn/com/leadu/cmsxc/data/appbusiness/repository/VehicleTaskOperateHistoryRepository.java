package cn.com.leadu.cmsxc.data.appbusiness.repository;

import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskOperateHistory;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/17.
 *
 * 任务工单操作日志
 */
public interface VehicleTaskOperateHistoryRepository {
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    List<VehicleTaskOperateHistory> selectByExampleList(Example example);
    /**
     * 登录任务工单操作日志信息
     * @param vehicleTaskOperateHistory
     */
    void insertOne(VehicleTaskOperateHistory vehicleTaskOperateHistory);
}
