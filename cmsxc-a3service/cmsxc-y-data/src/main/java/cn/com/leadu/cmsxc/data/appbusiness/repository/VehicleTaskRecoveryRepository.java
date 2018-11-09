package cn.com.leadu.cmsxc.data.appbusiness.repository;

import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskRecovery;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 收车公司派单任务
 */
public interface VehicleTaskRecoveryRepository {
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    List<VehicleTaskRecovery> selectByExampleList(Example example);
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    VehicleTaskRecovery selectOneByExample(Example example);
    /**
     * 登录收车公司派单任务表
     * @param vehicleTaskRecovery
     */
    void insertOne(VehicleTaskRecovery vehicleTaskRecovery);
    /**
     * 根据主键更新表
     * @param vehicleTaskRecovery
     */
    public void updateByPrimaryKey(VehicleTaskRecovery vehicleTaskRecovery);
}
