package cn.com.leadu.cmsxc.appuser.service.impl;

import cn.com.leadu.cmsxc.appbusiness.service.VehicleTaskService;
import cn.com.leadu.cmsxc.appuser.config.FileUploadProperties;
import cn.com.leadu.cmsxc.appuser.service.AuthorizationService;
import cn.com.leadu.cmsxc.appuser.service.MessageService;
import cn.com.leadu.cmsxc.appuser.service.SystemUserService;
import cn.com.leadu.cmsxc.common.constant.Constants;
import cn.com.leadu.cmsxc.common.constant.enums.*;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.common.util.*;
import cn.com.leadu.cmsxc.common.util.push.UmengPushUtils_XCGJ;
import cn.com.leadu.cmsxc.data.appuser.repository.*;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTask;
import cn.com.leadu.cmsxc.pojo.appuser.entity.*;
import cn.com.leadu.cmsxc.pojo.appuser.vo.AuthorizationListVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.ViewMaterialVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.*;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 授权service实现类
 */
@Service
public class AuthorizationServiceImpl implements AuthorizationService{
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationServiceImpl.class);
    @Autowired
    private VehicleAuthorizationRepository vehicleAuthorizationRepository;

    @Autowired
    private AuthorizationHistoryRepository authorizationHistoryRepository;

    @Autowired
    private VehiclePhotoPathRepository vehiclePhotoPathRepository;

    @Autowired
    private FileUploadProperties fileUploadProperties;

    @Autowired
    private VehicleTaskService vehicleTaskService;

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private MessageCenterRepository messageCenterRepository;

    @Autowired
    private MessagePushHistoryRepository messagePushHistoryRepository;

    @Autowired
    private UserDeviceInfoRepository userDeviceInfoRepository;

    /**
     * 申请授权前，获取此用户针对此车辆之前的最新申请信息
     *
     * @param userId 用户id
     * @param plate 车牌号
     * @return
     */
    public ResponseEntity<RestResponse> beforeApplyAuthorization(String userId, String plate){
        // 根据车牌号取得车辆任务工单状态为正常的车辆信息,并且收车公司派单表中为已过期，且不是已授权和已完成的
        VehicleTask task = vehicleTaskService.selectByPlateAndTaskStatus(plate,TaskStatusEnums.NORMAL.getType(), new Date());
        if(task == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该车辆状态无法进行授权申请"),
                    HttpStatus.OK);
        }
        // 查询该车辆有效的授权状态
        VehicleAuthorization vehicleAuthorization = authorizationService.selectByTaskId(task.getId());
        if(vehicleAuthorization != null && (AuthorizationStatusEnums.AUTHORIZETED.getType().equals(vehicleAuthorization.getAuthorizationStatus())
                || AuthorizationStatusEnums.FINISH.getType().equals(vehicleAuthorization.getAuthorizationStatus()))){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该车辆已授权给他人，无法进行授权申请"),
                    HttpStatus.OK);
        }
        // 根据车牌号取得车辆任务工单状态为正常的车辆信息
//        VehicleTask task = vehicleTaskService.selectByPlateAndTaskStatus(plate,TaskStatusEnums.NORMAL.getType());
//        if(task == null){
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该车辆当前状态为已完成/已取消状态，无法进行授权申请"),
//                    HttpStatus.OK);
//        }
        // 取得最新申请授权信息
        ViewMaterialVo viewMaterialVo = vehicleAuthorizationRepository.selectByUserIdAndTaskId(userId, String.valueOf(task.getId()));
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,viewMaterialVo,""),
                HttpStatus.OK);
    }

    /**
     * 申请授权
     * @param userId 用户id
     * @param paramterMap 参数集合 （车牌号，申请人姓名，申请人手机号，申请人证件号，备注）
     * @return
     */
    @Transactional
   public ResponseEntity<RestResponse> applyAuthorization(String userId, Map<String,String> paramterMap){
        // 根据车牌号取得车辆任务工单状态为正常的车辆信息
        VehicleTask task = vehicleTaskService.selectByPlateAndTaskStatus(paramterMap.get("plate"),TaskStatusEnums.NORMAL.getType());
        if(task == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","不存在此车辆"),
                    HttpStatus.OK);
        }
        //判断当前登录用户是否申请过此单，并且状态是申请中或已授权
        List<VehicleAuthorization> vehicleAuthorizationList = getAuthorizationByUserIdAndTaskId(task.getId(),userId);
        if(ArrayUtil.isNotNullAndLengthNotZero(vehicleAuthorizationList)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","申请失败，您在申请此单授权期间不可重复申请"),
                    HttpStatus.OK);
        }
        Date nowDate = new Date();
        // 附件关联表（照片路径保存）
        List<VehiclePhotoPath> vehiclePhotoPathList = new ArrayList<>();
        // 登录单车授权表
        VehicleAuthorization vehicleAuthorization = new VehicleAuthorization();
        // 工单id
        vehicleAuthorization.setTaskId(task.getId());
        // 用户id
        vehicleAuthorization.setUserId(userId);
        // 申请人姓名---除去姓名中的表情符号
        vehicleAuthorization.setApplicantName(CommonUtil.filterEmoji(paramterMap.get("applicantName")));
        // 申请人手机号
        vehicleAuthorization.setApplicantPhone(paramterMap.get("applicantPhone"));
        // 申请人证件号---除去表情符号
        vehicleAuthorization.setApplicantIdentityNum(CommonUtil.filterEmoji(paramterMap.get("applicantIdentityNum")));
        // 授权状态
        vehicleAuthorization.setAuthorizationStatus(AuthorizationStatusEnums.APPLYING.getType());
        // 备注---除去表情符号
        vehicleAuthorization.setRemark(CommonUtil.filterEmoji(paramterMap.get("remark")));
        // 地址
        vehicleAuthorization.setAddress(paramterMap.get("address"));
        // 经度
        vehicleAuthorization.setLon(paramterMap.get("lon"));
        // 纬度
        vehicleAuthorization.setLat(paramterMap.get("lat"));
        // 申请开始时间
        vehicleAuthorization.setApplyStartDate(nowDate);
        // 申请结束时间
//        vehicleAuthorization.setApplyEndDate();
        // 汽车照片附件标识
        vehicleAuthorization.setPhotoUuid(UUID.randomUUID().toString());
        // 设置是否授权延期标志为初始状态0：未延期
        vehicleAuthorization.setIsDelay(AuthDelayEnum.NO_DELAY.getCode());
        // 操作时间
        vehicleAuthorization.setOperateDate(nowDate);
        vehicleAuthorization.setCreateTime(nowDate);
        vehicleAuthorization.setUpdateTime(nowDate);
        vehicleAuthorization.setCreator(userId);
        vehicleAuthorization.setUpdater(userId);
        vehicleAuthorizationRepository.insertOne(vehicleAuthorization);
        // 登录授权日志表
        AuthorizationHistory history = new AuthorizationHistory();
        // 授权id
        history.setAuthorizationId(vehicleAuthorization.getId());
        // 用户id
        history.setUserId(userId);
        // 操作时间
        history.setOperateTime(nowDate);
        // 操作内容
        history.setOperateContent(OperateContentEnums.APPLYING.getType());
        // 备注
        history.setRemark(OperateContentEnums.APPLYING.getValue());
        authorizationHistoryRepository.insertOne(history);
        // 分别上传附件信息，保存附件访问url
        if(StringUtil.isNotNull(paramterMap.get("vedio"))){
            addVehiclePhotoPath(userId,paramterMap.get("vedio"), ApplyAuthFileTypeEnum.VEDIO.getCode(), vehicleAuthorization,vehiclePhotoPathList );
        }
        if(StringUtil.isNotNull(paramterMap.get("frontPhoto"))){
            addVehiclePhotoPath(userId,paramterMap.get("frontPhoto"), ApplyAuthFileTypeEnum.FRONT_PHOTO.getCode(), vehicleAuthorization,vehiclePhotoPathList );
        }
        if(StringUtil.isNotNull(paramterMap.get("exteriorPhoto"))){
            addVehiclePhotoPath(userId,paramterMap.get("exteriorPhoto"), ApplyAuthFileTypeEnum.EXTERIOR_PHOTO.getCode(), vehicleAuthorization,vehiclePhotoPathList );
        }
        if(StringUtil.isNotNull(paramterMap.get("photoUrl1"))){
            addVehiclePhotoPath(userId,paramterMap.get("photoUrl1"), ApplyAuthFileTypeEnum.OTHER_PHOTO.getCode(), vehicleAuthorization,vehiclePhotoPathList );
        }
        if(StringUtil.isNotNull(paramterMap.get("photoUrl2"))){
            addVehiclePhotoPath(userId,paramterMap.get("photoUrl2"), ApplyAuthFileTypeEnum.OTHER_PHOTO.getCode(),vehicleAuthorization,vehiclePhotoPathList );
        }
        if(StringUtil.isNotNull(paramterMap.get("photoUrl3"))){
            addVehiclePhotoPath(userId,paramterMap.get("photoUrl3"), ApplyAuthFileTypeEnum.OTHER_PHOTO.getCode(),vehicleAuthorization,vehiclePhotoPathList );
        }
        if(StringUtil.isNotNull(paramterMap.get("photoUrl4")) ){
            addVehiclePhotoPath(userId,paramterMap.get("photoUrl4"), ApplyAuthFileTypeEnum.OTHER_PHOTO.getCode(),vehicleAuthorization,vehiclePhotoPathList );
        }
        if(StringUtil.isNotNull(paramterMap.get("photoUrl5"))){
            addVehiclePhotoPath(userId,paramterMap.get("photoUrl5"), ApplyAuthFileTypeEnum.OTHER_PHOTO.getCode(),vehicleAuthorization,vehiclePhotoPathList );
        }
        vehiclePhotoPathRepository.insertMore(vehiclePhotoPathList);
        // 根据用户名取得金融公司一级管理员信息
        SystemUser systemUser = systemUserService.selectSystemUserByUserId(task.getLeaseCompanyUserName());
        // 根据金融公司id和用户角色获取审批人员信息
        List<SystemUser> leaseUserList = systemUserService.selectLeaseAdminUserList(systemUser.getLeaseId(), String.valueOf(task.getId()));
        if (leaseUserList != null && leaseUserList.size() != 0) {
            for(SystemUser leaseUser : leaseUserList){
                if(StringUtil.isNotNull(leaseUser.getUserPhone())){
                    //录消息中心表数据----委托公司审批人员
                    String content = MessageTempletConstants.templet_205.replace(MessageTempletConstants.templet_plate,task.getPlate())
                            .replace(MessageTempletConstants.templet_user_id,userId);
                    MessageCenter messageCenter = insertMessageCenter(content,leaseUser.getUserId(),nowDate,userId, MessageTriggerEnum.AUTH_205.getCode());
                    messageCenterRepository.insertOne(messageCenter);
                    // 给审批人员发送短信
                    try{
                        String messageContent = "【赏金寻车】您好！" + userId + "用户申请了车辆【" + task.getPlate() + "】的收车任务。请您尽快审批处理";
                        messageService.sendContentMessage(leaseUser.getUserPhone(),messageContent,"赏金寻车","申请授权时给车辆租赁公司的联系人发送短信", Constants.SENDMESSAGE_APPLY_SCAN);
                    }catch (Exception e){
                        e.printStackTrace();
                        logger.error("发送短信error",e);
                    }
                    //给委托公司审批人员推送消息
                    //获取委托公司审批人员的登录设备信息
                    UserDeviceInfo userDeviceInfo = getLeaseDeviceInfo(leaseUser.getUserId());
                    if(userDeviceInfo!= null){ //登录了才会推送
                        push(leaseUser.getUserId(),nowDate,userDeviceInfo.getClient().toString(),userDeviceInfo.getDeviceToken(),content,MessageTriggerEnum.AUTH_205.getNane());
                    }
                }
            }
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","申请成功"),
                HttpStatus.OK);
    }

    /**
     * 根据
     * @param taskId 工单id
     * @param userId 当前登录用户id
     * @return
     */
    private List<VehicleAuthorization> getAuthorizationByUserIdAndTaskId(Long taskId, String userId) {
        Example example = new Example(VehicleAuthorization.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", taskId);
        criteria.andEqualTo("userId", userId);
        List<String> statusList = new ArrayList();
        statusList.add(AuthorizationStatusEnums.APPLYING.getType());
        statusList.add(AuthorizationStatusEnums.AUTHORIZETED.getType());
        criteria.andIn("authorizationStatus", statusList);
        return vehicleAuthorizationRepository.selectByExampleList(example);
    }

    /**
     * 登录附件关联表（照片路径保存）
     *
     * @param userId 用户id
     * @param url 图片
     * @param type 类型 0：视频，1：图片，2：正面照，3：外观照
     * @param vehicleAuthorization 单车授权表
     * @param vehiclePhotoPathList
     */
    private void addVehiclePhotoPath(String userId,String url, String type, VehicleAuthorization vehicleAuthorization,List<VehiclePhotoPath> vehiclePhotoPathList){
        VehiclePhotoPath path = new VehiclePhotoPath();
        Date nowDate = new Date();
        // 附件路径
        path.setPhotoUrl(url);
        // 附件类型
        path.setType(type);
        // 汽车照片附件标识
        path.setPhotoUuid(vehicleAuthorization.getPhotoUuid());
        path.setRemark("申请授权");
        path.setCreateTime(nowDate);
        path.setUpdateTime(nowDate);
        path.setCreator(userId);
        path.setUpdater(userId);
        vehiclePhotoPathList.add(path);
    }
    /**
     * 登录附件关联表（照片路径保存）--------弃用
     *
     * @param userId 用户id
     * @param photoMsg 图片
     * @param vehicleAuthorization 单车授权表
     * @param vehiclePhotoPathList
     */
    private void addVehiclePhotoPath(String userId,MultipartFile photoMsg, VehicleAuthorization vehicleAuthorization,List<VehiclePhotoPath> vehiclePhotoPathList){
        VehiclePhotoPath path = new VehiclePhotoPath();
        Date nowDate = new Date();
        // 文件名 = 时间序列 + 文件原名
        String fileName = DateUtil.dateToStr(nowDate, DateUtil.formatStr_yyyyMMddHHmmssS_) + "_"+ FileUtil.getFileName(photoMsg.getOriginalFilename());
        String uploadDate = DateUtil.dateToStr(nowDate,DateUtil.formatStr_yyyyMMddS_);
        // 文件保存，返回访问路径
        String photoMagPath = FileUtil.saveFile(fileName, fileUploadProperties.getAuthorizationPath() + uploadDate + "/", photoMsg, fileUploadProperties.getRequestAuthorizationPath() + uploadDate + "/" );
        path.setPhotoUrl(photoMagPath);
        // 汽车照片附件标识
        path.setPhotoUuid(vehicleAuthorization.getPhotoUuid());
        path.setCreateTime(nowDate);
        path.setUpdateTime(nowDate);
        path.setCreator(userId);
        path.setUpdater(userId);
        vehiclePhotoPathList.add(path);
    }

    /**
     * 分页查询指定用户指定授权状态的授权列表
     *
     * @param userId 用户id
     * @param authorizationStatus 授权状态
     * @param page 当前页
     * @param size 每页个数
     * @return
     */
    public ResponseEntity<RestResponse> getApplyAuthorizationList(String userId, String authorizationStatus, int page, int size){
        if(StringUtil.isNull(userId)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","当前登录用户手机号为空"),
                    HttpStatus.OK);
        }
        String nowDate = DateUtil.dateToStr(new Date(),DateUtil.formatStr_yyyyMMddHHmmssS);
        // 根据用户id和授权状态获取授权列表分页数据
        List<AuthorizationListVo> authorizationList = vehicleAuthorizationRepository.selectByUserIdAndAuthorizationstatus(
                userId, authorizationStatus,nowDate, page, size);
        if(authorizationList == null || authorizationList.isEmpty()){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","数据加载完毕"),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,authorizationList,""),
                HttpStatus.OK);
    }

    /**
     * 申请资料信息预览
     *
     * @param authorizationId 授权id
     * @return
     */
    public ResponseEntity<RestResponse> viewMaterial( String authorizationId){
        ViewMaterialVo viewMaterialVo = new ViewMaterialVo();
        // 取得申请授权信息
        List<ViewMaterialVo> authorizationList = vehicleAuthorizationRepository.selectByAuthorizationId(authorizationId);
        if(authorizationList == null || authorizationList.isEmpty()){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","暂无数据"),
                    HttpStatus.OK);
        }
        // 返回申请资料信息
        for(ViewMaterialVo vo : authorizationList){
            // 申请人姓名
            viewMaterialVo.setApplicantName(vo.getApplicantName());
            // 申请人电话
            viewMaterialVo.setApplicantPhone(vo.getApplicantPhone());
            // 申请人证件号
            viewMaterialVo.setApplicantIdentityNum(vo.getApplicantIdentityNum());
            // 备注
            viewMaterialVo.setRemark(vo.getRemark());
            // 图片url list
            viewMaterialVo.getPhotoUrls().add(vo.getPhotoUrl());
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,viewMaterialVo,""),
                HttpStatus.OK);
    }
    /**
     * 取消申请授权
     *
     * @param authorizationId 授权id
     * @param userid 用户id
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> cancelApplyAuthorization(String userid, String authorizationId){
        if(StringUtil.isNull(userid)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","当前登录用户手机号为空"),
                    HttpStatus.OK);
        }
        Date nowDate = new Date();
        // 根据授权id获取授权信息
        VehicleAuthorization vehicleAuthorization = selectByAuthorizationId(authorizationId);
        // 更新单车授权表
        if(vehicleAuthorization != null){
            // 授权状态
            vehicleAuthorization.setAuthorizationStatus(AuthorizationStatusEnums.CANCEL.getType());
            // 操作时间
            vehicleAuthorization.setOperateDate(nowDate);
            // 结束时间
            vehicleAuthorization.setApplyEndDate(nowDate);
            vehicleAuthorization.setUpdateTime(nowDate);
            vehicleAuthorizationRepository.updateByPrimaryKeySelective(vehicleAuthorization);
            // 登录授权日志表
            AuthorizationHistory history = new AuthorizationHistory();
            // 授权id
            history.setAuthorizationId(vehicleAuthorization.getId());
            // 用户id
            history.setUserId(userid);
            // 操作时间
            history.setOperateTime(nowDate);
            // 操作内容
            history.setOperateContent(OperateContentEnums.CANCEL.getType());
            // 备注
            history.setRemark(OperateContentEnums.CANCEL.getValue());
            authorizationHistoryRepository.insertOne(history);
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","取消成功"),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","取消失败"),
                HttpStatus.OK);
    }
    /**
     * 根据授权ID获取授权信息
     *
     * @param authorizationId 授权id
     * @return
     */
    public VehicleAuthorization selectByAuthorizationId(String authorizationId){
        if(StringUtil.isNotNull(authorizationId)) {
            Example example = new Example(VehicleAuthorization.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id", authorizationId);
            return vehicleAuthorizationRepository.selectOneByExample(example);
        }
        return null;
    }
    /**
     * 根据任务工单id查询该车辆有效的授权状态
     *
     * @param taskId 任务id
     * @return
     */
    public VehicleAuthorization selectByTaskId(Long taskId){
        if(StringUtil.isNotNull(taskId)) {
            Example example = new Example(VehicleAuthorization.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("taskId", taskId);
            example.setOrderByClause(" update_time desc ");
            List<VehicleAuthorization> resultList = vehicleAuthorizationRepository.selectByExampleList(example);
            for (VehicleAuthorization result : resultList) {
                if (AuthorizationStatusEnums.AUTHORIZETED.getType().equals(result.getAuthorizationStatus())) {
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * 根据任务工单id和用户id获取该用户最新授权信息
     *
     * @param taskId 任务id
     * @return
     */
    public VehicleAuthorization selectByTaskIdAndUserId(Long taskId, String userId){
        if(StringUtil.isNotNull(taskId)) {
            Example example = new Example(VehicleAuthorization.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("taskId", taskId);
            criteria.andEqualTo("userId", userId);
            example.setOrderByClause(" update_time desc ");
            return vehicleAuthorizationRepository.selectOneByExample(example);
        }
        return null;
    }

    /**
     * 编辑资料--------弃用
     *
     * @param userId 用户id
     * @param paramterMap  参数集合 （包含车牌号，授权id，申请人姓名，申请人电话，申请人证件号码，备注）
     * @param photoUrl1 图片1
     * @param photoUrl2 图片2
     * @param photoUrl3 图片3
     * @param photoUrl4 图片4
     * @param photoUrl4 图片5
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> editAuthorization(String userId, Map<String,String> paramterMap, MultipartFile photoUrl1, MultipartFile photoUrl2,
                                                           MultipartFile photoUrl3, MultipartFile photoUrl4, MultipartFile photoUrl5){
        if(StringUtil.isNull(userId)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","当前登录用户手机号为空"),
                    HttpStatus.OK);
        }
        // 根据车牌号取得车辆任务工单状态为正常的车辆信息
        VehicleTask task = vehicleTaskService.selectVehicleInfo(paramterMap.get("plate"));
        if(task == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","不存在此车辆"),
                    HttpStatus.OK);
        }
        // 附件关联表（照片路径保存）
        List<VehiclePhotoPath> vehiclePhotoPathList = new ArrayList<>();
        // 根据授权id获取单车授权信息,更新单车授权表
        VehicleAuthorization vehicleAuthorization = selectByAuthorizationId(paramterMap.get("authorizationId"));
        // 申请人姓名
        vehicleAuthorization.setApplicantName(paramterMap.get("applicantName"));
        // 申请人电话
        vehicleAuthorization.setApplicantPhone(paramterMap.get("applicantPhone"));
        // 申请人证件号
        vehicleAuthorization.setApplicantIdentityNum(paramterMap.get("applicantIdentityNum"));
        // 授权状态
        vehicleAuthorization.setAuthorizationStatus(AuthorizationStatusEnums.APPLYING.getType());
        // 备注
        vehicleAuthorization.setRemark(paramterMap.get("remark"));
        Date nowDate = new Date();
        vehicleAuthorization.setOperateDate(nowDate);
        vehicleAuthorization.setUpdateTime(nowDate);
        vehicleAuthorization.setUpdater(userId);
        vehicleAuthorizationRepository.updateByPrimaryKeySelective(vehicleAuthorization);
        // 登录授权日志表
        AuthorizationHistory history = new AuthorizationHistory();
        // 授权id
        history.setAuthorizationId(vehicleAuthorization.getId());
        // 用户id
        history.setUserId(userId);
        // 操作时间
        history.setOperateTime(nowDate);
        // 操作内容
        history.setOperateContent("修改申请资料");
//        history.setRemark();
        authorizationHistoryRepository.insertOne(history);
        // 删除附件表中的数据
        vehiclePhotoPathRepository.deleteByVehiclePhotoUUID(vehicleAuthorization.getPhotoUuid());
        // 分别将图片url保存在vehiclePhotoPathList，登录到附件表中
        if(photoUrl1 != null ){
            addVehiclePhotoPath(userId,photoUrl1, vehicleAuthorization,vehiclePhotoPathList );
        }else{
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","至少上传呢一张图片"),
                    HttpStatus.OK);
        }
        if(photoUrl1 != null ){
            addVehiclePhotoPath(userId,photoUrl2, vehicleAuthorization,vehiclePhotoPathList );
        }
        if(photoUrl1 != null ){
            addVehiclePhotoPath(userId,photoUrl3, vehicleAuthorization,vehiclePhotoPathList );
        }
        if(photoUrl1 != null ){
            addVehiclePhotoPath(userId,photoUrl4, vehicleAuthorization,vehiclePhotoPathList );
        }
        if(photoUrl1 != null ){
            addVehiclePhotoPath(userId,photoUrl5, vehicleAuthorization,vehiclePhotoPathList );
        }
        vehiclePhotoPathRepository.insertMore(vehiclePhotoPathList);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","编辑资料成功"),
                HttpStatus.OK);
    }


    /**
     * 文件下载
     *
     * @param response 网页请求
     * @param fileName 文件名
     * @param loadDate 上传时间
     */
    public void downloadFile(HttpServletResponse response, String fileName, String loadDate) {
        String path = fileUploadProperties.getAuthorizationPath() + loadDate + "/" + fileName;
        response.setContentType("text/html;charset=utf-8");
        File file = new File(path);
        try {
            if (file.exists()) {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName());
                if (mimeType == null) {
                    mimeType = "application/octet-stream";
                }
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }else{
                throw new CmsServiceException("你访问的文件不存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("你访问的文件不存在error",e);
            throw new CmsServiceException("你访问的文件不存在！");
        }
    }

    /**
     * 上传保存文件,返回文件访问路径
     * @param file
     * @return
     */
    public ResponseEntity<RestResponse> uploadFile(MultipartFile file){
        Date nowDate = new Date();
        String uploadDate = DateUtil.dateToStr(nowDate, DateUtil.formatStr_yyyyMMddS_);
        String nameName = DateUtil.dateToStr(nowDate, DateUtil.formatStr_yyyyMMddHHmmssS_) + "_"+ FileUtil.getFileName(file.getOriginalFilename());
        String fileUrl = FileUtil.saveFile(nameName, fileUploadProperties.getAuthorizationPath() + uploadDate + "/", file, fileUploadProperties.getRequestAuthorizationPath() + uploadDate + "/" );
        Map<String, String> fileNameMap = new HashMap<>();
        fileNameMap.put("fileUrl", fileUrl );
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,fileNameMap,""),
                HttpStatus.OK);
    }

    /**
     * 登录消息中心表数据
     *
     * @param content 消息内容
     * @param receiver 接收用户
     * @param nowDate 当前时间
     * @param userId 用户id
     */
    private MessageCenter insertMessageCenter(String content, String receiver, Date nowDate,String userId,String messageTrigger){
        MessageCenter messageCenter = new MessageCenter();
        messageCenter.setType(MessageTypeEnum.AUTH.getCode()); //消息类型:任务小消息
        //消息内容，把模板消息的参数替换掉
        messageCenter.setContent(content);
        //判断消息动作类型
        messageCenter.setActivity(messageTrigger);//消息触发动作:新增催收记录
        messageCenter.setReceiver(receiver); //接收者
        messageCenter.setIsReaded(MessageReadEnum.NO_READ.getCode()); //设定为未读消息
        messageCenter.setPushDate(nowDate); //推送时间
        messageCenter.setDeleteFlag(DeleteFlagEnum.ON.getCode()); //未删除
        messageCenter.setCreateTime(nowDate);
        messageCenter.setUpdateTime(nowDate);
        messageCenter.setCreator(userId);
        messageCenter.setUpdater(userId);
        return messageCenter;
    }

    /**
     * 获得委托公司审批人员设备信息
     * @param userId
     * @return
     */
    private UserDeviceInfo getLeaseDeviceInfo(String userId){
        if(StringUtil.isNull(userId)){
            return null;
        }
        Example example = new Example(UserDeviceInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        return userDeviceInfoRepository.selectOneByExample(example);
    }

    /**
     * 消息推送，单播
     * @param userId 消息接收者
     * @param now 当前时间
     * @param client 客户端类型
     * @param deviceToken 友盟设备号
     * @param content 内容
     * @param title 标题
     */
    private void push(String userId, Date now, String client, String deviceToken,String content,String title) {
        //构建消息推送历史表信息
        MessagePushHistory messagePushHistory = new MessagePushHistory();
        messagePushHistory.setClient(client); //客户端类型
        messagePushHistory.setContent(content); //内容
        messagePushHistory.setDeviceTokens(deviceToken);
        try {
            int result = UmengPushUtils_XCGJ.push(PushCastEnum.UNICAST.getCode(),client,
                    deviceToken,null,title,content);
            messagePushHistory.setResultCode(result); //设定调用友盟api响应code
            //如果是返回状态200，设为成功、否则失败
            if(result == HttpStatus.OK.value()){
                messagePushHistory.setStatus(UmengPushResultEnum.SUCCESS.getCode());
            } else {
                messagePushHistory.setStatus(UmengPushResultEnum.FAIL.getCode());
            }
        } catch (Exception e) {
            messagePushHistory.setResultCode(-1); //如果异常错误，设定结果为-1
            messagePushHistory.setStatus(UmengPushResultEnum.FAIL.getCode());
            e.printStackTrace();
        } finally {
            messagePushHistory.setUpdateTime(now);
            messagePushHistory.setCreator(userId);
            messagePushHistory.setUpdater(userId);
            messagePushHistoryRepository.insertOne(messagePushHistory);
        }
    }

}
