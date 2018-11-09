package cn.com.leadu.cmsxc.appbusiness.service;

import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskSearchHistory;

/**
 * Created by yuanzhenxia on 2018/1/17.
 *
 * 工单查询历史
 */
public interface VehicleTaskSearchHistoryService {
    /**
     * 根据任务id和用户id查询工单查询历史表
     * @param taskId 工单id
     * @param userId 用户id
     * @return
     */
    VehicleTaskSearchHistory selectByTaskIdAndUserId(String taskId, String userId);
    /**
     * 根据车牌号和用户id查询工单查询历史表
     * @param plate 车牌号
     * @param userId 用户id
     * @return
     */
    VehicleTaskSearchHistory selectByPlateAndUserId(String plate, String userId);


}
