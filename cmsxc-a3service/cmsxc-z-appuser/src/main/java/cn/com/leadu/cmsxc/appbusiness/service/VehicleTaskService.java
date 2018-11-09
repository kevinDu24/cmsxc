package cn.com.leadu.cmsxc.appbusiness.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.VehicleTaskResultVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.VehicleTaskVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTask;
import cn.com.leadu.cmsxc.pojo.appuser.vo.RewardForVehicleVo;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 车辆任务工单Service
 */
public interface VehicleTaskService {
    /**
     * 根据车牌号获取车辆任务工单信息
     *
     * @param plate 车架号
     * @return
     */
    VehicleTask selectVehicleTeskByplate(String plate);
    /**
     * 根据任务id获取车辆任务工单信息
     *
     * @param taskId 任务id
     * @return
     */
    VehicleTask selectVehicleTeskById(String taskId);
    /**
     * 取工单状态为正常的车辆任务工单数量
     *
     * @param nowDate 当前时间
     * @return
     */
    int selectCountAll( Date nowDate );
    /**
     * 根据价格区间，gps有无，线索有无，违章有无等条件，搜索车辆信息
     *
     * @param vehicleTaskVo 画面参数
     * @param taskStatus 工单状态
     * @param userId 用户id
     * @return
     */
    List<RewardForVehicleVo> selectByTaskStatusAndMore(String userId, String taskStatus , VehicleTaskVo vehicleTaskVo);

    /**
     * 根据车牌号和工单状态获取车辆任务工单信息
     *
     * @param plate 车牌号
     * @param taskStatus 工单状态
     * @param nowDate 当前时间
     * @return
     */
    VehicleTask selectByPlateAndTaskStatus(String plate, String taskStatus, Date nowDate );
    /**
     * 根据车牌号和工单状态获取车辆任务工单信息
     * @param plate 车牌号
     * @return
     */
    VehicleTask selectVehicleInfo(String plate);
    /**
     * 根据车牌号和工单状态获取车辆任务工单信息
     * @param plate 车牌号
     * @param taskStatus 状态
     * @return
     */
    VehicleTask selectByPlateAndTaskStatus(String plate,String taskStatus );
    /**
     * 用户查询历史
     *
     * @param userId 用户id
     * @param taskStatus 工单状态
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    List<RewardForVehicleVo> userSearchHistory(String taskStatus , String userId, int page, int size);

    /**
     * 根据任务id获取该任务的详情信息
     *
     * @param taskId 任务id
     * @param userId 用户id
     *  @param plate 车牌号
     * @return
     */
    VehicleTaskResultVo findTaskDetail(String userId, String taskId, String plate);

}
