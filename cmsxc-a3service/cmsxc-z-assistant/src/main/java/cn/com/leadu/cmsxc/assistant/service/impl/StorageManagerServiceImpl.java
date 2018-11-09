package cn.com.leadu.cmsxc.assistant.service.impl;

import cn.com.leadu.cmsxc.assistant.service.StorageManagerService;
import cn.com.leadu.cmsxc.common.constant.enums.StorageOperateEnum;
import cn.com.leadu.cmsxc.common.constant.enums.StorageStateEnum;
import cn.com.leadu.cmsxc.common.constant.enums.UserRoleEnums;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.appbusiness.repository.VehicleTaskRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.StorageAttachmentRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.StorageHistoryRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.StorageInfoRepository;
import cn.com.leadu.cmsxc.data.system.repository.SystemUserRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTask;
import cn.com.leadu.cmsxc.pojo.appuser.entity.StorageAttachment;
import cn.com.leadu.cmsxc.pojo.appuser.entity.StorageHistory;
import cn.com.leadu.cmsxc.pojo.appuser.entity.StorageInfo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.ScanInfoVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ParkingDataVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ParkingDdlVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.StorageListVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.SubmitedInfoVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 入库管理Service实现类
 */
@Service
public class StorageManagerServiceImpl implements StorageManagerService {
    private static final Logger logger = LoggerFactory.getLogger(StorageManagerServiceImpl.class);

    @Autowired
    private StorageInfoRepository storageInfoRepository;
    @Autowired
    private StorageHistoryRepository storageHistoryRepository;
    @Autowired
    private VehicleTaskRepository vehicleTaskRepository;
    @Autowired
    private SystemUserRepository systemUserRepository;
    @Autowired
    private StorageAttachmentRepository storageAttachmentRepository;

    /**
     * 入库管理列表查询_库管/停车场公司管理员
     *
     * @param userId 用户名
     * @param userRole 角色code
     * @param status 状态 01：入库中，02：已结束
     * @param parkingId 所选停车场id
     * @param plate 车牌号
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    public ResponseEntity<RestResponse> getParkingStorageList(String userId, String userRole, String parkingId,
                                                              String status, String plate, int page, int size){
        // 只有停车场公司管理员和库管可以调用此接口
        if(!UserRoleEnums.PARKING_ADMIN.getType().equals(userRole) && !UserRoleEnums.PARKING_MANAGER.getType().equals(userRole)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","当前登录人无查询权限，请联系管理员"),
                    HttpStatus.OK);
        }
        // 获取列表信息
        List<StorageListVo> resultList =  storageInfoRepository.selectParkingStorageList(userId, userRole, parkingId, status, plate, page, size);
        if(resultList == null || resultList.isEmpty()){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","数据加载完毕"),
                  HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, resultList,""),
                HttpStatus.OK);
    }

    /**
     * 获得停车场管理员下对应的停车场集合
     *
     * @param userId 用户名
     * @return
     */
    public ResponseEntity<RestResponse> getParkingListAdmin(String userId){
        // 获取下拉框集合
        List<ParkingDdlVo> resultList =  storageInfoRepository.selectParkingDdlAdmin(userId);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, resultList,""),
                HttpStatus.OK);
    }

    /**
     * 获取该任务最新入库状态
     *
     * @param storageId 用户名
     * @return
     */
    public ResponseEntity<RestResponse> getLatestState(String storageId){
        // 获取对应的入库信息
        StorageInfo storageInfo =  storageInfoRepository.selectByPrimaryKey(storageId);
        //若该车还未抵达停车场
        if(StorageStateEnum.RECOVERY_FINISH.getType().equals(storageInfo.getState())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该车尚未到达，请在该车抵达停车场并扫码入库后再行操作"),
                    HttpStatus.OK);
        }
        //如果是终止入库、入库完成、修改停车场状态，则给出统一提示
        if(StorageStateEnum.STORAGE_FINISH.getType().equals(storageInfo.getState())
                || StorageStateEnum.FORBID_STORAGE.getType().equals(storageInfo.getState())
                || StorageStateEnum.MODIFY_PARKING.getType().equals(storageInfo.getState())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该任务状态已变更，请返回列表刷新后重试"),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, storageInfo.getState(),""),
                HttpStatus.OK);
    }

    /**
     * 确认抵达停车场
     * @param storageId 入库id
     * @param loginUser 当前登录人信息
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> arriveParking(String storageId, SystemUser loginUser){
        //通过入库id找出对应的入库信息
        StorageInfo storageInfo = storageInfoRepository.selectByPrimaryKey(storageId);
        // 如果状态不是收车完成，则提示相应信息
        if(!StorageStateEnum.RECOVERY_FINISH.getType().equals(storageInfo.getState())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该任务状态已经变更，请返回列表后刷新重试"),
                    HttpStatus.OK);
        }
        Date now = new Date();
        storageInfo.setState(StorageStateEnum.ARRIVE_PARKING.getType()); // 设定状态为"02:抵达停车场"
        storageInfo.setUpdater(loginUser.getUserId());
        storageInfo.setUpdateTime(now);
        storageInfoRepository.updateByPrimaryKey(storageInfo);
        //登录入库日志表
        StorageHistory storageHistory = new StorageHistory();
        storageHistory.setStorageId(storageInfo.getId());
        storageHistory.setOperateContent(StorageOperateEnum.ARRIVE_PARKING.getType());
        storageHistory.setOperatorName(loginUser.getUserName());
        storageHistory.setOperator(loginUser.getUserId());
        storageHistory.setOperateTime(now);
        storageHistory.setCreateTime(now);
        storageHistory.setCreator(loginUser.getUserId());
        storageHistory.setUpdateTime(now);
        storageHistory.setUpdater(loginUser.getUserId());
        storageHistoryRepository.insertOne(storageHistory);
        // TODO: 2018/8/9 消息推送
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, "","操作成功"),
                HttpStatus.OK);
    }

    /**
     * 获取入库管理车辆详情
     * @param storageId 入库id
     * @return
     */
    public ResponseEntity<RestResponse> getVehicleInfo(String storageId){
        ScanInfoVo scanInfoVo = getScanInfoVo(storageId);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, scanInfoVo,""),
                HttpStatus.OK);
    }

    /**
     * 停车场相关人员上传资料
     * @param vo 资料对象
     * @param loginUser 当前登录人信息
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> uploadData(ParkingDataVo vo, SystemUser loginUser){
        //通过入库id找出对应的入库信息
        StorageInfo storageInfo = storageInfoRepository.selectByPrimaryKey(vo.getStorageId());
        // 如果状态不是收车完成，则提示相应信息
        if(!StorageStateEnum.FILES_UPLOAD.getType().equals(storageInfo.getState())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该任务状态已经变更，请返回列表后刷新重试"),
                    HttpStatus.OK);
        }
        Date now = new Date();
        storageInfo.setState(StorageStateEnum.FILES_AUDITING.getType()); // 设定状态为"06:资料审核中"
        storageInfo.setUpdater(loginUser.getUserId());
        storageInfo.setUpdateTime(now);
        storageInfoRepository.updateByPrimaryKey(storageInfo);
        //登录入库附件表(停车场)
        StorageAttachment storageAttachment = new StorageAttachment();
        BeanUtils.copyProperties(vo,storageAttachment);
        storageAttachment.setCreator(loginUser.getUserId());
        storageAttachment.setCreateTime(now);
        storageAttachment.setUpdater(loginUser.getUserId());
        storageAttachment.setUpdateTime(now);
        storageAttachmentRepository.insertOne(storageAttachment);
        //登录入库日志表
        StorageHistory storageHistory = new StorageHistory();
        storageHistory.setStorageId(storageInfo.getId());
        storageHistory.setOperateContent(StorageOperateEnum.PARKING_SUBMIT.getType());
        storageHistory.setOperatorName(loginUser.getUserName());
        storageHistory.setOperator(loginUser.getUserId());
        storageHistory.setOperateTime(now);
        storageHistory.setCreateTime(now);
        storageHistory.setCreator(loginUser.getUserId());
        storageHistory.setUpdateTime(now);
        storageHistory.setUpdater(loginUser.getUserId());
        storageHistoryRepository.insertOne(storageHistory);
        // TODO: 2018/8/9 消息推送
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, "","提交成功"),
                HttpStatus.OK);
    }

    /**
     * 获取已提交页面信息接口
     * @param storageId 入库id
     * @return
     */
    public ResponseEntity<RestResponse> getSubmitedInfo(String storageId){
        //通过入库id找出对应的入库信息
        StorageInfo storageInfo = storageInfoRepository.selectByPrimaryKey(storageId);
        //获得车辆详情信息
        ScanInfoVo scanInfoVo = getScanInfoVo(storageId);
        //获取停车场上传的附件信息
        Example example = new Example(StorageAttachment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("storageId", storageId);
        StorageAttachment storageAttachment = storageAttachmentRepository.selectOneByExample(example);
        ParkingDataVo attachmentVo = new ParkingDataVo();
        BeanUtils.copyProperties(storageAttachment,attachmentVo);
        //构建返回结果
        SubmitedInfoVo result = new SubmitedInfoVo();
        result.setVehicleInfo(scanInfoVo); //设定车辆信息
        result.setAttachment(attachmentVo); //设定附件信息
        // 如果状态为 "07'退回待修改，则设定驳回原因
        if(StorageStateEnum.REBACK_MODIFY.getType().equals(storageInfo.getState())){
            result.setLeaseRefuseRemark(StringUtil.isNull(storageInfo.getLeaseRefuseRemark()) ? "无" : storageInfo.getLeaseRefuseRemark());
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, result,""),
                HttpStatus.OK);
    }

    /**
     * 获取车辆详情及送车人信息
     * @param storageId 入库id
     * @return
     */
    private ScanInfoVo getScanInfoVo(String storageId) {
        //通过入库id找出对应的入库信息
        StorageInfo storageInfo = storageInfoRepository.selectByPrimaryKey(storageId);
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
        return scanInfoVo;
    }
}
