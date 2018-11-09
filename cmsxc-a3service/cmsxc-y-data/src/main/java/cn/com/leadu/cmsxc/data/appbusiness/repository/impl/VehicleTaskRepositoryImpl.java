package cn.com.leadu.cmsxc.data.appbusiness.repository.impl;

import cn.com.leadu.cmsxc.data.appbusiness.dao.VehicleTaskDao;
import cn.com.leadu.cmsxc.data.appbusiness.repository.VehicleTaskRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTask;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryTaskManagerVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryTaskVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.*;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 车辆任务工单
 */
@Component
public class VehicleTaskRepositoryImpl extends AbstractBaseRepository<VehicleTaskDao,VehicleTask> implements VehicleTaskRepository {

    /**
     * 通过主键获取工单
     * @param id
     * @return
     */
    public VehicleTask selectByPrimaryKey(Long id){
        return super.selectByPrimaryKey(id);
    }

    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    @Override
    public List<VehicleTask> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }
    /**
     * 查看首页车辆任务工单数量
     *
     * @param nowDate 当前时间
     * @return
     */
    public int selectCountAll(Date nowDate){
        return baseDao.selectCountAll(nowDate);
    }
    /**
     * 登录车辆任务工单信息
     * @param vehicleTask
     */
    public VehicleTask insertOne(VehicleTask vehicleTask){
        return super.insert(vehicleTask);
    }

    /**
     * @Title:
     * @Description:  批量更新工单信息
     * @param vehicleTasks
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 06:04:42
     */
    public List<VehicleTask> updateListByMapper(List<VehicleTask> vehicleTasks){
        return super.updateListByMapper(vehicleTasks);
    }

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
   public List<RewardForVehicleVo> selectByTaskStatusAndMore( VehicleTaskVo vehicleTaskVo, String taskStatus,String userId, int page, int size){
        return baseDao.selectByTaskStatusAndMore(vehicleTaskVo,taskStatus,userId, page, size );
    }
    /**
     * 用户查询历史
     *
     * @param userId 用户id
     * @param taskStatus 工单状态
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    public List<RewardForVehicleVo> userSearchHistory( String userId ,String taskStatus, int page, int size){
        return baseDao.selectUserSearchHistory(userId,taskStatus,page,size);
    }
    /**
     * 获取查看过某辆车的所有用户的用户id
     *
     * @param plate 车牌号
     * @return
     */
    public List<String> selectUserIdByPlate(String plate){
        return baseDao.selectUserIdByPlate(plate);
    }

    /**
     * 根据主键更新表
     * @param vehicleTask
     */
    @Override
    public void updateByPrimaryKey(VehicleTask vehicleTask) {
        super.updateByPrimaryKey(vehicleTask);
    }

    /**
     * 根据车牌号和工单状态查找授权状态不是“03：已授权，05：已完成”的任务信息
     * @param vo 参数
     * @return
     */
    public VehicleTask selectByPlateAndTaskStatus(SearchPlateVo vo){
        return baseDao.selectByPlateAndTaskStatus(vo);
    }
    /**
     * 根据车牌号和工单状态查找任务信息
     *
     * @param plate 车牌号
     * @return
     */
    public VehicleTask selectVehicleInfo(String plate){
        return baseDao.selectVehicleInfo(plate);
    }

    /**
     * 分页获取收车公司派单任务列表
     *
     * @param vo 画面参数
     * @return
     */
    public List<RecoveryTaskVo> selectRecoveryTaskList(RecoveryVo vo){
        return baseDao.selectRecoveryTaskList(vo);
    }
    /**
     * 分页获取收车公司派单任务列表_一期老接口
     *
     * @param vo 画面参数
     * @return
     */
    public List<RecoveryTaskVo> selectRecoveryTaskListOld(RecoveryVo vo){
        return baseDao.selectRecoveryTaskListOld(vo);
    }
    /**
     * 根据条件获取唯一数据
     *
     * @param example
     * @return
     */
    public VehicleTask selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }

    /**
     * 分页获取派单管理列表
     *
     * @param vo 画面参数
     * @return
     */
    public List<RecoveryTaskManagerVo> getTaskManagerList(RecoveryVo vo) {
        return baseDao.selectTaskManagerList(vo);
    }

    /**
     * 根据车牌号和工单状态判定该任务是否为独家任务
     * @param vo 参数
     * @return
     */
    public SoleTaskInfoVo selectSoleTask(SearchPlateVo vo){
        return baseDao.selectSoleTask(vo);
    }




    /**
     * 派单任务总量及总服务费
     *
     * @param statisticsVo 画面参数
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    public DataStatisticsSubVo selectSelfFinish(DataStatisticsParamVo statisticsVo, String recoveryCompanyId) {
        return baseDao.selectSelfFinish(statisticsVo, recoveryCompanyId);
    }

    /**
     * 取在授权表中不在派单表中的自己公司业务员完成的任务总量及总服务费
     *
     * @param statisticsVo 画面参数
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    public DataStatisticsSubVo selectOthersFinish(DataStatisticsParamVo statisticsVo, String recoveryCompanyId) {
        return baseDao.selectOthersFinish(statisticsVo, recoveryCompanyId);
    }
    /**
     * 派单任务总量及总服务费 -- 小组
     *
     * @param statisticsVo 画面参数
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    public List<DataStatisticsSubVo> selectGroupFinish(DataStatisticsParamVo statisticsVo, String recoveryCompanyId) {
        return baseDao.selectGroupFinish(statisticsVo, recoveryCompanyId);
    }
    /**
     * 获取收车公司任务表中各种状态的任务数量
     *
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    public List<HomePageStatisticsSubVo> selectCountByStatus(String recoveryCompanyId) {
        return baseDao.selectCountByStatus(recoveryCompanyId);
    }

    /**
     * 根据车架号更新工单表的gps账号和密码
     *
     * @param gpsSystemUserName gps账号
     * @param gpsSystemUserPassword gps密码
     * @param vehicleIdentifyNum 车架号
     */
    public void updateByVehicleIdentifyNum(String gpsSystemUserName, String gpsSystemUserPassword, String vehicleIdentifyNum){
        baseDao.updateByVehicleIdentifyNum(gpsSystemUserName,gpsSystemUserPassword,vehicleIdentifyNum);
    }

    /**
     * 首页获取滚播消息,查询最近10条工单完成信息
     */
    public List<FinishTaskVo> selectFinishInfo(){
        return baseDao.selectFinishInfo();
    }
    /**
     * 更新任务分配flag为未分配
     *
     * @param vehicleTaskRecoveryIdList
     */
    public void updateAssignmentFlag(List<String> vehicleTaskRecoveryIdList){
        baseDao.updateAssignmentFlag(vehicleTaskRecoveryIdList);
    }
    /**
     * 更新授权小组id
     *
     * @param authoTaskIds
     * @param groupId
     */
    public void updateAuthorizationGroupId(List<String> authoTaskIds, String groupId){
        baseDao.updateAuthorizationGroupId(authoTaskIds, groupId);
    }
    /**
     * 更新完成小组id
     *
     * @param finishTaskIds
     * @param groupId
     */
    public void updateFinishGroupId(List<String> finishTaskIds, String groupId){
        baseDao.updateFinishGroupId(finishTaskIds, groupId);
    }
    /**
     * 根据小组id清空授权小组id
     *
     * @param groupId
     */
    public void updateAuthGroupIdNull( String groupId){
        baseDao.updateAuthGroupIdNull( groupId);
    }
    /**
     * 根据小组id清空 完成小组id
     *
     * @param groupId
     */
    public void updateFinishGroupIdNull( String groupId){
        baseDao.updateFinishGroupIdNull( groupId);
    }
    /**
     * 根据车牌号或车架号查找任务信息
     * @param plate 参数
     * @return
     */
    public VehicleTask selectByPlateOrVehicleIdentifyNum( String plate,String taskStatus){
        return baseDao.selectByPlateOrVehicleIdentifyNum( plate,taskStatus );
    }

    /**
     * 赏金三期更新工单表历史数据三址、fp所在省份
     */
    public void updateHistoryData(){
        baseDao.updateHistoryData();
    }
}
