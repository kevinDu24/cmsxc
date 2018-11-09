package cn.com.leadu.cmsxc.appuser.service;

import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskGroup;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/12.
 *
 * 小组任务表
 */
public interface VehicleTaskGroupService {
    /**
     * 根据分组id获取所有小组任务
     *
     * @param groupId 分组id
     * @return
     */
    List<VehicleTaskGroup> getVehicleTaskGroupByGroupId(String groupId);
    /**
     * 根据任务id和分组id获取小组的任务
     *
     * @param vehicleTaskRecoveryId 收车公司任务表主键
     * @param groupId 分组id
     * @return
     */
    VehicleTaskGroup getByGroupIdAndVehicleTaskRecoveryId(String groupId,String vehicleTaskRecoveryId);
    /**
     * 根据收车公司任务表id获取任务分组情况
     *
     * @param vehicleTaskRecoveryId 收车公司任务表主键
     * @return
     */
    List<VehicleTaskGroup> getByVehicleTaskRecoveryId(String vehicleTaskRecoveryId);

}
