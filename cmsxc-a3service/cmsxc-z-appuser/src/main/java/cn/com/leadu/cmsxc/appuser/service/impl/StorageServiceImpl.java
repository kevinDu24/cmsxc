package cn.com.leadu.cmsxc.appuser.service.impl;

import cn.com.leadu.cmsxc.appuser.service.StorageService;
import cn.com.leadu.cmsxc.common.constant.enums.StorageOperateEnum;
import cn.com.leadu.cmsxc.common.constant.enums.StorageStateEnum;
import cn.com.leadu.cmsxc.common.constant.enums.UserRoleEnums;
import cn.com.leadu.cmsxc.common.util.ArrayUtil;
import cn.com.leadu.cmsxc.common.util.DistanceUtils;
import cn.com.leadu.cmsxc.data.appbusiness.repository.VehicleTaskRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.ParkingInfoRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.StorageHistoryRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.StorageInfoRepository;
import cn.com.leadu.cmsxc.data.system.repository.SystemUserRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTask;
import cn.com.leadu.cmsxc.pojo.appuser.entity.StorageHistory;
import cn.com.leadu.cmsxc.pojo.appuser.entity.StorageInfo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.ParkingInfoSubVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.ParkingInfoVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.RecoveryDataVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.ScanInfoVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ParkingDistanceVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.RecoveryEvidenceVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 入库Service实现类
 */
@Service
public class StorageServiceImpl implements StorageService {
    private static final Logger logger = LoggerFactory.getLogger(StorageServiceImpl.class);

    @Autowired
    private ParkingInfoRepository parkingInfoRepository;
    @Autowired
    private VehicleTaskRepository vehicleTaskRepository;
    @Autowired
    private SystemUserRepository systemUserRepository;
    @Autowired
    private StorageInfoRepository storageInfoRepository;
    @Autowired
    private StorageHistoryRepository storageHistoryRepository;

    /**
     * 获取入库状态
     * @param taskId 工单id
     * @return
     */
    public ResponseEntity<RestResponse> getStorageState(Long taskId){
        //到入库表中查询该工单对应的最新信息
        Example example = new Example(StorageInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", taskId);
        example.setOrderByClause(" update_time desc ");
        List<StorageInfo> storageInfoList = storageInfoRepository.selectByExampleList(example);
        //返回最新一条入库信息的入库状态，若无入库信息就为""
        if(ArrayUtil.isNullOrLengthZero(storageInfoList)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"",""),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,storageInfoList.get(0).getState(),""),
                HttpStatus.OK);
    }

    /**
     * 推荐附近停车场
     * @param lat 纬度
     * @param lon 经度
     * @return
     */
    public ResponseEntity<RestResponse> getNearbyParking(Long taskId, Double lat, Double lon){
        //判断该工单是属于哪个委托公司下的,获取委托公司id
        VehicleTask task = vehicleTaskRepository.selectByPrimaryKey(taskId);
        Example example = new Example(SystemUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", task.getLeaseCompanyUserName());
        String leaseId = systemUserRepository.selectOneByExample(example).getLeaseId();
        //通过委托公司id获取停车场信息
        List<ParkingDistanceVo> parkingList = parkingInfoRepository.selectParkingbyLeaseId(leaseId);
        if(ArrayUtil.isNullOrLengthZero(parkingList)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","暂无停车场可以选择，无法进行收车完成操作"),
                    HttpStatus.OK);
        }
        List<ParkingDistanceVo> distanceList = new ArrayList();
        //依次计算每个停车场距目标点的距离
        for(ParkingDistanceVo item : parkingList){
            item.setDistance(DistanceUtils.getDistance(lat,lon,Double.valueOf(item.getLat()),Double.valueOf(item.getLon()))/1000);
            distanceList.add(item);
        }
        //对list按照距离进行排序
        Collections.sort(distanceList, new Comparator<ParkingDistanceVo>(){
            public int compare(ParkingDistanceVo v1, ParkingDistanceVo v2) {
                //按距离升序排列
                if(v1.getDistance().compareTo(v2.getDistance()) > 0){
                    return 1;
                }
                if(v1.getDistance().compareTo(v2.getDistance()) == 0){
                    return 0;
                }
                return -1;
            }
        });
        //取前5个最近的停车场
        if(distanceList.size() <= 5){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,distanceList,""),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,distanceList.subList(0,5),""),
                HttpStatus.OK);
    }

    /**
     * 提交收车完成资料
     * @param vo 提交信息
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> uploadEvidence(RecoveryEvidenceVo vo, SystemUser loginUser){
        //check该车辆是否已经在入库流程中
        Example example = new Example(StorageInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", vo.getTaskId());
        criteria.andNotEqualTo("state", StorageStateEnum.FORBID_STORAGE.getType()); //终止入库，不算在入库流程中
        criteria.andNotEqualTo("state", StorageStateEnum.MODIFY_PARKING.getType()); //修改停车场，不算在入库流程中
        List<StorageInfo> storageInfoList = storageInfoRepository.selectByExampleList(example);
        if(ArrayUtil.isNotNullAndLengthNotZero(storageInfoList)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该任务状态已经变更，请返回列表后刷新重试"),
                    HttpStatus.OK);
        }
        Date now = new Date();
        //登录入库信息表
        StorageInfo storageInfo = new StorageInfo();
        storageInfo.setTaskId(vo.getTaskId()); //工单id
        storageInfo.setParkingId(vo.getParkingId()); //停车场id
        storageInfo.setRecoveryUserId(loginUser.getUserId()); //送车人用户名
        storageInfo.setVehicleUser(vo.getVehicleUser()); //车辆使用人
        storageInfo.setFinishImages(vo.getFinishImages()); //收车完成时的图片记录
        storageInfo.setFinishVideo(vo.getFinishVideo()); //收车完成时视频
        storageInfo.setFinishRemark(vo.getFinishRemark()); //收车完成时备注
        storageInfo.setState(StorageStateEnum.RECOVERY_FINISH.getType());//状态
        storageInfo.setCreateTime(now);
        storageInfo.setCreator(loginUser.getUserId());
        storageInfo.setUpdateTime(now);
        storageInfo.setUpdater(loginUser.getUserId());
        storageInfo = storageInfoRepository.insertOne(storageInfo);
        //登录入库日志表
        StorageHistory storageHistory = new StorageHistory();
        storageHistory.setStorageId(storageInfo.getId());
        storageHistory.setOperateContent(StorageOperateEnum.RECOVERY_FINISH.getType());
        storageHistory.setOperatorName(loginUser.getUserName());
        storageHistory.setOperator(loginUser.getUserId());
        storageHistory.setRemark(vo.getFinishRemark());
        storageHistory.setOperateTime(now);
        storageHistory.setCreateTime(now);
        storageHistory.setCreator(loginUser.getUserId());
        storageHistory.setUpdateTime(now);
        storageHistory.setUpdater(loginUser.getUserId());
        storageHistoryRepository.insertOne(storageHistory);
        //消息推送
        //// TODO: 2018/8/7  
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","提交成功"),
                HttpStatus.OK);
    }

    /**
     * 获取已选停车场及二维码信息
     * @param taskId 工单id
     * @return
     */
    public ResponseEntity<RestResponse> getSelectedParking(Long taskId, Double lat, Double lon){
        //到入库表中查询该工单对应的最新信息
        Example example = new Example(StorageInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", taskId);
        example.setOrderByClause(" update_time desc ");
        List<StorageInfo> storageInfoList = storageInfoRepository.selectByExampleList(example);
        //返回最新一条入库信息的入库状态，若无入库信息就为""
        if(ArrayUtil.isNullOrLengthZero(storageInfoList)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该任务状态已经变更，请返回列表后刷新重试"),
                    HttpStatus.OK);
        }
        //以最新的入库信息为准
        StorageInfo storageInfo = storageInfoList.get(0);
        //获取停车场相关信息
        ParkingInfoSubVo subVo = parkingInfoRepository.selectParkingById(storageInfo.getParkingId());
        subVo.setDistance(DistanceUtils.getDistance(lat,lon,Double.valueOf(subVo.getLat()),Double.valueOf(subVo.getLon()))/1000);
        //获取二维码中信息
        ScanInfoVo scanInfoVo = new ScanInfoVo();
        //获取工单相关信息
        VehicleTask vehicleTask = vehicleTaskRepository.selectByPrimaryKey(storageInfo.getTaskId());
        scanInfoVo.setPlate(vehicleTask.getPlate()); //车牌号
        scanInfoVo.setBrand(vehicleTask.getBrand() == null ? vehicleTask.getVehicleType() : vehicleTask.getBrand() + " "
                + (vehicleTask.getVehicleType() == null ? "" : vehicleTask.getVehicleType())); //车型
        scanInfoVo.setManufacturer(vehicleTask.getManufacturer()); // 品牌
        scanInfoVo.setVehicleColor(vehicleTask.getVehicleColor()); //颜色
        scanInfoVo.setEngineNo(vehicleTask.getEngineNo()); //发送机号
        scanInfoVo.setVehicleIdentifyNum(vehicleTask.getVehicleIdentifyNum()); //车架号
        //获取送车人相关信息
        ScanInfoVo recoveryUser = systemUserRepository.selectInfoByUserId(storageInfo.getRecoveryUserId());
        scanInfoVo.setName(recoveryUser.getName()); //送车人姓名
        scanInfoVo.setPhoneNum(recoveryUser.getPhoneNum()); //送车人手机号
        scanInfoVo.setRecoveryCompany(recoveryUser.getRecoveryCompany()); //送车人所属收车公司
        //构建最终返回对象
        ParkingInfoVo result = new ParkingInfoVo();
        result.setParkingInfo(subVo);
        result.setScanInfo(scanInfoVo);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,result,""),
                HttpStatus.OK);
    }

    /**
     * 送车人上传资料
     * @param vo 提交信息
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> uploadData(RecoveryDataVo vo, SystemUser loginUser){
        //check该车辆是否已经在入库流程中
        Example example = new Example(StorageInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", vo.getTaskId());
        List<String> states = new ArrayList();
        states.add(StorageStateEnum.ARRIVE_PARKING.getType());
        states.add(StorageStateEnum.MANAGER_REBACK.getType());
        criteria.andIn("state", states); //只有状态为02或者04时才可以提交
        List<StorageInfo> storageInfoList = storageInfoRepository.selectByExampleList(example);
        if(ArrayUtil.isNullOrLengthZero(storageInfoList)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该任务状态已经变更，请返回列表后刷新重试"),
                    HttpStatus.OK);
        }
        Date now = new Date();
        //获取当前入库信息
        StorageInfo storageInfo = storageInfoList.get(0);
        //如果是内勤点击，则跳过内勤审核环节
        if(UserRoleEnums.RECOVERY_MANAGER.getType().equals(loginUser.getUserRole())){
            storageInfo.setState(StorageStateEnum.FILES_UPLOAD.getType()); //状态变为待停车场人员提交资料
        } else {
            storageInfo.setState(StorageStateEnum.MANAGER_CONFIRM.getType()); //状态变为待内勤确认
        }
        storageInfo.setVehicleRecoveryReport(vo.getVehicleRecoveryReport()); //车辆回收报告
        storageInfo.setVehicleNormalInfo(vo.getVehicleNormalInfo()); //车辆基本情况
        storageInfo.setStorageFinanceList(vo.getStorageFinanceList()); //入库财务交接单
        storageInfo.setVehicleAppearance(vo.getVehicleAppearance()); //车辆外观照
        storageInfo.setVehicleGood(vo.getVehicleGood()); //车上物品
        storageInfo.setOtherImages(vo.getOtherImages()); //其他附件
        storageInfo.setVideo(vo.getVideo()); //视频
        storageInfo.setUpdateTime(now);
        storageInfo.setUpdater(loginUser.getUserId());
        storageInfoRepository.updateByPrimaryKey(storageInfo);
        //登录入库日志表
        StorageHistory storageHistory = new StorageHistory();
        storageHistory.setStorageId(storageInfo.getId());
        storageHistory.setOperateContent(StorageOperateEnum.RECOVERY_SUBMIT.getType());
        storageHistory.setOperatorName(loginUser.getUserName());
        storageHistory.setOperator(loginUser.getUserId());
        storageHistory.setOperateTime(now);
        storageHistory.setCreateTime(now);
        storageHistory.setCreator(loginUser.getUserId());
        storageHistory.setUpdateTime(now);
        storageHistory.setUpdater(loginUser.getUserId());
        storageHistoryRepository.insertOne(storageHistory);
        //消息推送
        if(UserRoleEnums.RECOVERY_MANAGER.getType().equals(loginUser.getUserRole())){
            //// TODO: 2018/8/9  内勤点击，如何推送
        } else {
            //// TODO: 2018/8/9  组长点击，如何推送
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","提交成功"),
                HttpStatus.OK);
    }
}
