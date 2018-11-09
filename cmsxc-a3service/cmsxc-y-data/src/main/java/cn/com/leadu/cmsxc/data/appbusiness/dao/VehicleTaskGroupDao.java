package cn.com.leadu.cmsxc.data.appbusiness.dao;

import cn.com.leadu.cmsxc.data.base.config.BaseDao;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/9.
 *
 * 小组任务表Dao
 */
public interface VehicleTaskGroupDao extends BaseDao<VehicleTaskGroup> {

    /**
     * 根据分组id删除分组任务
     *
     * @param groupId 分组id
     */
    void deleteByGroupId(@Param("groupId") String groupId);
    /**
     * 根据分组id删和任務id除分组任务
     *
     * @param groupId 分组id
     * @param vehicleTaskRecoveryId 任務id
     */
    void deleteByGroupIdAndVehicleTaskRecoveryId(@Param("groupId") String groupId, @Param("vehicleTaskRecoveryId") String vehicleTaskRecoveryId);
    /**
     * 根据车牌号，获取任务分配小组id
     *
     * @param plate 分组id
     */
    List<String> selectGroupIdByPlate(@Param("plate") String plate);

}
