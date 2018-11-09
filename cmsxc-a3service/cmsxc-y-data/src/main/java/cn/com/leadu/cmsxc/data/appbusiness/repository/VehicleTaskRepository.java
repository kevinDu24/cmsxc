package cn.com.leadu.cmsxc.data.appbusiness.repository;

import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTask;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryTaskManagerVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryTaskVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.*;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 车辆任务工单
 */
public interface VehicleTaskRepository {
    /**
     * 通过主键获取工单
     * @param id
     * @return
     */
    public VehicleTask selectByPrimaryKey(Long id);
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<VehicleTask> selectByExampleList(Example example);
    /**
     * 根据条件获取唯一数据
     *
     * @param example
     * @return
     */
    VehicleTask selectOneByExample(Example example);
    /**
     * 查看首页车辆任务工单数量
     *
     * @param nowDate 当前时间
     * @return
     */
    int selectCountAll(Date nowDate);
    /**
     * 登录车辆任务工单信息
     *
     * @param vehicleTask
     */
    VehicleTask insertOne(VehicleTask vehicleTask);

    /**
     * @Title:
     * @Description:  批量更新工单信息
     * @param vehicleTasks
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 06:04:42
     */
    List<VehicleTask> updateListByMapper(List<VehicleTask> vehicleTasks);

    /**
     * 根据价格区间，gps有无，线索有无，违章有无等条件，搜索车辆信息
     *
     * @param vehicleTaskVo 画面参数信息
     * @param taskStatus 任务状态
     * @param userId 用户id
     * @param page 当前页
     * @param size 每页数目
     * @return
     */
    List<RewardForVehicleVo> selectByTaskStatusAndMore( VehicleTaskVo vehicleTaskVo, String taskStatus,String userId, int page, int size);
    /**
     * 用户查询历史
     *
     * @param userId 用户id
     * @param taskStatus 工单状态
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    List<RewardForVehicleVo> userSearchHistory( String userId ,String taskStatus, int page, int size);

    /**
     * 获取查看过某辆车的所有用户的用户id
     *
     * @param plate 车牌号
     * @return
     */
    List<String> selectUserIdByPlate(String plate);

    /**
     * 根据主键更新表
     * @param vehicleTask
     */
    void updateByPrimaryKey(VehicleTask vehicleTask);

    /**
     * 根据车牌号和工单状态查找授权状态不是“03：已授权，05：已完成”的任务信息
     *
     * @param vo 参数
     * @return
     */
    VehicleTask selectByPlateAndTaskStatus(SearchPlateVo vo );
    /**
     * 根据车牌号查找任务信息
     *
     * @param plate 车牌号
     * @return
     */
    VehicleTask selectVehicleInfo(String plate);

    /**
     * 分页获取收车公司派单任务列表
     *
     * @param vo 画面参数
     * @return
     */
    List<RecoveryTaskVo> selectRecoveryTaskList(RecoveryVo vo);

    /**
     * 分页获取收车公司派单任务列表_一期老接口
     *
     * @param vo 画面参数
     * @return
     */
    public List<RecoveryTaskVo> selectRecoveryTaskListOld(RecoveryVo vo);

    /**
     * 分页获取派单管理列表
     *
     * @param vo 画面参数
     * @return
     */
    List<RecoveryTaskManagerVo> getTaskManagerList(RecoveryVo vo);

    /**
     * 根据车牌号和工单状态判定该任务是否为独家任务
     * @param vo 参数
     * @return
     */
    SoleTaskInfoVo selectSoleTask(SearchPlateVo vo);

    /**
     * 取在授权表中不在派单表中的自己公司业务员完成的任务总量及总服务费
     *
     * @param statisticsVo 画面参数
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    DataStatisticsSubVo selectOthersFinish(DataStatisticsParamVo statisticsVo, String recoveryCompanyId);
    /**
     * 派单任务总量及总服务费
     *
     * @param statisticsVo 画面参数
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    DataStatisticsSubVo selectSelfFinish(DataStatisticsParamVo statisticsVo, String recoveryCompanyId);
    /**
     * 派单任务及悬赏列表任务完成总量及总服务费 -- 小组
     *
     * @param statisticsVo 画面参数
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    List<DataStatisticsSubVo> selectGroupFinish(DataStatisticsParamVo statisticsVo, String recoveryCompanyId);
    /**
     * 获取收车公司任务表中各种状态的任务数量
     *
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    List<HomePageStatisticsSubVo> selectCountByStatus(String recoveryCompanyId);

    /**
     * 根据车架号更新工单表的gps账号和密码
     *
     * @param gpsSystemUserName gps账号
     * @param gpsSystemUserPassword gps密码
     * @param vehicleIdentifyNum 车架号
     */
    void updateByVehicleIdentifyNum(String gpsSystemUserName, String gpsSystemUserPassword, String vehicleIdentifyNum);

    /**
     * 首页获取滚播消息,查询最近10条工单完成信息
     */
    List<FinishTaskVo> selectFinishInfo();


    /**
     * 更新任务分配flag为未分配
     *
     * @param vehicleTaskRecoveryIdList
     */
    void updateAssignmentFlag(List<String> vehicleTaskRecoveryIdList);
    /**
     * 更新完成小组id
     *
     * @param finishTaskIds
     * @param groupId
     */
    void updateFinishGroupId(List<String> finishTaskIds, String groupId);
    /**
     * 更新授权小组id
     *
     * @param authoTaskIds
     * @param groupId
     */
    void updateAuthorizationGroupId(List<String> authoTaskIds, String groupId);
    /**
     * 根据小组id清空 完成小组id
     *
     * @param groupId
     */
    void updateFinishGroupIdNull( String groupId);
    /**
     * 根据小组id清空授权小组id
     *
     * @param groupId
     */
    void updateAuthGroupIdNull( String groupId);
    /**
     * 根据车牌号或车架号查找任务信息
     * @param plate 参数
     * @return
     */
    VehicleTask selectByPlateOrVehicleIdentifyNum( String plate,String taskStatus);

    /**
     * 赏金三期更新工单表历史数据三址、fp所在省份
     */
    void updateHistoryData();

}
