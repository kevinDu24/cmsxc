package cn.com.leadu.cmsxc.appuser.service.impl;

import cn.com.leadu.cmsxc.appbusiness.service.RecoveryGroupUserService;
import cn.com.leadu.cmsxc.appbusiness.service.VehicleTaskService;
import cn.com.leadu.cmsxc.appuser.service.*;
import cn.com.leadu.cmsxc.common.constant.Constants;
import cn.com.leadu.cmsxc.common.constant.enums.*;
import cn.com.leadu.cmsxc.common.util.ArrayUtil;
import cn.com.leadu.cmsxc.common.util.CommonUtil;
import cn.com.leadu.cmsxc.common.util.MessageTempletConstants;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.common.util.push.UmengPushUtils;
import cn.com.leadu.cmsxc.data.appbusiness.repository.VehicleTaskGroupRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.CaseRecordRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.MessageCenterRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.MessagePushHistoryRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.UserDeviceInfoRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryGroupUser;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTask;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.GroupUserClientVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.GroupUserListVo;
import cn.com.leadu.cmsxc.pojo.appuser.entity.CaseRecord;
import cn.com.leadu.cmsxc.pojo.appuser.entity.MessageCenter;
import cn.com.leadu.cmsxc.pojo.appuser.entity.MessagePushHistory;
import cn.com.leadu.cmsxc.pojo.appuser.entity.UserDeviceInfo;
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
 * Created by yuanzhenxia on 2018/4/13.
 *
 * 案件记录Impl
 */
@Service
public class CaseRecordServiceImpl implements CaseRecordService{
    private static final Logger logger = LoggerFactory.getLogger(CaseRecordServiceImpl.class);
    @Autowired
    private CaseRecordRepository caseRecordRepository;
    @Autowired
    private RecoveryTaskService recoveryTaskService;
    @Autowired
    private RecoveryGroupUserService recoveryGroupUserService;
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private VehicleTaskService vehicleTaskService;
    @Autowired
    private MessageCenterRepository messageCenterRepository;
    @Autowired
    private MessagePushHistoryRepository messagePushHistoryRepository;
    @Autowired
    private UserDeviceInfoRepository userDeviceInfoRepository;
    @Autowired
    private CoreSystemInterface coreSystemInterface;
    @Autowired
    ObjectMapper objectMapper;

    private static String URL= "GainVehicleAppendix"; // 调用主系统的.url

    /**
     * 获取案件记录数量
     *
     * @param systemUser
     * @param caseRecordParamVo
     * @return
     */
    public ResponseEntity<RestResponse> getCaseRecordCount(SystemUser systemUser, CaseRecordParamVo caseRecordParamVo){
        if(StringUtil.isNull(systemUser.getRecoveryCompanyId())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","数据加载完毕"),
                    HttpStatus.OK);
        }
        // 拼接like的查询条件
        if(caseRecordParamVo != null && ArrayUtil.isNotNullAndLengthNotZero(caseRecordParamVo.getVehicleProvinces())
                && !caseRecordParamVo.getVehicleProvinces().contains("全部")){
            for(String str : caseRecordParamVo.getVehicleProvinces()){
                String string = CommonUtil.likePartten(str);
                caseRecordParamVo.getList().add(string);
            }
        }
        // 画面端选择“其他”分组时，分组id设为“000000”
        if(StringUtil.isNotNull(caseRecordParamVo.getGroupId()) && "其他".equals(caseRecordParamVo.getGroupId())){
            caseRecordParamVo.setGroupId("000000");
        }
        // 如果车牌号不为空，去除车牌号中的表情符号
        if(StringUtil.isNotNull(caseRecordParamVo.getPlate())){
            caseRecordParamVo.setPlate(CommonUtil.filterEmoji(caseRecordParamVo.getPlate()));
        }
        // 设置当前时间
        caseRecordParamVo.setNowDate(new Date());
        // 获取所有组员
        List<String> users = new ArrayList<>();
        CaseRecordCountVo caseRecordCountVo = new CaseRecordCountVo();
        if("0".equals(caseRecordParamVo.getFlag())){
            // 获取分组id
            String groupId = recoveryTaskService.getGroupIdByUserId(systemUser.getUserId());
            if(StringUtil.isNotNull(groupId)){
                caseRecordParamVo.setGroupId(groupId);
                List<RecoveryGroupUser> recoveryGroupUserList = recoveryGroupUserService.selectRecoveryGroupUserByGroupId(groupId);
                if(ArrayUtil.isNotNullAndLengthNotZero(recoveryGroupUserList)){
                    for(RecoveryGroupUser recoveryGroupUser : recoveryGroupUserList){
                        users.add(recoveryGroupUser.getGroupUserId());
                    }
                }
            }else{// 业务员没有分组，查看自己被授权数据
                caseRecordParamVo.setGroupId("");
                users.add(systemUser.getUserId());
            }
            caseRecordParamVo.setUsers(users);
            caseRecordParamVo.setStatus("1");
            Integer followTaskCount = caseRecordRepository.selectCaseRecordCountBySalesMan(caseRecordParamVo);
            // 获得跟进中数量
            caseRecordCountVo.setFollowCount(followTaskCount);
            // 取出已完成的
            caseRecordParamVo.setStatus("2");
            Integer finishTaskCount = caseRecordRepository.selectCaseRecordCountBySalesMan(caseRecordParamVo);
            // 获得已完成数量
            caseRecordCountVo.setFinishCount(finishTaskCount);
        }else{
            // 获取收车公司所有用户
            List<SystemUser> sysUserList = systemUserService.selectByRecoveryCompanyId(systemUser.getRecoveryCompanyId());
            for(SystemUser sysUser : sysUserList){
                users.add(sysUser.getUserId());
            }
            caseRecordParamVo.setUsers(users);
            // 取出跟进中的
            caseRecordParamVo.setStatus("1");
            // 获得跟进中数量
            Integer followTaskCount = caseRecordRepository.selectCaseRecordCountByManager(caseRecordParamVo,systemUser.getRecoveryCompanyId());
            caseRecordCountVo.setFollowCount(followTaskCount);
            // 取出已完成的
            caseRecordParamVo.setStatus("2");
            // 获得已完成数量
            Integer finishTaskCount = caseRecordRepository.selectCaseRecordCountByManager(caseRecordParamVo,systemUser.getRecoveryCompanyId());
            caseRecordCountVo.setFinishCount(finishTaskCount);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,caseRecordCountVo,""),
                HttpStatus.OK);
    }

    /**
     * （业务员）案件信息列表
     *
     * @param caseRecordParamVo 画面参数信息
     * @return
     */
    public ResponseEntity<RestResponse> getCaseRecordListBySalesMan(SystemUser systemUser, CaseRecordParamVo caseRecordParamVo){
        // 拼接like的查询条件
        if(caseRecordParamVo != null && ArrayUtil.isNotNullAndLengthNotZero(caseRecordParamVo.getVehicleProvinces())
                && !caseRecordParamVo.getVehicleProvinces().contains("全部")){
            for(String str : caseRecordParamVo.getVehicleProvinces()){
                String string = CommonUtil.likePartten(str);
                caseRecordParamVo.getList().add(string);
            }
        }
        // 设置当前时间
        caseRecordParamVo.setNowDate(new Date());
        // 获取所有组员
        List<String> users = new ArrayList<>();
        // 获取分组id
        String groupId = recoveryTaskService.getGroupIdByUserId(systemUser.getUserId());
        if(StringUtil.isNotNull(groupId)){
            caseRecordParamVo.setGroupId(groupId);
            List<RecoveryGroupUser> recoveryGroupUserList = recoveryGroupUserService.selectRecoveryGroupUserByGroupId(groupId);
            if(ArrayUtil.isNotNullAndLengthNotZero(recoveryGroupUserList)){
                for(RecoveryGroupUser recoveryGroupUser : recoveryGroupUserList){
                    users.add(recoveryGroupUser.getGroupUserId());
                }
            }
        }else{// 业务员没有分组，查看自己被授权数据
            caseRecordParamVo.setGroupId("");
            users.add(systemUser.getUserId());
        }
        caseRecordParamVo.setUsers(users);
        // 如果车牌号不为空，去除车牌号中的表情符号
        if(StringUtil.isNotNull(caseRecordParamVo.getPlate())){
            caseRecordParamVo.setPlate(CommonUtil.filterEmoji(caseRecordParamVo.getPlate()));
        }
        // 根据画面状态设返回值
        List<CaseRecordListVo> resultTaskList;
        resultTaskList = caseRecordRepository.selectCaseRecordListBySalesMan(caseRecordParamVo,caseRecordParamVo.getPage(),caseRecordParamVo.getSize());
        if(ArrayUtil.isNotNullAndLengthNotZero(resultTaskList)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,resultTaskList,""),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","数据加载完毕"),
                HttpStatus.OK);
    }
    /**
     * （内勤人员）案件信息列表
     *
     * @param caseRecordParamVo 画面参数信息
     * @return
     */
    public ResponseEntity<RestResponse> getCaseRecordListByManager(SystemUser systemUser, CaseRecordParamVo caseRecordParamVo){
        if(StringUtil.isNull(systemUser.getRecoveryCompanyId())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","数据加载完毕"),
                    HttpStatus.OK);
        }
        // 拼接like的查询条件
        if(caseRecordParamVo != null && caseRecordParamVo.getVehicleProvinces() != null
                && !caseRecordParamVo.getVehicleProvinces().isEmpty() && !caseRecordParamVo.getVehicleProvinces().contains("全部")){
            for(String str : caseRecordParamVo.getVehicleProvinces()){
                String string = CommonUtil.likePartten(str);
                caseRecordParamVo.getList().add(string);
            }
        }
        // 画面端选择“其他”分组时，分组id设为“000000”
        if(StringUtil.isNotNull(caseRecordParamVo.getGroupId()) && "其他".equals(caseRecordParamVo.getGroupId())){
            caseRecordParamVo.setGroupId("000000");
        }
        // 设置当前时间
        caseRecordParamVo.setNowDate(new Date());
        // 如果车牌号不为空，去除车牌号中的表情符号
        if(StringUtil.isNotNull(caseRecordParamVo.getPlate())){
            caseRecordParamVo.setPlate(CommonUtil.filterEmoji(caseRecordParamVo.getPlate()));
        }
        // 获取收车公司所有业务员和内勤人员(内勤人员也可以申请授权并获取授权)
        List<String> users = new ArrayList<>();
        // 获取收车公司所有用户
        List<SystemUser> sysUserList = systemUserService.selectByRecoveryCompanyId(systemUser.getRecoveryCompanyId());
        for(SystemUser sysUser : sysUserList){
            users.add(sysUser.getUserId());
        }
        caseRecordParamVo.setUsers(users);
        List<CaseRecordListVo> resultTaskList;
        resultTaskList = caseRecordRepository.selectCaseRecordListByManager(caseRecordParamVo,systemUser.getRecoveryCompanyId(),caseRecordParamVo.getPage(),caseRecordParamVo.getSize());
        if(ArrayUtil.isNotNullAndLengthNotZero(resultTaskList)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,resultTaskList,""),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","数据加载完毕"),
                HttpStatus.OK);
    }
    /**
     * 添加催收记录
     *
     * @param systemUser 用户信息
     * @param vo 参数信息
     * @return
     */
    public ResponseEntity<RestResponse> createCaseRecord(SystemUser systemUser, CaseRecordVo vo){
        Date nowDate = new Date();
        String groupId = recoveryTaskService.getGroupIdByUserId(systemUser.getUserId());
        // 1,登录案件记录表
        CaseRecord caseRecord = new CaseRecord();
        // 任务id
        caseRecord.setTaskId(Long.parseLong(vo.getTaskId()));
        // 收车公司id
        caseRecord.setRecoveryCompanyId(systemUser.getRecoveryCompanyId());
        // 分组id
        caseRecord.setRecoveryGroupId(groupId);
        // 记录人id
        caseRecord.setRecordUserId(systemUser.getUserId());
        // 案件标题
        caseRecord.setCaseTitle(CommonUtil.filterEmoji(vo.getCaseTitle()));
        // 详细内容
        caseRecord.setDetailContent(CommonUtil.filterEmoji(vo.getDetailContent()));
        // 语音时长
        caseRecord.setVoiceLength(vo.getVoiceLength());
        // 图片urls  若干图片url之间加“，”
        int count = 0;
        StringBuilder photoUrls = new StringBuilder("");
        if(ArrayUtil.isNotNullAndLengthNotZero(vo.getPhotoUrls())){
            for(String str : vo.getPhotoUrls()){
                if(count != vo.getPhotoUrls().size() - 1){
                    photoUrls.append(str).append(Constants.COMMA);
                }else{
                    photoUrls.append(str);
                }
                count = count + 1;
            }
        }
        caseRecord.setPhotoUrls(photoUrls.toString());
        // 视频url
        caseRecord.setVideoUrls(vo.getVideoUrls());
        // 语音url
        caseRecord.setVoiceUrls(vo.getVoiceUrls());
        // 定位位置
        caseRecord.setPosition(vo.getPosition());
        // 纬度
        caseRecord.setLon(vo.getLon());
        // 经度
        caseRecord.setLat(vo.getLat());
        // 副标题
        caseRecord.setSubCaseTitle(vo.getSubCaseTitle());
        // 确认结果
        caseRecord.setResultFlag(vo.getResultFlag());
        caseRecord.setCreateTime(nowDate);
        caseRecord.setCreator(systemUser.getUserId());
        caseRecord.setUpdater(systemUser.getUserId());
        caseRecord.setUpdateTime(nowDate);
        caseRecordRepository.insertOne(caseRecord);
        // 2，给内勤人员和小组成员推送消息
        createCaseRecordPush(groupId, String.valueOf(vo.getTaskId()), systemUser.getUserId(),systemUser.getRecoveryCompanyId());
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","添加催收记录成功！"),
                HttpStatus.OK);
    }

    /**
     * 新增案件记录时推送消息给小组成员和内勤人员，并插入消息中心表,推送结果消息推送状态表
     *
     * @param groupId  分组id
     * @param taskId 任务id
     * @param userId 用户id
     */
    @Transactional
    private void createCaseRecordPush(String groupId, String taskId, String userId,String recoveryCompanyId) {
        //通过工单id取得该工单对应的车牌号
        VehicleTask task = vehicleTaskService.selectVehicleTeskById(taskId);
        String plate = task.getPlate();
        String content = MessageTempletConstants.templet_105.replace(MessageTempletConstants.templet_plate,plate);
        Date nowDate = new Date();
        // 获取内勤人员信息
        GroupUserClientVo groupUserClientVo = organizationService.getUserClientByRecoveryCompanyId(recoveryCompanyId);
        if(groupUserClientVo != null){
            // 登录消息中心表数据----内勤人员
            MessageCenter messageCenter = insertMessageCenter(content,groupUserClientVo.getUserId(),nowDate,userId,MessageTriggerEnum.TASK_105.getCode());
            messageCenterRepository.insertOne(messageCenter);
            // 推送消息 --- 内勤人员
            List<GroupUserClientVo>  groupUserClientVoList = new ArrayList<>();
            groupUserClientVoList.add(groupUserClientVo);
            push(userId, groupUserClientVoList, content, groupUserClientVo.getClient(),PushCastEnum.UNICAST.getCode(),MessageTriggerEnum.TASK_105.getNane(),groupUserClientVo.getDeviceToken());
        }
        // TODO: 2018/6/7 暂时注释掉给小组成员推送的代码
//        // 如果用户不在小组中且用户不是内勤人员
//        if(StringUtil.isNull(groupId) && !groupUserClientVo.getUserId().equals(userId)){
//            UserDeviceInfo userDeviceInfo = selectByUserId(userId);  //获取用户登录设备信息
//            //构建不在小组成员消息中心表实体类
//            if(userDeviceInfo != null){
//                MessageCenter messageCenter = insertMessageCenter(content,userDeviceInfo.getUserId(),nowDate,userId,MessageTriggerEnum.TASK_105.getCode());
//                messageCenterRepository.insertOne(messageCenter); //登录消息中心表
//                //推送
//                List<GroupUserClientVo>  groupUserClientVoList = new ArrayList<>();
//                GroupUserClientVo userClientVo = new GroupUserClientVo();
//                userClientVo.setUserId(userDeviceInfo.getUserId());
//                userClientVo.setDeviceToken(userDeviceInfo.getDeviceToken());
//                userClientVo.setClient(String.valueOf(userDeviceInfo.getClient()));
//                groupUserClientVoList.add(userClientVo);
//                if( StringUtil.isNotNull(userClientVo.getDeviceToken())){ //如果该用户处于登陆状态，进行消息推送
//                    push(userId, groupUserClientVoList, content, userClientVo.getClient(),PushCastEnum.UNICAST.getCode(),MessageTriggerEnum.TASK_105.getNane(),userClientVo.getDeviceToken());
//                }
//            }
//            return;
//        }
//        // 以下是小组成员信息推送
//        // 获取小组成员信息
//        GroupUserListVo vo = organizationService.getGroupUserClientByGroupId(groupId);
//        if(vo != null){
//            List<MessageCenter> messageCenters = new ArrayList();
//            //小组用户集合不为空
//            if(ArrayUtil.isNotNullAndLengthNotZero(vo.getGroupUsers())){
//                //遍历小组用户集合
//                for(String groupUser : vo.getGroupUsers()){
//                    // 登录消息中心表数据 --- 小组成员
//                    messageCenters.add(insertMessageCenter(content,groupUser,nowDate,userId, MessageTriggerEnum.TASK_105.getCode())) ;
//                }
//                //批量插入消息中心表
//                messageCenterRepository.insertMore(messageCenters);
//            }
//            //ios设备集合不为空，调用推送共同推送消息
//            if(ArrayUtil.isNotNullAndLengthNotZero(vo.getIosUsers())){
//                push(userId, vo.getIosUsers(), content, ClientTypeEnums.IOS.getCode(),PushCastEnum.LISTCAST.getCode(),MessageTriggerEnum.TASK_105.getNane(),null);
//            }
//            //android设备集合不为空，调用推送共同推送消息
//            if(ArrayUtil.isNotNullAndLengthNotZero(vo.getAndroidUsers())){
//                push(userId, vo.getAndroidUsers(), content, ClientTypeEnums.ANDROID.getCode(),PushCastEnum.LISTCAST.getCode(),MessageTriggerEnum.TASK_105.getNane(),null);
//            }
//        }
    }
    /**
     * 推送消息，登录消息推送日志表
     *
     * @param userId 用户id
     * @param voList 安卓或iOS用户信息
     * @param content 消息内容
     * @param clientType "1":ios,"0":安卓
     */
    @Transactional
    private void push(String userId, List<GroupUserClientVo> voList, String content, String clientType, String pushCast,String messageTrigger,String deviceToken) {
        List<String> deviceTokens = new ArrayList(); //设备集合
        StringBuilder deviceTokenStr = new StringBuilder(); //消息推送日志表
        for(GroupUserClientVo item : voList){
            if(deviceTokenStr.length() > 0){
                deviceTokenStr = deviceTokenStr.append(Constants.COMMA);
            }
            if(StringUtil.isNotNull(item.getDeviceToken())){
                deviceTokenStr = deviceTokenStr.append(item.getDeviceToken());
                deviceTokens.add(item.getDeviceToken());
            }
        }
        if(ArrayUtil.isNotNullAndLengthNotZero(deviceTokens) || StringUtil.isNotNull(deviceToken)){
            MessagePushHistory messagePushHistory = new MessagePushHistory();
            messagePushHistory.setClient(clientType);
            messagePushHistory.setContent(content);
            if(PushCastEnum.LISTCAST.getCode().equals(pushCast)){
                messagePushHistory.setDeviceTokens(deviceTokenStr.toString());
            }else{
                messagePushHistory.setDeviceTokens(deviceToken);
            }
            String title  = messageTrigger;//消息触发动作:新增催收记录
            try {
                int result;
                if(PushCastEnum.LISTCAST.getCode().equals(pushCast)){
                    result = UmengPushUtils.push(pushCast,clientType,null,
                            deviceTokens,title,content);
                }else{
                    result = UmengPushUtils.push(pushCast,clientType,deviceToken,
                            null,title,content);
                }
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
    }

    /**
     * 登录消息中心表数据
     *
     * @param content 消息内容
     * @param groupUser 接收用户
     * @param nowDate 当前时间
     * @param userId 用户id
     */
    private MessageCenter insertMessageCenter(String content, String groupUser, Date nowDate,String userId,String messageTrigger){
        MessageCenter messageCenter = new MessageCenter();
        messageCenter.setType(MessageTypeEnum.TASK.getCode()); //消息类型:任务小消息
        messageCenter.setTag(MessageTagEnum.RECORD.getCode()); //消息标签：记录
        //消息内容，把模板消息的参数替换掉
        messageCenter.setContent(content);
        //判断消息动作类型
        messageCenter.setActivity(messageTrigger);//消息触发动作:新增催收记录
        messageCenter.setReceiver(groupUser); //接收者
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
     * 催单推送
     *
     * @param groupId 分组id
     * @param plate 车牌号
     * @param authorizationUserId 授权用户id
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> reminderPush(String groupId, String plate, SystemUser systemUser, String reminderContent, String authorizationUserId){
        if(StringUtil.isNull(groupId) && StringUtil.isNull(authorizationUserId)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","任务尚未被分配，不可进行催单！"),
                    HttpStatus.OK);
        }
        String userId = systemUser.getUserId();
        // 去除催单内容中的表情符号
        reminderContent = CommonUtil.filterEmoji(reminderContent);
        String content = MessageTempletConstants.templet_106.replace(MessageTempletConstants.templet_plate,plate).replace(MessageTempletConstants.templet_remindercontent,reminderContent);
        Date nowDate = new Date();
        if(StringUtil.isNotNull(groupId)){
            //如果客户端传来的所属小组id不为空
            // 获取小组成员信息
            GroupUserListVo vo = organizationService.getGroupUserClientByGroupId(groupId);
            if(vo != null){
                List<MessageCenter> messageCenters = new ArrayList();
                //小组用户集合不为空
                if(ArrayUtil.isNotNullAndLengthNotZero(vo.getGroupUsers())){
                    //遍历小组用户集合
                    for(String groupUser : vo.getGroupUsers()){
                        // 登录消息中心表数据 --- 小组成员
                        messageCenters.add(insertMessageCenter(content,groupUser,nowDate,userId,MessageTriggerEnum.TASK_106.getCode())) ;
                    }
                    //批量插入消息中心表
                    messageCenterRepository.insertMore(messageCenters);
                }
                //ios设备集合不为空，调用推送共同推送消息
                if(ArrayUtil.isNotNullAndLengthNotZero(vo.getIosUsers())){
                    push(userId, vo.getIosUsers(), content, ClientTypeEnums.IOS.getCode(),PushCastEnum.LISTCAST.getCode(),MessageTriggerEnum.TASK_106.getNane(),null);
                }
                //android设备集合不为空，调用推送共同推送消息
                if(ArrayUtil.isNotNullAndLengthNotZero(vo.getAndroidUsers())){
                    push(userId, vo.getAndroidUsers(), content, ClientTypeEnums.ANDROID.getCode(),PushCastEnum.LISTCAST.getCode(),MessageTriggerEnum.TASK_106.getNane(),null);
                }
            }
        } else if(StringUtil.isNotNull(authorizationUserId)){
            //如果客户端传来的所属小组id为空，但授权用户id不为空
            //1.判断该成员是否属于本公司人员，是则推，不是则不推
            GroupUserClientVo userClientVo = getUserClientByUserId(authorizationUserId);
            SystemUser authUser = systemUserService.selectSystemUserByUserId(authorizationUserId);
            //如果获得授权人员是自己所在收车公司的，进行推送
            if(systemUser.getRecoveryCompanyId().equals(authUser.getRecoveryCompanyId())){
                // 登录消息中心表数据----已授权人员
                MessageCenter messageCenter = insertMessageCenter(content,authorizationUserId,nowDate,userId, MessageTriggerEnum.TASK_106.getCode());
                messageCenterRepository.insertOne(messageCenter);
                if(userClientVo != null){
                    // 推送消息 --- 已授权人员
                    List<GroupUserClientVo>  groupUserClientVoList = new ArrayList<>();
                    groupUserClientVoList.add(userClientVo);
                    push(userId, groupUserClientVoList, content, userClientVo.getClient(), PushCastEnum.UNICAST.getCode(),MessageTriggerEnum.TASK_106.getNane(),userClientVo.getDeviceToken());
                }
            }
        }

        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","催单推送成功！"),
                HttpStatus.OK);
    }

    /**
     * 查看案件记录详情
     *
     * @param taskId 任务id
     * @param systemUser 用户信息
     * @return
     */
    public ResponseEntity<RestResponse> getCaseRecordDetail(String taskId, SystemUser systemUser){
        List<CaseRecordVo> newCaseRecordVoList = new ArrayList<>();
        // 如果小组id不为空则根据小组id查看案件记录，如果小组id为空则根据记录人员id查看记录
        List<CaseRecordVo> caseRecordVoList = caseRecordRepository.selectCaseRecordDetails(taskId, systemUser.getRecoveryCompanyId());
        if(ArrayUtil.isNotNullAndLengthNotZero(caseRecordVoList)){
            for(CaseRecordVo vo : caseRecordVoList){
                vo.setPhotoUrls(CommonUtil.breakStringByComma(vo.getPhotoUrl()));
                newCaseRecordVoList.add(vo);
            }
        }
        if(ArrayUtil.isNotNullAndLengthNotZero(newCaseRecordVoList)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,caseRecordVoList,""),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未查询到信息"),
                HttpStatus.OK);
    }
    /**
     * 查看案件相关附件信息
     *
     * @param taskId 任务id
     * @return
     */
    public ResponseEntity<RestResponse> getAttachmentFile(String taskId){
        CaseAttachmentFileVo caseAttachmentFileVo = new CaseAttachmentFileVo();
        //通过工单id取得该工单对应的车牌号
        VehicleTask task = vehicleTaskService.selectVehicleTeskById(taskId);
        if(task == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未获取到工单信息"),
                    HttpStatus.OK);
        }
        // 车主姓名
        caseAttachmentFileVo.setName(task.getName());
        // 车主证件号码
        caseAttachmentFileVo.setIdCard(task.getIdCard());
        // 车主手机号
        caseAttachmentFileVo.setPhoneNum(task.getPhoneNum());
        // 车主家庭住址
        caseAttachmentFileVo.setHomeAddress(task.getHomeAddress());
        // 车主常住地址
        caseAttachmentFileVo.setLifeAddress(task.getLifeAddress());
        // 车主工作地址
        caseAttachmentFileVo.setWorkAddress(task.getWorkAddress());
        // TODO 调主系统获取
       // 调用主统接口，根据车架号，获取相关附件信息
//        String result = coreSystemInterface.getAttchmentFile(URL, task.getVehicleIdentifyNum());
//        // json 解析
//        AttachmentFileResultVo gpsResultVo = new AttachmentFileResultVo();
//        try {
//            gpsResultVo = objectMapper.readValue(result, AttachmentFileResultVo.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","系统异常，请重试"),
//                    HttpStatus.OK);
//        }
//        if(!"00000000".equals(gpsResultVo.getCode())){
//            logger.error(gpsResultVo.getMessage());
//            if(StringUtil.isNotNull(gpsResultVo.getMessage())){
//                return new ResponseEntity<RestResponse>(
//                        RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"",gpsResultVo.getMessage()),
//                        HttpStatus.OK);
//            }
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","获取车辆附件信息失败"),
//                    HttpStatus.OK);
//        }
//        caseAttachmentFileVo.setAttachmentFileVoList(gpsResultVo.getData());


        // 模拟主系统附件信息
        List<AttachmentFileVo> attachmentFileVoList = new ArrayList<>(); // 车辆附件信息@"doc",@"txt",@"xls",@"pdf",@"docx",@"xlsx"
        AttachmentFileVo vo = new AttachmentFileVo();
        vo.setAttachmentFileName("先锋太盟融资租赁合同（含章）_pdf");
        vo.getAttachmentFileUrls().add("http://222.73.56.12:8588/files/download/idCard/20180227/8ee535b9-1cfa-4413-917a-3f3b1d32efe6.pdf");
        attachmentFileVoList.add(vo);
        AttachmentFileVo vo1 = new AttachmentFileVo();
        vo1.setAttachmentFileName("先锋太盟融资租赁合同_img");
        vo1.getAttachmentFileUrls().add("http://192.168.1.136:9021/api/appuser/file/download/apply/20180620/20180620181812_1_img.jpg");
        vo1.getAttachmentFileUrls().add("http://116.236.234.246:18080//pdfurla/20180810//LNBMDBAF9FU063820//3d28a7de-c87a-46d2-a641-039281bef1a0.jpg");
        vo1.getAttachmentFileUrls().add("http://116.236.234.246:18080//pdfurla/20180810//LNBMDBAF9FU063820//00148abc-0d56-417a-82df-284b07f7bc52.jpg");
        attachmentFileVoList.add(vo1);
        caseAttachmentFileVo.setAttachmentFileVoList(attachmentFileVoList);
        AttachmentFileVo vo2 = new AttachmentFileVo();
        vo2.setAttachmentFileName("先锋太盟融资租赁合同_word");
        vo2.getAttachmentFileUrls().add("http://192.168.1.136:9021/api/appuser/file/download/apply/20180620/111111111111111111.docx");
        attachmentFileVoList.add(vo2);
        AttachmentFileVo vo4 = new AttachmentFileVo();
        vo4.setAttachmentFileName("赏金寻车numbers文件_numbers");
        vo4.getAttachmentFileUrls().add("http://192.168.1.136:9021/api/appuser/file/download/apply/20180620/赏金寻车numbers文件.numbers");
        attachmentFileVoList.add(vo4);
        AttachmentFileVo vo5 = new AttachmentFileVo();
        vo5.setAttachmentFileName("赏金寻车xls文件");
        vo5.getAttachmentFileUrls().add("http://192.168.1.136:9021/api/appuser/file/download/apply/20180620/赏金寻车xls文件.xls");
        attachmentFileVoList.add(vo5);
        AttachmentFileVo vo6 = new AttachmentFileVo();
        vo6.setAttachmentFileName("赏金寻车xlsx文件");
        vo6.getAttachmentFileUrls().add("http://192.168.1.136:9021/api/appuser/file/download/apply/20180620/赏金寻车xlsx文件.xlsx");
        attachmentFileVoList.add(vo6);
        AttachmentFileVo vo7 = new AttachmentFileVo();
        vo7.setAttachmentFileName("新建 DOC 文档");
        vo7.getAttachmentFileUrls().add("http://192.168.1.136:9021/api/appuser/file/download/apply/20180620/新建DOC文档.doc");
        attachmentFileVoList.add(vo7);
        caseAttachmentFileVo.setAttachmentFileVoList(attachmentFileVoList);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,caseAttachmentFileVo,""),
                HttpStatus.OK);
    }

    /**
     * 通过用户id获取用户客户端类型
     *
     * @param userId 用户id
     * @return
     */
    public GroupUserClientVo getUserClientByUserId(String userId){
        GroupUserClientVo groupUserClientVo = new GroupUserClientVo();
        Example example = new Example(UserDeviceInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        UserDeviceInfo vo = userDeviceInfoRepository.selectOneByExample(example);
        if(vo == null){
            return null;
        }
        groupUserClientVo.setClient(String.valueOf(vo.getClient()));
        groupUserClientVo.setDeviceToken(vo.getDeviceToken());
        groupUserClientVo.setUserId(userId);
        return groupUserClientVo;
    }
    /**
     * 根据user_id获取用户登录设备信息
     * @param userId 用户id
     * @return
     */
    public UserDeviceInfo selectByUserId(String userId){
        if(StringUtil.isNotNull(userId)) {
            Example example = new Example(UserDeviceInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId", userId);
            return userDeviceInfoRepository.selectOneByExample(example);
        }
        return null;
    }

}
