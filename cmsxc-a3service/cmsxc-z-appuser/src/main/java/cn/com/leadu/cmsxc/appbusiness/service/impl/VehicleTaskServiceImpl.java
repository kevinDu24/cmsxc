package cn.com.leadu.cmsxc.appbusiness.service.impl;

import cn.com.leadu.cmsxc.appbusiness.service.VehicleTaskService;
import cn.com.leadu.cmsxc.appuser.service.ClueService;
import cn.com.leadu.cmsxc.common.constant.enums.ClientTypeEnums;
import cn.com.leadu.cmsxc.common.constant.enums.ServiceFeeEnum;
import cn.com.leadu.cmsxc.common.util.ArrayUtil;
import cn.com.leadu.cmsxc.common.util.CommonUtil;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.appbusiness.repository.VehicleTaskRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.GpsAppInfoRepository;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTask;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.ClueDetail;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.VehicleTaskResultVo;
import cn.com.leadu.cmsxc.pojo.appuser.entity.ClueInfo;
import cn.com.leadu.cmsxc.pojo.appuser.entity.GpsAppInfo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.RewardForVehicleVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.SearchPlateVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.VehicleTaskVo;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 车辆任务工单
 */
@Service
public class VehicleTaskServiceImpl implements VehicleTaskService{
    @Autowired
    private VehicleTaskRepository vehicleTaskRepository;
    @Autowired
    private VehicleTaskService vehicleTaskService;
    @Autowired
    private GpsAppInfoRepository gpsAppInfoRepository;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private ClueService clueService;
    /**
     * 根据车牌号获取车辆任务工单信息
     * @param plate 车牌号
     * @return
     */
    public VehicleTask selectVehicleTeskByplate(String plate){
        if(StringUtil.isNotNull(plate)) {
            Example example = new Example(VehicleTask.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("plate", plate);
            example.setOrderByClause(" update_time desc ");
            return vehicleTaskRepository.selectOneByExample(example);
        }
        return null;
    }
    /**
     * 根据车牌号和工单状态获取车辆任务工单信息
     * @param plate 车牌号
     * @param taskStatus 状态
     * @return
     */
    public VehicleTask selectByPlateAndTaskStatus(String plate,String taskStatus ){
        if(StringUtil.isNotNull(plate)) {
            return vehicleTaskRepository.selectByPlateOrVehicleIdentifyNum(plate,taskStatus);
        }
        return null;
    }
    /**
     * 根据任务id获取车辆任务工单信息
     *
     * @param taskId 任务id
     * @return
     */
    public VehicleTask selectVehicleTeskById(String taskId){
        if(StringUtil.isNotNull(taskId)) {
            Example example = new Example(VehicleTask.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id", taskId);
            return vehicleTaskRepository.selectOneByExample(example);
        }
        return null;
    }
    /**
     * 根据车牌号和工单状态获取车辆任务工单信息
     * @param plate 车牌号
     * @param taskStatus 工单状态
     * @param nowDate 当前时间
     * @return
     */
    public VehicleTask selectByPlateAndTaskStatus(String plate, String taskStatus, Date nowDate){
        if(StringUtil.isNotNull(plate)&& StringUtil.isNotNull(taskStatus)) {
            SearchPlateVo vo = new SearchPlateVo();
            vo.setNowDate(nowDate);
            vo.setPlate(plate);
            vo.setTaskStatus(taskStatus);
            return vehicleTaskRepository.selectByPlateAndTaskStatus(vo);
        }
        return null;
    }
    /**
     * 根据车牌号和工单状态获取车辆任务工单信息
     * @param plate 车牌号
     * @return
     */
    public VehicleTask selectVehicleInfo(String plate){
        if(StringUtil.isNotNull(plate)) {
            return vehicleTaskRepository.selectVehicleInfo(plate);
        }
        return null;
    }
    /**
     * 取工单状态为正常的车辆任务工单数量
     * @param nowDate 当前时间
     * @return
     */
    public int selectCountAll( Date nowDate ){
        return vehicleTaskRepository.selectCountAll(nowDate);
    }
    /**
     * 根据价格区间，gps有无，线索有无，违章有无等条件，搜索车辆信息
     *
     * @param vehicleTaskVo 画面参数信息
     * @param taskStatus 工单状态
     * @param userId 用户id
     * @return
     */
    public List<RewardForVehicleVo> selectByTaskStatusAndMore(String userId, String taskStatus , VehicleTaskVo vehicleTaskVo){
        // 拼接like的查询条件
        if(vehicleTaskVo != null && vehicleTaskVo.getVehicleProvinces() != null
                && !vehicleTaskVo.getVehicleProvinces().isEmpty() && !vehicleTaskVo.getVehicleProvinces().contains("全部")){
            for(String str : vehicleTaskVo.getVehicleProvinces()){
                String string = CommonUtil.likePartten(str);
                vehicleTaskVo.getList().add(string);
            }
        }
        // 价格区间设置“0：不限”时，价格区间范围设空
        if(vehicleTaskVo != null && StringUtil.isNotNull(vehicleTaskVo.getPriceRange()) && ServiceFeeEnum.RANGE_X.value().equals(vehicleTaskVo.getPriceRange()) ){
            vehicleTaskVo.setPriceRange("");
        }
        // 设置当前时间
        vehicleTaskVo.setNowDate(new Date());
        List<RewardForVehicleVo> vehicleTaskList = vehicleTaskRepository.selectByTaskStatusAndMore(vehicleTaskVo,taskStatus,userId,vehicleTaskVo.getPage(),vehicleTaskVo.getSize());
        if (ArrayUtil.isNotNullAndLengthNotZero(vehicleTaskList))
            return vehicleTaskList;
        return null;
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
    public List<RewardForVehicleVo> userSearchHistory(String taskStatus , String userId, int page, int size){
        List<RewardForVehicleVo> vehicleTaskList = vehicleTaskRepository.userSearchHistory(userId,taskStatus, page, size);
        if (ArrayUtil.isNotNullAndLengthNotZero(vehicleTaskList))
            return vehicleTaskList;
        return null;
    }

    /**
     * 根据任务id获取该任务的详情信息
     *
     * @param taskId 任务id
     * @param userId 用户id
     * @param plate 车牌号
     * @return
     */
    @Transactional
    public VehicleTaskResultVo findTaskDetail(String userId, String taskId, String plate){
        // 根据任务id查找车辆信息
        VehicleTask task = vehicleTaskService.selectVehicleTeskById(taskId);
        VehicleTaskResultVo vo;
        if(task != null ){
            // 车辆详情信息
            vo = new VehicleTaskResultVo(task);
            // 委托公司用户名
            vo.setLeaseCompanyUserName(task.getLeaseCompanyUserName());
            // 根据委托公司用户名查询下载连接
            Example example = new Example(GpsAppInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("leaseCompanyUserName", task.getLeaseCompanyUserName());
            GpsAppInfo gpsAppInfo  = gpsAppInfoRepository.selectOneByExample(example);
            if (gpsAppInfo != null){
                // 获取安卓请求还是iOS请求
                String client = httpServletRequest.getHeader("client");
                vo.setAppDownLoadUrl(ClientTypeEnums.IOS.getCode().equals(client) ? gpsAppInfo.getIosUrl() : ClientTypeEnums.ANDROID.getCode().equals(client) ? gpsAppInfo.getAndroidUrl() : "" );
            }
            // 查询车辆所有线索信息
            List<ClueInfo>  clueList = clueService.selectByPlate(plate,task.getVehicleIdentifyNum());
            if(clueList != null && !clueList.isEmpty()){
                for(ClueInfo clue : clueList){
                    // 返回用车辆线索详情
                    ClueDetail clueDetail = new ClueDetail();
                    // 线索地址
                    clueDetail.setAppAddr(clue.getAppAddr());
                    // 线索上传时间
                    clueDetail.setUploadDate(clue.getUploadDate());
                    vo.getClueDetails().add(clueDetail);
                }
            }
            return vo;
        }
        return null;
    }
}
