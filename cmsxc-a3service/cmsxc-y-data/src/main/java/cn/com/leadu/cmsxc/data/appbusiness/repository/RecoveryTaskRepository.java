package cn.com.leadu.cmsxc.data.appbusiness.repository;

import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskRecovery;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/2/12.
 *
 * 收车公司派单任务
 */
public interface RecoveryTaskRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<VehicleTaskRecovery> selectByExampleList(Example example);
    /**
     * 登录车辆任务工单信息
     *
     * @param vehicleTaskRecovery
     */
    void insertOne(VehicleTaskRecovery vehicleTaskRecovery);
    /**
     * 根据主键更新表
     * @param vehicleTaskRecovery
     */
    void updateByPrimaryKey(VehicleTaskRecovery vehicleTaskRecovery);
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    VehicleTaskRecovery selectOneByExample(Example example);
}
