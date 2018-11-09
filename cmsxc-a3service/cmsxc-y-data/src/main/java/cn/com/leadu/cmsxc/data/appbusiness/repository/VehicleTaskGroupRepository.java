package cn.com.leadu.cmsxc.data.appbusiness.repository;

import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskGroup;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/9.
 *
 * 小组任务表Respository
 */
public interface VehicleTaskGroupRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<VehicleTaskGroup> selectByExampleList(Example example);
    /**
     * 登录分组表信息
     *
     * @param vehicleTaskGroup
     */
    void insertOne(VehicleTaskGroup vehicleTaskGroup);
    /**
     * 根据主键更新表
     * @param vehicleTaskGroup
     */
    void updateByPrimaryKey(VehicleTaskGroup vehicleTaskGroup);
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    VehicleTaskGroup selectOneByExample(Example example);

    /**
     * 批量插入数据
     * @param vehicleTaskGroups
     */
    void insertMore(List<VehicleTaskGroup> vehicleTaskGroups);
    /**
     * 删除数据
     *
     * @param id
     */
    void delete(Object id);
    /**
     * 根据分组id删除分组任务
     *
     * @param groupId 分组id
     */
    void deleteByGroupId(String groupId);
    /**
     * 根据分组id删和任務id除分组任务
     *
     * @param groupId 分组id
     * @param vehicleTaskRecoveryId 任務id
     */
    void deleteByGroupIdAndVehicleTaskRecoveryId(String groupId, String vehicleTaskRecoveryId);
    /**
     * 根据车牌号，获取任务分配小组id
     *
     * @param plate 车牌号
     */
    List<String> selectGroupIdByPlate(String plate);

}
