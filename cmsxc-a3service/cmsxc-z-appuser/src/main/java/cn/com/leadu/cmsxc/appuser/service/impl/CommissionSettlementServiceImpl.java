package cn.com.leadu.cmsxc.appuser.service.impl;

import cn.com.leadu.cmsxc.appuser.service.CommissionSettlementService;
import cn.com.leadu.cmsxc.appuser.service.CoreSystemInterface;
import cn.com.leadu.cmsxc.appuser.service.KilometresInterface;
import cn.com.leadu.cmsxc.common.constant.enums.CommissionOperateEnums;
import cn.com.leadu.cmsxc.common.constant.enums.CommissionStatusEnums;
import cn.com.leadu.cmsxc.common.constant.enums.StorageStateEnum;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.appuser.repository.*;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appuser.entity.*;
import cn.com.leadu.cmsxc.pojo.appuser.vo.*;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/8/6.
 *
 * 佣金结算impl
 */
@Service
public class CommissionSettlementServiceImpl implements CommissionSettlementService{
    private static final Logger logger = LoggerFactory.getLogger(CommissionSettlementServiceImpl.class);
    @Autowired
    private ParkingInfoRepository parkingInfoRepository;
    @Autowired
    private KilometresInterface kilometresInterface;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StorageInfoRepository storageInfoRepository;
    @Autowired
    private CaseRecordRepository caseRecordRepository;
    @Autowired
    private CoreSystemInterface coreSystemInterface;
    @Autowired
    private CommissionSettlementRepository commissionSettlementRepository;
    @Autowired
    private CommissionSettlementHistoryRepository commissionSettlementHistoryRepository;
    @Autowired
    private CutPaymentItemRepository cutPaymentItemRepository;

    private static String output = "json";
    private static String ak = "t6z915PcMl4ZnWWg2nFunrj2hD6adrVH";
    private static String coord_type = "wgs84";
    private static String URL= "GainCommissionSettlement"; // 调用主系统的.url

    /**
     * 获取佣金结算列表信息
     *
     * @param systemUser 用户信息
     * @param vo 画面参数
     * @return
     */
    public ResponseEntity<RestResponse> getCommissionSettlementList(SystemUser  systemUser, CommissionSettlementParamVo vo){
        // 根据收车公司id，获取佣金结算列表数据
        List<CommissionSettlementListVo> commissionSettlementListVoList = storageInfoRepository.selectCommissionSettlementList( systemUser.getRecoveryCompanyId(),vo.getStatus(),vo.getPlate(),vo.getPage(),vo.getSize());
        if(commissionSettlementListVoList != null && commissionSettlementListVoList.size() != 0 ){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,commissionSettlementListVoList,""),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未获取到列表信息"),
                HttpStatus.OK);
    }

    /**
     * 获取佣金结算列表数量
     *
     * @param systemUser 用户信息
     * @param vo 画面参数
     * @return
     */
    public ResponseEntity<RestResponse> getCommissionSettlementCount(SystemUser  systemUser, CommissionSettlementParamVo vo){
        CommissionSettlementCountVo commissionSettlementCountVo = new CommissionSettlementCountVo();
        // 获取待申请数量  01-待申请
        Integer applyCount = storageInfoRepository.selectCommissionSettlementCount( systemUser.getRecoveryCompanyId(),"01",vo.getPlate());
        commissionSettlementCountVo.setApplyCount(applyCount);
        // 获取审核中数量  02-审核中
        Integer auditingCount = storageInfoRepository.selectCommissionSettlementCount( systemUser.getRecoveryCompanyId(),"02",vo.getPlate());
        commissionSettlementCountVo.setAuditingCount(auditingCount);
        // 获取被退回数量  03-被退回
        Integer rebackCount = storageInfoRepository.selectCommissionSettlementCount( systemUser.getRecoveryCompanyId(),"03",vo.getPlate());
        commissionSettlementCountVo.setRebackCount(rebackCount);
        // 获取已通过数量  04-已通过
        Integer passCount = storageInfoRepository.selectCommissionSettlementCount( systemUser.getRecoveryCompanyId(),"04",vo.getPlate());
        commissionSettlementCountVo.setPassCount(passCount);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,commissionSettlementCountVo,""),
                HttpStatus.OK);
    }

    /**
     *  获取佣金结算详情
     *
     * @param taskId 工单id
     * @return
     */
    public ResponseEntity<RestResponse> getCommissionSettlementDetail(String taskId ){
        CommissionSettlementDetailVo vo = commissionSettlementRepository.selectCommissionSettlementDetail(taskId);
        if(vo != null){
            List<CutPaymentItemVo> cutPaymentItems = cutPaymentItemRepository.selectByCommissionSettlementInfoId(vo.getCommissionSettlementInfoId());
            vo.setCutPaymentItemList(cutPaymentItems);
            // TODO 调主系统获取
            // 调用主统接口，根据车架号，获取佣金详情信息
//        String result = coreSystemInterface.getCommissionDetail(URL, task.getVehicleIdentifyNum());
//        // json 解析
        CommissionDetailResVo commissionDetailResVo = new CommissionDetailResVo();
//        try {
//            commissionDetailResVo = objectMapper.readValue(result, CommissionDetailResVo.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","系统异常，请重试"),
//                    HttpStatus.OK);
//        }
//        if(!"00000000".equals(commissionDetailResVo.getCode())){
//            logger.error(commissionDetailResVo.getMessage());
//            if(StringUtil.isNotNull(commissionDetailResVo.getMessage())){
//                return new ResponseEntity<RestResponse>(
//                        RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"",commissionDetailResVo.getMessage()),
//                        HttpStatus.OK);
//            }
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","获取佣金详情失败"),
//                    HttpStatus.OK);
//        }
//        if(commissionDetailResVo.getData() != null && commissionDetailResVo.getData().size() != 0 ){
//            for(CommissionDetailVo detailVo : commissionDetailResVo.getData()){
//                if("佣金总额".equals(detailVo.getItemName())){
//                    // 佣金总额
//                    vo.setTotalAmount(detailVo.getItemName());
//                    commissionDetailResVo.getData().remove(detailVo);
//                }
//            }
//            vo.setCommissionDetailVoList(commissionDetailResVo.getData());
//        }
            // 佣金总额
            vo.setTotalAmount("23000");
            List<CommissionDetailVo> commissionDetailVoList = new ArrayList<>();
            CommissionDetailVo commissionDetailVo1 = new CommissionDetailVo();
            commissionDetailVo1.setItemName("剩余本金");
            commissionDetailVo1.setItemValue("19324");
            commissionDetailVoList.add(commissionDetailVo1);
            CommissionDetailVo commissionDetailVo2 = new CommissionDetailVo();
            commissionDetailVo2.setItemName("收车难度系数");
            commissionDetailVo2.setItemValue("19%(Ⅱ)");
            commissionDetailVoList.add(commissionDetailVo2);
            CommissionDetailVo commissionDetailVo3 = new CommissionDetailVo();
            commissionDetailVo3.setItemName("保底佣金");
            commissionDetailVo3.setItemValue("15000");
            commissionDetailVoList.add(commissionDetailVo3);
            CommissionDetailVo commissionDetailVo4 = new CommissionDetailVo();
            commissionDetailVo4.setItemName("上限");
            commissionDetailVo4.setItemValue("50000");
            commissionDetailVoList.add(commissionDetailVo4);

            vo.setCommissionDetailVoList(commissionDetailVoList);
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,vo,""),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未获取到工单信息"),
                HttpStatus.OK);

    }

    /**
     * 批量申请佣金结算
     *
     * @param systemUser 用户信息
     * @param commissionApplyParamVoList  画面参数
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> applyCommissionSettlement(SystemUser systemUser, List<CommissionApplyParamVo> commissionApplyParamVoList){
        List<CommissionSettlementInfo> commissionSettlementInfoList = new ArrayList<>();
        List<CommissionSettlementHistory> commissionSettlementHistoryList = new ArrayList<>();
        Date nowDate = new Date();
        if(commissionApplyParamVoList != null && commissionApplyParamVoList.size() !=0 ){
            for(CommissionApplyParamVo commissionApplyParamVo : commissionApplyParamVoList){
                // 判断是否申请过，申请过不可以再申请
                CommissionSettlementInfo info = selectByTaskId(commissionApplyParamVo.getTaskId());
                if(info != null){
                    return new ResponseEntity<RestResponse>(
                            RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"",info.getTaskId() + "已申请，不可重复申请"),
                            HttpStatus.OK);
                }
                CommissionSettlementInfo commissionSettlementInfo = new CommissionSettlementInfo();
                // 工单id
                commissionSettlementInfo.setTaskId(commissionApplyParamVo.getTaskId());
                // 入库信息id
                commissionSettlementInfo.setStorageInfoId(commissionApplyParamVo.getStorageInfoId());
                // 申请时间
                commissionSettlementInfo.setApplyDate(nowDate);
                // 申请备注
                commissionSettlementInfo.setApplyRemark(commissionApplyParamVo.getApplyRemark());
                // 使用板车公里数
                if(commissionApplyParamVo.getFlatBedDistance() != null){
                    commissionSettlementInfo.setFlatBedDistance(Double.valueOf(commissionApplyParamVo.getFlatBedDistance()));
                }
                // 使用板车费用
                if(commissionApplyParamVo.getFlatBedFee() != null){
                    commissionSettlementInfo.setFlatBedFee(Double.valueOf(commissionApplyParamVo.getFlatBedFee()));
                }
                // 使用钥匙费用
                if(commissionApplyParamVo.getKeyFee() != null ){
                    commissionSettlementInfo.setKeyFee(Double.valueOf(commissionApplyParamVo.getKeyFee()));
                }
                // 申请收车公司id
                commissionSettlementInfo.setRecoveryCompanyId(systemUser.getRecoveryCompanyId());
                // 使用板车起点
                commissionSettlementInfo.setStartAddress(commissionApplyParamVo.getStartAddress());
                // 使用板车起点纬度
                commissionSettlementInfo.setStartLat(commissionApplyParamVo.getStartLat());
                // 使用板车起点经度
                commissionSettlementInfo.setStartLon(commissionApplyParamVo.getStartLon());
                // 状态
                commissionSettlementInfo.setStatus(CommissionStatusEnums.APPLY.getCode());
                // 申请人账号
                commissionSettlementInfo.setUserId(systemUser.getUserId());
                commissionSettlementInfo.setCreateTime(nowDate);
                commissionSettlementInfo.setCreator(systemUser.getUserId());
                commissionSettlementInfo.setUpdateTime(nowDate);
                commissionSettlementInfo.setUpdater(systemUser.getUserId());
                commissionSettlementInfoList.add(commissionSettlementInfo);
                // 佣金结算日志信息
                CommissionSettlementHistory commissionSettlementHistory = new CommissionSettlementHistory();
                // 拥挤结算信息id
                commissionSettlementHistory.setCommissionSettlementInfoId(commissionSettlementInfo.getId());
                // 操作内容
                commissionSettlementHistory.setOperateContent(CommissionOperateEnums.APPLY.getCode());
                // 操作时间
                commissionSettlementHistory.setOperateDate(nowDate);
                // 操作人
                commissionSettlementHistory.setOperateUserId(systemUser.getUserId());
                // 备注信息
                commissionSettlementHistory.setRemark(commissionSettlementInfo.getApplyRemark());
                // 操作人姓名
                commissionSettlementHistory.setOperateUserName(systemUser.getUserName());
                commissionSettlementHistory.setCreateTime(nowDate);
                commissionSettlementHistory.setCreator(systemUser.getUserId());
                commissionSettlementHistory.setUpdater(systemUser.getUserId());
                commissionSettlementHistory.setUpdateTime(nowDate);
                commissionSettlementHistoryList.add(commissionSettlementHistory);
            }
            commissionSettlementRepository.insertMore(commissionSettlementInfoList);
            commissionSettlementHistoryRepository.insertMore(commissionSettlementHistoryList);
            // TODO 佣金结算时消息推送
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","申请成功"),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未获取到申请信息"),
                HttpStatus.OK);
    }
    /**
     * 佣金结算申请流程被退回后，批量提交佣金结算申请
     *
     * @param systemUser 用户信息
     * @param commissionApplyParamVoList  画面参数
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> submitCommissionSettlement(SystemUser systemUser, List<CommissionApplyParamVo> commissionApplyParamVoList){
        List<CommissionSettlementHistory> commissionSettlementHistoryList = new ArrayList<>();
        Date nowDate = new Date();
        if(commissionApplyParamVoList != null && commissionApplyParamVoList.size() !=0 ){
            for(CommissionApplyParamVo commissionApplyParamVo : commissionApplyParamVoList){
                // 根据工单id获取佣金结算的申请信息
                CommissionSettlementInfo info = selectByTaskId(commissionApplyParamVo.getTaskId());
                if(info != null){
                    // 申请备注
                    info.setApplyRemark(commissionApplyParamVo.getApplyRemark());
                    // 使用板车公里数
                    if(commissionApplyParamVo.getFlatBedDistance() != null){
                        info.setFlatBedDistance(Double.valueOf(commissionApplyParamVo.getFlatBedDistance()));
                    }
                    // 使用板车费用
                    if(commissionApplyParamVo.getFlatBedFee() != null){
                        info.setFlatBedFee(Double.valueOf(commissionApplyParamVo.getFlatBedFee()));
                    }
                    // 使用钥匙费用
                    if(commissionApplyParamVo.getKeyFee() != null ){
                        info.setKeyFee(Double.valueOf(commissionApplyParamVo.getKeyFee()));
                    }
                    // 申请收车公司id
                    info.setRecoveryCompanyId(systemUser.getRecoveryCompanyId());
                    // 使用板车起点
                    info.setStartAddress(commissionApplyParamVo.getStartAddress());
                    // 使用板车起点纬度
                    info.setStartLat(commissionApplyParamVo.getStartLat());
                    // 使用板车起点经度
                    info.setStartLon(commissionApplyParamVo.getStartLon());
                    // 状态
                    info.setStatus(CommissionStatusEnums.APPLY.getCode());
                    // 申请人账号
                    info.setUserId(systemUser.getUserId());
                    info.setUpdateTime(nowDate);
                    info.setUpdater(systemUser.getUserId());
                    commissionSettlementRepository.updateByPrimaryKey(info);
                    // 佣金结算日志信息
                    CommissionSettlementHistory commissionSettlementHistory = new CommissionSettlementHistory();
                    // 拥挤结算信息id
                    commissionSettlementHistory.setCommissionSettlementInfoId(info.getId());
                    // 操作内容
                    commissionSettlementHistory.setOperateContent(CommissionOperateEnums.APPLY.getCode());
                    // 操作时间
                    commissionSettlementHistory.setOperateDate(nowDate);
                    // 操作人
                    commissionSettlementHistory.setOperateUserId(systemUser.getUserId());
                    // 备注信息
                    commissionSettlementHistory.setRemark(info.getApplyRemark());
                    // 操作人姓名
                    commissionSettlementHistory.setOperateUserName(systemUser.getUserName());
                    commissionSettlementHistory.setCreateTime(nowDate);
                    commissionSettlementHistory.setCreator(systemUser.getUserId());
                    commissionSettlementHistory.setUpdater(systemUser.getUserId());
                    commissionSettlementHistory.setUpdateTime(nowDate);
                    commissionSettlementHistoryList.add(commissionSettlementHistory);
                }
            }
            commissionSettlementHistoryRepository.insertMore(commissionSettlementHistoryList);
            // TODO 佣金结算时消息推送
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","提交成功"),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未获取到申请信息"),
                HttpStatus.OK);
    }




    /**
     * 根据工单id与收车公司id，获取板车使用起点地址
     *
     * @param systemUser 用户信息
     * @param taskId 工单id
     * @return
     */
    public ResponseEntity<RestResponse> getStartAddress(SystemUser  systemUser, String taskId){
        // 根据工单id与收车公司id，获取板车使用起点地址
        List<StartLonLatVo> startLonLatVoList = caseRecordRepository.selectStartAddressInfo(taskId, systemUser.getRecoveryCompanyId());
        if(startLonLatVoList != null && startLonLatVoList.size() != 0 ){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,startLonLatVoList,""),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未获取到地址信息"),
                HttpStatus.OK);
    }

    /**
     * 获取公里数
     *
     * @param systemUser
     * @param vo 画面参数
     * @return
     */
    public ResponseEntity<RestResponse> getKilometres(SystemUser  systemUser, KilometresParamVo vo){
        String origins = "";
        String destinations = "";
        String jsonData = "";
        // 根据工单号获取入库完成的入库信息
       StorageInfo storageInfo = selectByTaskIdAndState(Long.valueOf(vo.getTaskId()), StorageStateEnum.STORAGE_FINISH.getType());
        if(storageInfo == null ){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未获取到入库信息"),
                    HttpStatus.OK);
        }
        // 根据停车场id获取停车场的经纬度
        ParkingInfo parkingInfo = selectParkingInfoById(storageInfo.getParkingId());
        if(parkingInfo == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未获取到停车场信息"),
                    HttpStatus.OK);
        }
        if(StringUtil.isNotNull(vo.getStartLon()) && StringUtil.isNotNull(vo.getStartLat())){
            origins = vo.getStartLat().concat(",").concat(vo.getStartLon());
        }
        if(StringUtil.isNotNull(parkingInfo.getLon()) && StringUtil.isNotNull(parkingInfo.getLat())){
            destinations = parkingInfo.getLat().concat(",").concat(parkingInfo.getLon());
        }
        try {
            //调用外部接口
            jsonData = kilometresInterface.getKilometres(origins,destinations,output,ak,coord_type);
        } catch (Exception e){
            e.printStackTrace();
            logger.error("获取公里数接口异常");
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","获取公里数失败"),
                    HttpStatus.OK);
        }
        KilometresResVo res = new KilometresResVo();
        try {
            res = objectMapper.readValue(jsonData, KilometresResVo.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","系统异常，请重试"),
                    HttpStatus.OK);
        }
        //如果返回结果为成功
        if("0".equals(res.getStatus())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,res.getResult(),""),
                    HttpStatus.OK);
        }else {
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","获取公里数失败"+ res.getMessage()),
                    HttpStatus.OK);
        }
    }
    /**
     * 根据任务工单id和状态获取该用户最新入库信息
     *
     * @param id 任务id
     * @return
     */
    public ParkingInfo selectParkingInfoById(String id){
        if(StringUtil.isNotNull(id)) {
            Example example = new Example(ParkingInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id", id);
            criteria.andEqualTo("state", "1");
            example.setOrderByClause(" update_time desc ");
            return parkingInfoRepository.selectOneByExample(example);
        }
        return null;
    }
    /**
     * 根据任务工单id和状态获取该用户最新入库信息
     *
     * @param taskId 任务id
     * @param state 状态
     * @return
     */
    public StorageInfo selectByTaskIdAndState(Long taskId, String state){
        if(StringUtil.isNotNull(taskId)&& StringUtil.isNotNull(state)) {
            Example example = new Example(StorageInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("taskId", taskId);
            criteria.andEqualTo("state", state);
            example.setOrderByClause(" update_time desc ");
            return storageInfoRepository.selectOneByExample(example);
        }
        return null;
    }
    /**
     * 根据任务工单id获取佣金结算申请信息
     *
     * @param taskId 任务id
     * @return
     */
    public CommissionSettlementInfo selectByTaskId(String taskId){
        if(StringUtil.isNotNull(taskId)) {
            Example example = new Example(StorageInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("taskId", taskId);
            return commissionSettlementRepository.selectOneByExample(example);
        }
        return null;
    }

}
