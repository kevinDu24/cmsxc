package cn.com.leadu.cmsxc.appuser.service.impl;

import cn.com.leadu.cmsxc.appbusiness.service.RecoveryCompanyService;
import cn.com.leadu.cmsxc.appbusiness.service.VehicleTaskService;
import cn.com.leadu.cmsxc.appuser.config.FileUploadProperties;
import cn.com.leadu.cmsxc.appuser.service.*;
import cn.com.leadu.cmsxc.appuser.util.constant.enums.ClueCheckFlag;
import cn.com.leadu.cmsxc.appuser.util.constant.enums.FlagEnums;
import cn.com.leadu.cmsxc.appuser.util.constant.enums.ScanTypeEnums;
import cn.com.leadu.cmsxc.appuser.util.constant.enums.ScoreCodeEnums;
import cn.com.leadu.cmsxc.common.constant.Constants;
import cn.com.leadu.cmsxc.common.constant.enums.*;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.common.util.*;
import cn.com.leadu.cmsxc.common.util.push.UmengPushUtils;
import cn.com.leadu.cmsxc.common.util.push.UmengPushUtils_XCGJ;
import cn.com.leadu.cmsxc.data.appbusiness.repository.RecoveryTaskRepository;
import cn.com.leadu.cmsxc.data.appbusiness.repository.VehicleTaskGroupRepository;
import cn.com.leadu.cmsxc.data.appbusiness.repository.VehicleTaskRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.*;
import cn.com.leadu.cmsxc.data.system.repository.SystemUserRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryCompany;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTask;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskGroup;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskRecovery;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.GroupUserClientVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.GroupUserListVo;
import cn.com.leadu.cmsxc.pojo.appuser.entity.*;
import cn.com.leadu.cmsxc.pojo.appuser.vo.SearchPlateVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.SoleTaskInfoVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import com.google.common.collect.Maps;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 上传扫描车牌照
 */
@Service
public class PlateUploadServiceImpl implements PlateUploadService {
    private static final Logger logger = LoggerFactory.getLogger(PlateUploadServiceImpl.class);
    private double distance = 1000;// 一公里以内为同一条线索
    @Autowired
    private VehicleTaskService vehicleTaskService;
    @Autowired
    private ClueService clueService;
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private FileUploadProperties fileUploadProperties;
    @Autowired
    private ClueInforRepository clueInforRepository;
    @Autowired
    private VehicleTaskRepository vehicleTaskRepository;
    @Autowired
    private SystemUserRepository systemUserRepository;
    @Autowired
    private SystemUserScoreRepository systemUserScoreRepository;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private MessageCenterRepository messageCenterRepository;
    @Autowired
    private MessagePushHistoryRepository messagePushHistoryRepository;
    @Autowired
    private RecoveryCompanyService recoveryCompanyService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private VehicleTaskGroupRepository vehicleTaskGroupRepository;
    @Autowired
    private RecoveryTaskRepository recoveryTaskRepository;
    @Autowired
    private UserDeviceInfoRepository userDeviceInfoRepository;

    /**
     * 上传扫描车牌照
     *
     * @param systemUser 用户信息
     * @param photoMsg 线索图片
     * @param paramterMap 参数信息（车牌号，线索地址，线索经纬度）
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> uploadPlateInfor(SystemUser systemUser, Map<String,String> paramterMap, MultipartFile photoMsg){
        // 去除车牌号中的表情符号
        String plate = CommonUtil.filterEmoji(paramterMap.get("plate"));
        String userId = systemUser.getUserId();
        Date now  = new Date(); //当前时间
        // 获取扫描方式，0:车牌号、1:车架号  ScanTypeEnums
        String type = paramterMap.get("type");
        if(StringUtil.isNull(type)){
            // 如果type为空，默认为"0"车牌号扫描，兼容老接口
            type = ScanTypeEnums.PLATE.getType();
        }
        // 保存用户手机号，发送短信用
        List<String> userIdList = null;
        // 根据车牌号和用户id查看已上传的线索,用于判断当前线索是否有效
        List<ClueInfo> clueInformationList = new ArrayList();
        if(ScanTypeEnums.PLATE.getType().equals(type)){
            clueInformationList = clueService.selectByUserIdAndPlate(plate, userId);
        } else if(ScanTypeEnums.VEHICLE_IDENTIFY_NUM.getType().equals(type)){
            clueInformationList = clueService.selectByUserIdAndVehicleIdentifyNum(plate, userId);
        }
        if(clueInformationList != null && !clueInformationList.isEmpty()){
            for(ClueInfo clueInformation : clueInformationList){
                // 如果当前线索和以前上传过的任意一条线索的距离在1公里以内，算同一条线索，返回“您已上传该线索”
                if(StringUtil.isNotNull(clueInformation.getAppLon())&& StringUtil.isNotNull(clueInformation.getAppLat())
                        && StringUtil.isNotNull(paramterMap.get("appLon")) && StringUtil.isNotNull(paramterMap.get("appLat"))) {
                    double result = DistanceUtils.getDistance(Double.valueOf(clueInformation.getAppLon()), Double.valueOf(clueInformation.getAppLat()),
                            Double.valueOf(paramterMap.get("appLon")), Double.valueOf(paramterMap.get("appLat")));
                    if (result <= distance) {
                        return new ResponseEntity<RestResponse>(
                                RestResponseGenerator.genResponse(ResponseEnum.FAILURE, "", "您已上传该线索，请勿重复上传！"),
                                HttpStatus.OK);
                    }
                }
            }
        }
        // 存放返回值---命中标志
        Map map = Maps.newHashMap();
        // 我的线索表
        ClueInfo clue = new ClueInfo();
        // 积分流水表
        SystemUserScore systemUserScore = new SystemUserScore();
        // 取得用户信息
        SystemUser sysUser = systemUserService.selectSystemUserByUserId(userId);
        // 用户增加10积分，更新用户信息
        if(sysUser != null){
            sysUser.setTotalScore(sysUser.getTotalScore()+ 10);
            sysUser.setUpdateTime(now);
            sysUser.setUpdater(userId);
            systemUserRepository.updateByPrimaryKey(sysUser);
        }
        // 我的积分流水表中添加一条数据
        // 用户id
        systemUserScore.setUserId(userId);
        // 积分code
        systemUserScore.setScoreCode(ScoreCodeEnums.SCAN.getType());
        // 积分值
        systemUserScore.setScoreValue(+10);
        // 备注
        systemUserScore.setRemark(ScoreCodeEnums.SCAN.getValue());
        systemUserScore.setScoreTime(now);
        systemUserScore.setUpdateTime(now);
        systemUserScore.setCreateTime(now);
        systemUserScore.setCreator(userId);
        systemUserScore.setUpdater(userId);
        systemUserScoreRepository.insertOne(systemUserScore);
        // 上传文件，返回文件访问路径
        String filePath = ""; //TODO 提升上传速率，舍弃图片上传操作
        // 用户id
        clue.setUserId(userId);
        // 扫描方式
        clue.setType(type);
        if(ScanTypeEnums.PLATE.getType().equals(type)){
            // 车牌号
            clue.setPlate(plate);
        } else if(ScanTypeEnums.VEHICLE_IDENTIFY_NUM.getType().equals(type)){
            // 设定车架号
            clue.setVehicleIdentifyNum(plate);
        }
        // 线索上传地址
        clue.setAppAddr(paramterMap.get("appAddr"));
        // 线索经度
        clue.setAppLon(paramterMap.get("appLon"));
        // 线索纬度
        clue.setAppLat(paramterMap.get("appLat"));
        // 根据车牌号或车架号取得车辆任务工单状态为正常的车辆信息,并且收车公司派单表中为已过期，且不是已授权和已完成的
        VehicleTask task = vehicleTaskService.selectByPlateAndTaskStatus(plate, TaskStatusEnums.NORMAL.getType(),now);
        // 如果车辆任务工单中存在此车辆信息，返回 1：已命中，我的线索表中是否命中flag设置为1,并且获取命中这辆车的所有用户，向这些用户发送短信
        if(task != null){
            // 已命中
            clue.setPlate(task.getPlate()); //设定车牌号
            clue.setVehicleIdentifyNum(task.getVehicleIdentifyNum()); //设定车牌号
            clue.setTargetFlag(FlagEnums.YES.getType());
            map.put("targetFlag", FlagEnums.YES.getType());
            // 更新工单表中clueFlag,设置为1：有
            task.setClueFlag(FlagEnums.YES.getType());
            task.setUpdater(userId);
            vehicleTaskRepository.updateByPrimaryKey(task);
            //消息推送
            // 获取内勤人员信息
            GroupUserClientVo groupUserClientVo = null;
            groupUserClientVo = organizationService.getUserClientByRecoveryCompanyId(systemUser.getRecoveryCompanyId());
            if(groupUserClientVo != null){
                //非独家任务线索命中时，推送消息给内勤人员
                pushMessage(systemUser, now, groupUserClientVo, null, null,"0");
            }
            // 获取委托公司一级管理员信息
            GroupUserClientVo leaseApprover = messageCenterRepository.findByLeaseAdminUserId(task.getLeaseCompanyUserName());
            if(leaseApprover != null){
                //非独家任务线索命中时，推送消息给委托公司一级管理员
                pushMessage(systemUser, now, leaseApprover, plate, groupUserClientVo == null ? "无" : groupUserClientVo.getUserPhone(),"1");
            }
        }else{
            // 未命中
            clue.setTargetFlag(FlagEnums.NO.getType());
            map.put("targetFlag", FlagEnums.NO.getType());
            //单独判断是否是独家任务命中
            SearchPlateVo searchPlateVo = new SearchPlateVo();
            searchPlateVo.setPlate(plate);
            searchPlateVo.setTaskStatus(TaskStatusEnums.NORMAL.getType());
            searchPlateVo.setNowDate(now);
            //根据车牌号和工单状态判定该任务是否为独家任务
            SoleTaskInfoVo soleTaskInfoVo = vehicleTaskRepository.selectSoleTask(searchPlateVo);
            if(soleTaskInfoVo != null){
                //推送消息给内勤和任务所属小组成员
                GroupUserListVo groupUserListVo = null; //内勤和小组成员公用集合实体类
                // 获取小组成员
                //  判断该任务是否分配给小组
                Example example = new Example(VehicleTaskGroup.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("vehicleTaskRecoveryId", soleTaskInfoVo.getRecoveryTaskId());
                VehicleTaskGroup vehicleTaskGroup =  vehicleTaskGroupRepository.selectOneByExample(example);
                if(vehicleTaskGroup != null){
                    String groupId = vehicleTaskGroup.getGroupId(); //独家任务对应的小组id
                    groupUserListVo = organizationService.getGroupUserClientByGroupId(groupId);
                }
                //获取内勤人员信息
                GroupUserClientVo groupUserClientVo = null;
                //通过工单id获得收车公司任务信息
                VehicleTaskRecovery vehicleTaskRecovery = getByTaskId(soleTaskInfoVo.getTaskId());
                if(vehicleTaskRecovery != null){
                    //内勤人员推送相关信息
                    groupUserClientVo = organizationService.getUserClientByRecoveryCompanyId(vehicleTaskRecovery.getRecoveryCompanyId());
                }
                //整合小组成员与内勤人员集合
                if(groupUserClientVo != null){
                    if(groupUserListVo == null){
                        groupUserListVo = new GroupUserListVo();
                        groupUserListVo.getGroupUsers().add(groupUserClientVo.getUserId());
                        if(ClientTypeEnums.ANDROID.getCode().equals(groupUserClientVo.getClient())){ //如果内勤人员当前登录设备是安卓客户端
                            groupUserListVo.getAndroidUsers().add(groupUserClientVo); //安卓集合新增一个元素
                        } else if(ClientTypeEnums.IOS.getCode().equals(groupUserClientVo.getClient())){ //如果内勤人员当前登录设备是ios客户端
                            groupUserListVo.getIosUsers().add(groupUserClientVo); //ios集合新增一个元素
                        }
                    } else {
                        List<String> users = groupUserListVo.getGroupUsers(); //获得小组所有成员集合
                        users.add(groupUserClientVo.getUserId()); //将内勤任人员信息放入小组所有成员集合中，统一消息推送目标
                        groupUserListVo.setGroupUsers(users); //重新设值
                        if(ClientTypeEnums.ANDROID.getCode().equals(groupUserClientVo.getClient())){ //如果内勤人员当前登录设备是安卓客户端
                            groupUserListVo.getAndroidUsers().add(groupUserClientVo); //安卓集合新增一个元素
                        } else if(ClientTypeEnums.IOS.getCode().equals(groupUserClientVo.getClient())){ //如果内勤人员当前登录设备是ios客户端
                            groupUserListVo.getIosUsers().add(groupUserClientVo); //ios集合新增一个元素
                        }
                    }
                }
                //独家任务线索命中时，推送给内勤人员和小组成员
                if(groupUserListVo != null){
                    pushRecoveryCompany(userId, soleTaskInfoVo, groupUserListVo);
                }

                //独家任务线索命中时，推送消息给委托公司审批人员
                // 获取委托公司审批人员信息
                GroupUserClientVo leaseApprover = messageCenterRepository.findByLeaseAdminUserId(soleTaskInfoVo.getUserId());
                if(leaseApprover != null){
                    //非独家任务线索命中时，推送消息给委托公司审批人员
                    pushMessage(systemUser, now, leaseApprover, plate, groupUserClientVo == null ? "无" : groupUserClientVo.getUserPhone(), "2");
                }
            }
        }
        // 如果线索图片上传成功，将图片访问url保存在我的线索表中
        if(StringUtil.isNotNull(filePath) && !filePath.contains("失败")){
            clue.setPhotoUrl(filePath);
        }
        // 线索是否查看过，初始化为0：未查看
        clue.setCheckFlag(ClueCheckFlag.NOTCHECK.getType());
        clue.setUploadDate(now);
        clue.setCreateTime(now);
        clue.setUpdateTime(now);
        clue.setCreator(userId);
        clue.setUpdater(userId);
        clueInforRepository.insertOne(clue);

        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, map, "线索上传成功"),
                HttpStatus.OK);
    }

    /**
     * 任务线索命中消息推送——单播（独家/非独家）
     * @param systemUser 当前登录人
     * @param now 当前时间
     * @param groupUserClientVo 推送对象的信息
     * @param plate 车牌号（推送给委托公司一级管理员时有意义，其他为null）
     * @param insiderUserId 内勤人员user_id（推送给委托公司一级管理员时有意义，其他为null）
     * @param flag "0":非独家命中推给内勤人员，"1":非独家命中推给委托公司一级管理员，"2":独家命中推给委托公司一级管理员
     */
    private void pushMessage(SystemUser systemUser, Date now, GroupUserClientVo groupUserClientVo, String plate, String insiderUserId, String flag) {
        String userId = systemUser.getUserId();
        //构架消息中心表信息
        MessageCenter messageCenter = new MessageCenter();
        messageCenter.setType(MessageTypeEnum.TASK.getCode()); //消息类型
        messageCenter.setTag(MessageTagEnum.CLUE.getCode()); //消息标签
        //消息内容，把模板消息的参数替换掉
        String content = null;
        //如果入参中工单id和内勤人员用户id都不为空，表示是推送给委托公司一级管理员
        if("1".equals(flag)){
            RecoveryCompany recoveryCompany = recoveryCompanyService.selectRecoveryCompanyById(systemUser.getRecoveryCompanyId()); //取得该人员所属的收车公司
            content = MessageTempletConstants.templet_104_2.replace(MessageTempletConstants.templet_plate,plate).replace(MessageTempletConstants.templet_user_id,userId)
                    .replace(MessageTempletConstants.templet_recovery_company,recoveryCompany == null ? "无" : recoveryCompany.getRecoveryFullName())
                    .replace(MessageTempletConstants.templet_insider_user_id,insiderUserId);
            try{
                messageService.sendContentMessage(groupUserClientVo.getUserPhone(), content, "赏金寻车", "非独家任务线索命中时给委托公司一级管理员发送短信", Constants.CLUE_TARGET_SCAN);
            }catch (Exception e){
                e.printStackTrace();
                logger.error("发送短信error",e);
            }
            //给委托公司一级管理员推送消息
            //获取委托公司一级管理员的登陆设备信息
            UserDeviceInfo userDeviceInfo = getLeaseDeviceInfo(groupUserClientVo.getUserId());
            if(userDeviceInfo!= null){ //登录了才会推送
                push(groupUserClientVo.getUserId(),now,userDeviceInfo.getClient().toString(),userDeviceInfo.getDeviceToken(),content,MessageTriggerEnum.TASK_104.getNane());
            }
        } else if("0".equals(flag)){
            content = MessageTempletConstants.templet_104_1.replace(MessageTempletConstants.templet_user_id,userId);
            // 若当前用户为未登录状态（device_token为空），不推送消息
            if(StringUtil.isNotNull(groupUserClientVo.getDeviceToken())){
                pushInnerUser(now, groupUserClientVo, userId, messageCenter);
            }
        } else if("2".equals(flag)){
            RecoveryCompany recoveryCompany = recoveryCompanyService.selectRecoveryCompanyById(systemUser.getRecoveryCompanyId()); //取得该人员所属的收车公司
            content = MessageTempletConstants.templet_103_1.replace(MessageTempletConstants.templet_plate,plate).replace(MessageTempletConstants.templet_user_id,userId)
                    .replace(MessageTempletConstants.templet_recovery_company,recoveryCompany == null ? "" : recoveryCompany.getRecoveryFullName())
                    .replace(MessageTempletConstants.templet_insider_user_id,insiderUserId);
            try{
                messageService.sendContentMessage(groupUserClientVo.getUserPhone(), content, "赏金寻车", "独家任务线索命中时给委托公司一级管理员发送短信", Constants.CLUE_TARGET_SOLE_SCAN);
            }catch (Exception e){
                e.printStackTrace();
                logger.error("发送短信error",e);
            }
            //给委托公司一级管理员推送消息
            //获取委托公司一级管理员的登录设备信息
            UserDeviceInfo userDeviceInfo = getLeaseDeviceInfo(groupUserClientVo.getUserId());
            if(userDeviceInfo!= null){ //登录了才会推送
                push(groupUserClientVo.getUserId(),now,userDeviceInfo.getClient().toString(),userDeviceInfo.getDeviceToken(),content,MessageTriggerEnum.TASK_103.getNane());
            }
        }
        messageCenter.setContent(content);
        //判断消息动作类型
        if("0".equals(flag) || "1".equals(flag)){
            messageCenter.setActivity(MessageTriggerEnum.TASK_104.getCode());//消息触发动作
        } else if("2".equals(flag)){
            messageCenter.setActivity(MessageTriggerEnum.TASK_103.getCode());//消息触发动作
        }
        messageCenter.setReceiver(groupUserClientVo.getUserId()); //接收者
        messageCenter.setIsReaded(MessageReadEnum.NO_READ.getCode()); //设定为未读消息
        messageCenter.setPushDate(now); //推送时间
        messageCenter.setDeleteFlag(DeleteFlagEnum.ON.getCode()); //未删除
        messageCenter.setCreateTime(now);
        messageCenter.setUpdateTime(now);
        messageCenter.setCreator(userId);
        messageCenter.setUpdater(userId);
        messageCenterRepository.insertOne(messageCenter);

    }

    /**
     * 非独家任务线索命中时，推送给命中人所在公司的内勤人员
     * @param now
     * @param groupUserClientVo
     * @param userId
     * @param messageCenter
     */
    private void pushInnerUser(Date now, GroupUserClientVo groupUserClientVo, String userId, MessageCenter messageCenter) {
        //推送消息给内勤人员
        //构建消息推送历史表信息
        MessagePushHistory messagePushHistory = new MessagePushHistory();
        messagePushHistory.setClient(groupUserClientVo.getClient()); //客户端类型
        messagePushHistory.setContent(messageCenter.getContent()); //内容
        messagePushHistory.setDeviceTokens(groupUserClientVo.getDeviceToken());
        String title = MessageTriggerEnum.TASK_104.getNane();//消息触发动作
        try {
            int result = UmengPushUtils.push(PushCastEnum.UNICAST.getCode(),groupUserClientVo.getClient(),
                    groupUserClientVo.getDeviceToken(),null,title,messageCenter.getContent());
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

    /**
     * 独家线索命中时，推送消息给内勤和小组成员
     * @param userId
     * @param soleTaskInfoVo
     * @param groupUserListVo
     */
    private void pushRecoveryCompany(String userId, SoleTaskInfoVo soleTaskInfoVo, GroupUserListVo groupUserListVo) {
        String plate = soleTaskInfoVo.getPlate();
        String content = ""; //推送内容
         //推送给内勤和小组成员
        content = MessageTempletConstants.templet_103_2.replace(MessageTempletConstants.templet_plate,plate);
        List<MessageCenter> messageCenters = new ArrayList();
        MessageCenter messageCenter;
        //小组用户集合不为空
        if(ArrayUtil.isNotNullAndLengthNotZero(groupUserListVo.getGroupUsers())){
            //遍历小组用户集合
            Date nowDate = new Date();
            for(String groupUser : groupUserListVo.getGroupUsers()){
                messageCenter = new MessageCenter();
                messageCenter.setType(MessageTypeEnum.TASK.getCode()); //消息类型
                messageCenter.setTag(MessageTagEnum.CLUE.getCode()); //消息标签
                //消息内容，把模板消息的参数替换掉
                messageCenter.setContent(content);
                //判断消息动作类型
                messageCenter.setActivity(MessageTriggerEnum.TASK_103.getCode());//消息触发动作
                messageCenter.setReceiver(groupUser); //接收者
                messageCenter.setIsReaded(MessageReadEnum.NO_READ.getCode()); //设定为未读消息
                messageCenter.setPushDate(nowDate); //推送时间
                messageCenter.setDeleteFlag(DeleteFlagEnum.ON.getCode()); //未删除
                messageCenter.setCreateTime(nowDate);
                messageCenter.setUpdateTime(nowDate);
                messageCenter.setCreator(userId);
                messageCenter.setUpdater(userId);
                messageCenters.add(messageCenter);
            }
            //批量插入消息中心表
            messageCenterRepository.insertMore(messageCenters);
        }
        //ios设备集合不为空，调用推送共同推送消息
        if(ArrayUtil.isNotNullAndLengthNotZero(groupUserListVo.getIosUsers())){
            push(userId, groupUserListVo, content, ClientTypeEnums.IOS.getCode());
        }
        //android设备集合不为空，调用推送共同推送消息
        if(ArrayUtil.isNotNullAndLengthNotZero(groupUserListVo.getAndroidUsers())){
            push(userId, groupUserListVo, content, ClientTypeEnums.ANDROID.getCode());
        }
    }

    /**
     * 推送消息，登录消息推送日志表
     *
     * @param userId
     * @param vo
     * @param content
     * @param clientType "1":ios,"0":安卓
     */
    @Transactional
    private void push(String userId, GroupUserListVo vo, String content, String clientType) {
        List<String> deviceTokens = new ArrayList(); //设备集合
        StringBuilder deviceTokenStr = new StringBuilder(); //消息推送日志表
        for(GroupUserClientVo item : ClientTypeEnums.IOS.getCode().equals(clientType) ? vo.getIosUsers() : vo.getAndroidUsers()){
            if(deviceTokenStr.length() > 0){
                deviceTokenStr = deviceTokenStr.append(Constants.COMMA);
            }
            deviceTokenStr = deviceTokenStr.append(item.getDeviceToken());
            deviceTokens.add(item.getDeviceToken());
        }
        MessagePushHistory messagePushHistory = new MessagePushHistory();
        messagePushHistory.setClient(clientType); //客户端类型
        messagePushHistory.setContent(content); //内容
        messagePushHistory.setDeviceTokens(deviceTokenStr.toString());
        String title = "";
        title = MessageTriggerEnum.TASK_103.getNane();//消息触发动作
        try {
            int result = UmengPushUtils.push(PushCastEnum.LISTCAST.getCode(),clientType,null,
                    deviceTokens,title,content);
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
            Date now = new Date();
            messagePushHistory.setCreateTime(now);
            messagePushHistory.setUpdateTime(now);
            messagePushHistory.setCreator(userId);
            messagePushHistory.setUpdater(userId);
            messagePushHistoryRepository.insertOne(messagePushHistory);
        }
    }

    /**
     * 根据工单ID获取收车公司任务信息
     *
     * @param taskId 任务id
     * @return
     */
    private VehicleTaskRecovery getByTaskId(Long taskId){
        if(StringUtil.isNotNull(taskId)) {
            Example example = new Example(VehicleTaskRecovery.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("taskId", String.valueOf(taskId));
            return recoveryTaskRepository.selectOneByExample(example);
        }
        return null;
    }

    /**
     * 上传保存文件,返回文件访问路径
     * @param plate 车牌号
     * @param file
     * @return
     */
    private String uploadFile(String plate, MultipartFile file){
        Date now = new Date();
        String uploadDate = DateUtil.dateToStr(now, DateUtil.formatStr_yyyyMMddS_);
        String nameName = DateUtil.dateToStr(now, DateUtil.formatStr_yyyyMMddHHmmssS_) + "_" + plate;
        return FileUtil.saveFile(nameName, fileUploadProperties.getCluePath() + uploadDate + "/", file, fileUploadProperties.getRequestCluePath() + uploadDate + "/" );
    }
    /**
     * 文件下载
     *
     * @param response 网页请求
     * @param fileName 文件名
     * @param loadDate 上传时间
     */
    public void downloadFile(HttpServletResponse response, String fileName, String loadDate) {
        String path = fileUploadProperties.getCluePath() + loadDate + "/" + fileName;
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
