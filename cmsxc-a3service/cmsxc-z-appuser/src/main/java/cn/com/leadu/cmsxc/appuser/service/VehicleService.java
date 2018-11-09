package cn.com.leadu.cmsxc.appuser.service;

import cn.com.leadu.cmsxc.pojo.appuser.vo.VehicleTaskVo;
import cn.com.leadu.cmsxc.appuser.validator.sysuser.vo.VehicleVo;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import org.springframework.http.ResponseEntity;

/**
 * Created by yuanzhenxia on 2018/1/17.
 *
 * 车辆相关信息Service
 */
public interface VehicleService {
    /**
     * 根据车牌号，查看车辆是否存在，并返回是否第一次查看
     *
     * @param userId 用户id
     * @param plate 车牌号
     * @param taskStatus 任务状态
     * @return
     */
    ResponseEntity<RestResponse> checkSearchBefore(String userId ,String plate, String taskStatus);
    /**
     * 判断当前数据工单状态是否更改为“正常”之外的情况
     *
     * @param plate 用户id
     * @return
     */
    public ResponseEntity<RestResponse> checkTaskStatus(String plate);
    /**
     * 根据车牌号，查看车辆是否存在，并返回是否第一次查看【列表用】
     *
     * @param userId 用户id
     * @param plate 车牌号
     * @param taskStatus 任务状态
     * @return
     */
    ResponseEntity<RestResponse> checkSearchBeforeForList(String userId ,String plate,String taskStatus);
    /**
     * 取工单状态为正常的车辆任务工单数量
     * @return
     */
    ResponseEntity<RestResponse> getNormalCount();
    /**
     * 根据价格区间，gps有无，线索有无，违章有无等条件，搜索车辆信息
     *
     * @param vehicleTaskVo 画面参数
     * @param userId 用户id
     * @return
     */
    ResponseEntity<RestResponse> getVehicleTaskList(String userId, VehicleTaskVo vehicleTaskVo);
    /**
     * 根据车牌号，搜索车辆信息
     *
     * @param userId 用户id
     * @param vehicleVo 画面参数
     * @return
     */
    ResponseEntity<RestResponse> searchVehicleDetail(String userId ,VehicleVo vehicleVo);
    /**
     * 用户查询历史
     *
     * @param userId 用户Id
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    ResponseEntity<RestResponse> userSearchHistory(String userId, int page, int size);
    /**
     * 判断积分是否充足
     *
     * @param userId 用户id
     * @return
     */
    ResponseEntity<RestResponse> searchUserScore(String userId);

}
