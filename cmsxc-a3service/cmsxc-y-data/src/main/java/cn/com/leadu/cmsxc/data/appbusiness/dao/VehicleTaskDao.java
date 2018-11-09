package cn.com.leadu.cmsxc.data.appbusiness.dao;

import cn.com.leadu.cmsxc.data.base.config.BaseDao;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTask;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryTaskManagerVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryTaskVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 车辆任务工单
 */
public interface VehicleTaskDao extends BaseDao<VehicleTask> {
    /**
     * 根据价格区间，gps有无，线索有无，违章有无等条件，搜索车辆信息
     *
     * @param vehicleTaskVo 画面信息
     * @param taskStatus 工单状态
     * @param userId 用户id
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    List<RewardForVehicleVo> selectByTaskStatusAndMore(@Param("vehicleTaskVo") VehicleTaskVo vehicleTaskVo,@Param("taskStatus") String taskStatus,
                                                       @Param("userId") String userId, @Param("page") int page,@Param("size") int size);
    /**
     * 用户查询历史
     *
     * @param userId 用户id
     * @param taskStatus 工单状态
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    List<RewardForVehicleVo> selectUserSearchHistory(@Param("userId") String userId,@Param("taskStatus") String taskStatus,
                                                     @Param("page") int page,@Param("size") int size);

    /**
     * 获取查看过某辆车的所有用户的用户id
     *
     * @param plate 车牌号
     * @return
     */
    List<String> selectUserIdByPlate(@Param("plate") String plate);

    /**
     * 查看首页车辆任务工单数量
     *
     * @param nowDate 当前时间
     * @return
     */
    int selectCountAll(Date nowDate);

    /**
     * 根据车牌号和工单状态查找授权状态不是“03：已授权，05：已完成”的任务信息
     * @param vo 参数
     * @return
     */
    VehicleTask selectByPlateAndTaskStatus(@Param("vo") SearchPlateVo vo);
    /**
     * 根据车牌号查找任务信息
     * @param plate 参数
     * @return
     */
    VehicleTask selectVehicleInfo(@Param("plate") String plate);

    /**
     * 分页获取收车公司派单任务列表
     *
     * @param vo 画面参数
     * @return
     */
    List<RecoveryTaskVo> selectRecoveryTaskList(@Param("vo") RecoveryVo vo);

    /**
     * 分页获取收车公司派单任务列表_一期老接口
     *
     * @param vo 画面参数
     * @return
     */
    List<RecoveryTaskVo> selectRecoveryTaskListOld(@Param("vo") RecoveryVo vo);

    /**
     * 分页获取派单管理列表
     *
     * @param vo 画面参数
     * @return
     */
    List<RecoveryTaskManagerVo> selectTaskManagerList(@Param("vo") RecoveryVo vo);

    /**
     * 根据车牌号和工单状态判定该任务是否为独家任务
     * @param vo 参数
     * @return
     */
    SoleTaskInfoVo selectSoleTask(@Param("vo") SearchPlateVo vo);
    /**
     * 派单任务总量及总服务费
     *
     * @param statisticsVo 画面参数
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    DataStatisticsSubVo selectSelfFinish(@Param("statisticsVo") DataStatisticsParamVo statisticsVo, @Param("recoveryCompanyId") String recoveryCompanyId);
    /**
     * 取在授权表中不在派单表中的自己公司业务员完成的任务总量及总服务费
     *
     * @param statisticsVo 画面参数
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    DataStatisticsSubVo selectOthersFinish(@Param("statisticsVo") DataStatisticsParamVo statisticsVo, @Param("recoveryCompanyId") String recoveryCompanyId);
    /**
     * 派单任务总量及总服务费  -- 小组
     *
     * @param statisticsVo 画面参数
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    List<DataStatisticsSubVo> selectGroupFinish(@Param("statisticsVo") DataStatisticsParamVo statisticsVo, @Param("recoveryCompanyId") String recoveryCompanyId);
    /**
     * 获取收车公司任务表中各种状态的任务数量
     *
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    List<HomePageStatisticsSubVo> selectCountByStatus(@Param("recoveryCompanyId") String recoveryCompanyId);

    /**
     * 根据车架号更新工单表的gps账号和密码
     * @param gpsSystemUserName 参数
     * @return
     */
    void updateByVehicleIdentifyNum(@Param("gpsSystemUserName") String gpsSystemUserName,
                                    @Param("gpsSystemUserPassword") String gpsSystemUserPassword,
                                    @Param("vehicleIdentifyNum") String vehicleIdentifyNum);

    /**
     * 首页获取滚播消息,查询最近10条工单完成信息
     * @return
     */
    List<FinishTaskVo> selectFinishInfo();

    /**
     * 更新任务分配flag为未分配
     *
     * @param vehicleTaskRecoveryIdList
     */
    void updateAssignmentFlag(@Param("vehicleTaskRecoveryIdList") List<String> vehicleTaskRecoveryIdList);
    /**
     * 更新授权小组id
     *
     * @param authoTaskIds
     * @param groupId
     */
    void updateAuthorizationGroupId(@Param("authoTaskIds") List<String> authoTaskIds,@Param("groupId") String groupId);
    /**
     * 更新完成小组id
     *
     * @param finishTaskIds
     * @param groupId
     */
    void updateFinishGroupId(@Param("finishTaskIds") List<String> finishTaskIds,@Param("groupId") String groupId);
    /**
     * 根据小组id清空授权小组id和完成小组id
     *
     * @param groupId
     */
    void updateAuthGroupIdNull(@Param("groupId") String groupId);
    /**
     * 根据小组id清空授权小组id和完成小组id
     *
     * @param groupId
     */
    void updateFinishGroupIdNull(@Param("groupId") String groupId);
    /**
     * 根据车牌号或车架号查找任务信息
     * @param plate 参数
     * @return
     */
    VehicleTask selectByPlateOrVehicleIdentifyNum(@Param("plate") String plate,@Param("taskStatus") String taskStatus);

    /**
     * 赏金三期更新工单表历史数据三址、fp所在省份
     * @return
     */
    void updateHistoryData();
}
