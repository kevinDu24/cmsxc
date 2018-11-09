package cn.com.leadu.cmsxc.system.service.impl;

import cn.com.leadu.cmsxc.system.service.TaskPushService;
import org.springframework.stereotype.Service;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 委托机构推送Service实现类
 */
@Service
public class TaskPushServiceImpl implements TaskPushService {
//    private static final Logger logger = LoggerFactory.getLogger(TaskPushServiceImpl.class);
//    @Autowired
//    private VehicleTaskRepository vehicleTaskRepository;
//    @Autowired
//    private RedisRepository redisRepository;
//    @Autowired
//    private ClueInforRepository clueInforRepository;
//    @Autowired
//    private GpsAppInfoRepository gpsAppInfoRepository;
//    @Autowired
//    private RecoveryCompanyTempRepository recoveryCompanyTempRepository;
//    @Autowired
//    private VehicleTaskRecoveryRepository vehicleTaskRecoveryRepository;
//    @Autowired
//    private SysUserService sysUserService;
//    @Autowired
//    private RecoveryUserService recoveryUserService;
//    @Autowired
//    private LeaseCompanyRepository leaseCompanyRepository;
//    @Autowired
//    private RecoveryCompanyRepository recoveryCompanyRepository;
//    @Autowired
//    private SystemUserRepository systemUserRepository;
//    @Autowired
//    private VehicleAuthorizationRepository vehicleAuthorizationRepository;
//    @Autowired
//    private AuthorizationHistoryRepository authorizationHistoryRepository;
//
//    /**
//     * 委托机构获取token
//     *
//     * @return
//     */
//    public ResponseEntity<RestResponse> getToken(String userName, String password){
//        if(StringUtil.isNull(userName) || StringUtil.isNull(password)){
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","用户名或密码不可为空"),
//                    HttpStatus.OK);
//        }
//        SystemUser systemUser = sysUserService.findSysUserByUserId(userName);
//        //如果用户名不存在或者此用户类型非委托公司
//        if(systemUser == null || !UserRoleEnums.LEASE_ADMIN.getType().equals(systemUser.getUserRole())){
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","用户名不正确"),
//                    HttpStatus.OK);
//        }
//        String pwd = EncodeUtils.MD5(EncodeUtils.getBase64(password));
//        if(!systemUser.getUserPassword().equals(pwd)){
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","密码不正确"),
//                    HttpStatus.OK);
//        }
//        String uuid = UUID.randomUUID().toString().replace("-", "");
//        redisRepository.save(RedisKeys.lease_recovery + userName, userName + ":" + uuid, 60*60*24);
//        return new ResponseEntity<RestResponse>(
//                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,userName + ":" + uuid,"获取token成功"),
//                HttpStatus.OK);
//    }
//
//    /**
//     * 委托机构推送工单
//     *
//     * @return
//     */
//    @Transactional
//    public ResponseEntity<RestResponse> pushVehicleTask(String token, VehicleTaskPushVo vo){
//        boolean tokenFlag = verifyToken(token);
//        if(!tokenFlag){
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","token验证失败"),
//                    HttpStatus.OK);
//        }
//        String userName = token.split(":")[0];
//        //从token中获取委托公司用户名
//        vo.setLeaseCompanyUserName(userName);
//        //找出委托公司用户名
//        SystemUser systemUser = sysUserService.findSysUserByUserId(userName);
//        if(systemUser != null && StringUtil.isNotNull(systemUser.getLeaseId())){
//            LeaseCompany leaseCompany = leaseCompanyRepository.selectByPrimaryKey(systemUser.getLeaseId());
//            vo.setLeaseCompanyName(leaseCompany.getLeaseFullName());
//        }
//        //必要参数为空提示信息，初始为""
//        StringBuffer param = new StringBuffer("");
//        checkPushTaskParam(vo, param);
//        if(!param.toString().isEmpty()){
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","推送失败，" + param + "不可为空"),
//                    HttpStatus.OK);
//        }
//        //判断工单表中是否存在此车牌号
//        Example example = new Example(VehicleTask.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("plate", vo.getPlate());
//        example.setOrderByClause(" update_time desc ");
//        VehicleTask taskOld = vehicleTaskRepository.selectOneByExample(example);
//        //如果存在
//        if(taskOld != null){
//            //只有工单状态为取消的收车任务才可以再推送
//            if(!TaskStatusEnums.CANCEL.getType().equals(taskOld.getTaskStatus())){
//                return new ResponseEntity<RestResponse>(
//                        RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该收车任务已存在"),
//                        HttpStatus.OK);
//            }
//        }
//        //构建任务工单表数据
//        VehicleTask task = buildVehicleTask(vo);
//        VehicleTask vehicleTask = vehicleTaskRepository.insertOne(task);
//        //判断入参收车公司id(即主系统)是否存在
//        RecoveryCompany recoveryCompany = null;
//        if(!StringUtil.isNull(vo.getRecoveryCompanyId())){
//            Example exam = new Example(RecoveryCompany.class);
//            Example.Criteria criter = exam.createCriteria();
//            criter.andEqualTo("recoveryCompanyId", userName + "_" + vo.getRecoveryCompanyId());
//            recoveryCompany = recoveryCompanyRepository.selectOneByExample(exam);
//            if(recoveryCompany == null){
//                return new ResponseEntity<RestResponse>(
//                        RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未找到该收车公司"),
//                        HttpStatus.OK);
//            }
//        }
//        //db操作，insert工单表和收车公司任务表
//        saveRecovery(vehicleTask.getId(),vo.getLeaseCompanyUserName(),recoveryCompany);
//
//        return new ResponseEntity<RestResponse>(
//                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","推送成功"),
//                HttpStatus.OK);
//    }
//
//    /**
//     * 委托机构推送收车app信息
//     *
//     * @return
//     */
//    public ResponseEntity<RestResponse> pushAppInfo(String token, AppInfoPushVo vo){
//        boolean tokenFlag = verifyToken(token);
//        if(!tokenFlag){
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","token验证失败"),
//                    HttpStatus.OK);
//        }
//        String userName = token.split(":")[0];
//        //从token中获取委托公司用户名
//        vo.setLeaseCompanyUserName(userName);
//        //必要参数为空提示信息，初始为""
//        StringBuffer param = new StringBuffer("");
//        checkAppInfoParam(vo,param);
//        if(!param.toString().isEmpty()){
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","推送失败，" + param + "不可为空"),
//                    HttpStatus.OK);
//        }
//        //判断app信息表中是否存在此委托机构的收车app信息
//        Example example = new Example(GpsAppInfo.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("leaseCompanyUserName", vo.getLeaseCompanyUserName());
//        example.setOrderByClause(" update_time desc ");
//        GpsAppInfo gpsAppInfo =gpsAppInfoRepository.selectOneByExample(example);
//        //如果不存在,insert
//        Date date = new Date();
//        if(gpsAppInfo == null){
//            gpsAppInfo = new GpsAppInfo();
//            gpsAppInfo.setCreator(vo.getLeaseCompanyUserName());
//            gpsAppInfo.setUpdater(vo.getLeaseCompanyUserName());
//            gpsAppInfo.setCreateTime(date);
//            gpsAppInfo.setUpdateTime(date);
//            gpsAppInfo.setLeaseCompanyUserName(vo.getLeaseCompanyUserName());
//            gpsAppInfo.setAndroidUrl(vo.getAndroidUrl());
//            gpsAppInfo.setIosUrl(vo.getIosUrl());
//            gpsAppInfoRepository.insertOne(gpsAppInfo);
//        } else {
//            //如果存在update
//            gpsAppInfo.setUpdater(vo.getLeaseCompanyUserName());
//            gpsAppInfo.setUpdateTime(date);
//            gpsAppInfo.setLeaseCompanyUserName(vo.getLeaseCompanyUserName());
//            gpsAppInfo.setAndroidUrl(vo.getAndroidUrl());
//            gpsAppInfo.setIosUrl(vo.getIosUrl());
//            gpsAppInfoRepository.updateByPrimaryKey(gpsAppInfo);
//        }
//        return new ResponseEntity<RestResponse>(
//                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","推送成功"),
//                HttpStatus.OK);
//    }
//
//    /**
//     * 委托机构推送新的收车公司
//     *
//     * @return
//     */
//    public ResponseEntity<RestResponse> pushRecoveryCompany(String token, RecoveryCompanyPushVo vo){
//        boolean tokenFlag = verifyToken(token);
//        if(!tokenFlag){
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","token验证失败"),
//                    HttpStatus.OK);
//        }
//        //必要参数为空提示信息，初始为""
//        StringBuffer param = new StringBuffer("");
//        checkAddCompanyParam(vo, param);
//        if(!param.toString().isEmpty()){
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","推送失败，" + param + "不可为空"),
//                    HttpStatus.OK);
//        }
//        String userName = token.split(":")[0];
//        //判断入参收车公司id是否存在
//        Example exam = new Example(RecoveryCompany.class);
//        Example.Criteria criter = exam.createCriteria();
//        criter.orEqualTo("recoveryCompanyId", userName + "_" + vo.getId());
//        criter.orEqualTo("recoveryFullName", vo.getRecoveryFullName());
//        RecoveryCompany recoveryCompany = recoveryCompanyRepository.selectOneByExample(exam);
//        if (recoveryCompany != null){
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该收车公司已存在"),
//                    HttpStatus.OK);
//        }
//        vo.setId(userName + "_" + vo.getId());
//        //db操作,插入收车公司表
//        saveRecoveryCompany(vo);
//        return new ResponseEntity<RestResponse>(
//                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","推送成功"),
//                HttpStatus.OK);
//    }
//
//    /**
//     * 收车任务取消时，确认该收车任务是否授权给其他收车公司
//     *
//     * @param token 访问凭证
//     * @param vo 参数信息
//     * @return
//     */
//    public ResponseEntity<RestResponse> cancelComfirm(String token, RecoveryStatusPushVo vo){
//        boolean tokenFlag = verifyToken(token);
//        if(!tokenFlag){
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","token验证失败"),
//                    HttpStatus.OK);
//        }
//        CancelConfirmVo cancelConfirmVo = null;
//        //1，根据车牌号和车牌号查找任务工单最新一条数据
//        VehicleTask task = getVehicleTask(vo.getPlate(),vo.getVehicleIdentifyNum());
//        if(task == null){
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该收车任务不存在"),
//                    HttpStatus.OK);
//        }
//        // 获取此工单是否有已授权状态
//        VehicleAuthorization vehicleAuthorization = getByUserIdAndTaskIdAndStatus(String.valueOf(task.getId()), AuthorizationStatusEnums.AUTHORIZETED.getType());
//        if(vehicleAuthorization != null){
//            cancelConfirmVo = new CancelConfirmVo();
//            // 获取被授权用户的收车公司id
//            SystemUser user = selectSystemUserByUserId(vehicleAuthorization.getUserId());
//            // 根据收车公司id获取收车公司信息
//            RecoveryCompany recoveryCompany = getByRecoveryCompanyById(user.getRecoveryCompanyId());
//            if(recoveryCompany != null){
//                cancelConfirmVo.setRecoveryCompanyName(recoveryCompany.getRecoveryFullName());
//                cancelConfirmVo.setTelPhoneNum(recoveryCompany.getContactPhone());
//                cancelConfirmVo.setContactEmail(recoveryCompany.getContactEmail());
//            }else{
//                cancelConfirmVo.setRecoveryCompanyName(user.getUserName());
//                cancelConfirmVo.setTelPhoneNum(user.getUserPhone());
//            }
//            cancelConfirmVo.setOutTimeDate(vehicleAuthorization.getAuthorizationOutTimeDate());
//            cancelConfirmVo.setStartDate(vehicleAuthorization.getOperateDate());
//        }
//        return new ResponseEntity<RestResponse>(
//                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,cancelConfirmVo,""),
//                HttpStatus.OK);
//
//    }
//    /**
//     * 收车任务取消时，主系统推送取消时间
//     *
//     * @param token
//     * @param vo
//     * @return
//     */
//    public ResponseEntity<RestResponse> pushRecoveryCanal(String token, RecoveryStatusPushVo vo){
//        boolean tokenFlag = verifyToken(token);
//        if(!tokenFlag){
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","token验证失败"),
//                    HttpStatus.OK);
//        }
//        //1，根据车牌号和车牌号查找任务工单最新一条数据
//        VehicleTask task = getVehicleTask(vo.getPlate(),vo.getVehicleIdentifyNum());
//        if(task == null){
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该收车任务不存在"),
//                    HttpStatus.OK);
//        }else if(TaskStatusEnums.CANCEL.getType().equals(task.getTaskStatus())){
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该任务已取消，不要重复操作"),
//                    HttpStatus.OK);
//        }else if(TaskStatusEnums.FINISH.getType().equals(task.getTaskStatus())){
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该任务已完成，不可取消"),
//                    HttpStatus.OK);
//        }
//        // 任务状态 --- 已取消
//        task.setTaskStatus(TaskStatusEnums.CANCEL.getType());
//        // 取消时间
//        task.setUpdateTime(DateUtil.getDateTime(vo.getCanalDate()));
//        // 1.更新任务工单表
//        vehicleTaskRepository.updateByPrimaryKey(task);
//            //根据任务id和收车公司id获取收车公司派单信息
//        VehicleTaskRecovery vehicleTaskRecovery = getByTaskId(String.valueOf(task.getId()));
//        if(vehicleTaskRecovery != null){
//            // 状态---已取消
//            vehicleTaskRecovery.setStatus(RecoveryTaskStatusEnums.CANCEL.getType());
//            // 取消时间
//            vehicleTaskRecovery.setUpdateTime(DateUtil.getDateTime(vo.getCanalDate()));
//            // 2.更新收车公司派单表
//            vehicleTaskRecoveryRepository.updateByPrimaryKey(vehicleTaskRecovery);
//        }
//        // 获取此工单是否有已授权状态
//        VehicleAuthorization vehicleAuthorization = getByUserIdAndTaskIdAndStatus(String.valueOf(task.getId()), AuthorizationStatusEnums.AUTHORIZETED.getType());
//        if(vehicleAuthorization != null){
//            // 授权状态---委托公司取消
//            vehicleAuthorization.setAuthorizationStatus(AuthorizationStatusEnums.LEASECANCEL.getType());
//            // 操作时间----取消时间
//            vehicleAuthorization.setOperateDate(DateUtil.getDateTime(vo.getCanalDate()));
//            // 取消证书url
//            vehicleAuthorization.setCancelPaperUrl(vo.getCancelPaperUrl());
//            // 取消原因
//            vehicleAuthorization.setCancelReason(vo.getCancelReason());
//            vehicleAuthorization.setUpdateTime(DateUtil.getDateTime(vo.getCanalDate()));
//            // 3,更新授权表
//            vehicleAuthorizationRepository.updateByPrimaryKeySelective(vehicleAuthorization);
//            // 登录授权日志表
//            AuthorizationHistory history = new AuthorizationHistory();
//            // 授权id
//            history.setAuthorizationId(vehicleAuthorization.getId());
//            // 用户id
//            history.setUserId(vehicleAuthorization.getUserId());
//            // 操作时间
//            history.setOperateTime(new Date());
//            // 操作内容
//            history.setOperateContent(OperateContentEnums.LEASECANCEL.getType());
//            // 备注
//            history.setRemark(OperateContentEnums.FINISH.getValue());
//            // 4.登录操作日志表
//            authorizationHistoryRepository.insertOne(history);
//        }
//        return new ResponseEntity<RestResponse>(
//                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","推送成功"),
//                HttpStatus.OK);
//    }
//    /**
//     * 收车任务完成时，主系统推送完成时间
//     *
//     * @param token
//     * @param vo
//     * @return
//     */
//    public ResponseEntity<RestResponse> pushRecoveryFinish(String token, RecoveryStatusPushVo vo){
//        boolean tokenFlag = verifyToken(token);
//        if(!tokenFlag){
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","token验证失败"),
//                    HttpStatus.OK);
//        }
//        //1，根据车牌号和车牌号查找任务工单最新一条数据
//        VehicleTask task = getVehicleTask(vo.getPlate(), vo.getVehicleIdentifyNum());
//        if(task == null){
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该收车任务不存在"),
//                    HttpStatus.OK);
//        }else if(TaskStatusEnums.CANCEL.getType().equals(task.getTaskStatus())){
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该任务已取消"),
//                    HttpStatus.OK);
//        }else if(TaskStatusEnums.FINISH.getType().equals(task.getTaskStatus())){
//            return new ResponseEntity<RestResponse>(
//                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该任务已完成，不可重复操作"),
//                    HttpStatus.OK);
//        }
//        // 任务状态 --- 已完成
//        task.setTaskStatus(TaskStatusEnums.FINISH.getType());
//        // 完成时间
//        task.setUpdateTime(DateUtil.getDateTime(vo.getFinishDate()));
//        // 1.更新任务工单表
//        vehicleTaskRepository.updateByPrimaryKey(task);
//        // 获取此工单是否有已授权状态
//        VehicleAuthorization vehicleAuthorization = getByUserIdAndTaskIdAndStatus(String.valueOf(task.getId()), AuthorizationStatusEnums.AUTHORIZETED.getType());
//        //根据任务id和收车公司id获取收车公司派单信息
//        VehicleTaskRecovery vehicleTaskRecovery = getByTaskId(String.valueOf(task.getId()));
//        if(vehicleTaskRecovery != null){
//            if(vehicleAuthorization != null){
//                // 获取被授权用户的收车公司id
//                SystemUser user = selectSystemUserByUserId(vehicleAuthorization.getUserId());
//                // 如果收车公司自己申请被授权，状态更新为 04:自己已完成
//                if(vehicleTaskRecovery.getRecoveryCompanyId().equals(user.getRecoveryCompanyId())){
//                    vehicleTaskRecovery.setStatus(RecoveryTaskStatusEnums.SELFFINISH.getType());
//                    // 否则，状态更新为 05：他人已完成
//                }else{
//                    vehicleTaskRecovery.setStatus(RecoveryTaskStatusEnums.OTHERFINISH.getType());
//                }
//                // 如果没有已授权状态，状态更新为 04:自己已完成
//            }else{
//                vehicleTaskRecovery.setStatus(RecoveryTaskStatusEnums.SELFFINISH.getType());
//            }
//            vehicleTaskRecovery.setUpdateTime(DateUtil.getDateTime(vo.getFinishDate()));
//            // 2.更新收车公司派单表
//            vehicleTaskRecoveryRepository.updateByPrimaryKey(vehicleTaskRecovery);
//        }
//        if(vehicleAuthorization != null){
//            // 3.更新授权状态为已完成
//            vehicleAuthorization.setAuthorizationStatus(AuthorizationStatusEnums.FINISH.getType());
//            // 操作时间
//            vehicleAuthorization.setOperateDate(DateUtil.getDateTime(vo.getFinishDate()));
//            vehicleAuthorization.setUpdateTime(DateUtil.getDateTime(vo.getFinishDate()));
//            vehicleAuthorizationRepository.updateByPrimaryKeySelective(vehicleAuthorization);
//            // 登录授权日志表
//            AuthorizationHistory history = new AuthorizationHistory();
//            // 授权id
//            history.setAuthorizationId(vehicleAuthorization.getId());
//            // 用户id
//            history.setUserId(vehicleAuthorization.getUserId());
//            // 操作时间
//            history.setOperateTime(new Date());
//            // 操作内容
//            history.setOperateContent(OperateContentEnums.FINISH.getType());
//            // 备注
//            history.setRemark(OperateContentEnums.FINISH.getValue());
//            authorizationHistoryRepository.insertOne(history);
//        }
//        return new ResponseEntity<RestResponse>(
//                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","推送成功"),
//                HttpStatus.OK);
//    }
//    /**
//     * 根据车牌号和车架号查找任务工单
//     * @param plate 车牌号
//     * @param vehicleIdentifyNum 车架号
//     * @return
//     */
//    private VehicleTask getVehicleTask(String plate, String vehicleIdentifyNum){
//        VehicleTask task = null;
//        Example example = new Example(VehicleTask.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("plate", plate);
//        criteria.andEqualTo("vehicleIdentifyNum", vehicleIdentifyNum);
//        example.setOrderByClause(" update_time desc ");
//        task = vehicleTaskRepository.selectOneByExample(example);
//        return  task;
//    }
//    /**
//     * 根据任务id获取收车公司派单信息
//     * @param taskId 任务id
//     * @return
//     */
//    private VehicleTaskRecovery getByTaskId(String taskId){
//        Example example = new Example(VehicleTaskRecovery.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("taskId", taskId);
//        VehicleTaskRecovery vehicleTaskRecovery = null;
//        vehicleTaskRecovery = vehicleTaskRecoveryRepository.selectOneByExample(example);
//        return  vehicleTaskRecovery;
//    }
//    /**
//     *  根据收车公司id获取收车公司信息
//     * @param recoveryCompanyId 收车公司id
//     * @return
//     */
//    private RecoveryCompany getByRecoveryCompanyId(String recoveryCompanyId){
//        RecoveryCompany recoveryCompany = null;
//        //判断入参收车公司id是否存在
//        Example exam = new Example(RecoveryCompany.class);
//        Example.Criteria criter = exam.createCriteria();
//        criter.andEqualTo("recoveryCompanyId", recoveryCompanyId);
//        recoveryCompany = recoveryCompanyRepository.selectOneByExample(exam);
//        return recoveryCompany;
//    }
//    /**
//     *  根据收车公司id获取收车公司信息
//     * @param id 收车公司id
//     * @return
//     */
//    private RecoveryCompany getByRecoveryCompanyById(String id){
//        RecoveryCompany recoveryCompany = null;
//        //判断入参收车公司id是否存在
//        Example exam = new Example(RecoveryCompany.class);
//        Example.Criteria criter = exam.createCriteria();
//        criter.andEqualTo("id", id);
//        recoveryCompany =recoveryCompanyRepository.selectOneByExample(exam);
//        return recoveryCompany;
//    }
//
//    /**
//     * 根据任务id，授权状态获取授权信息
//     *
//     * @param taskId 任务id
//     * @param status 状态
//     * @return
//     */
//    private VehicleAuthorization getByUserIdAndTaskIdAndStatus(String taskId, String status){
//        if(StringUtil.isNotNull(taskId) && StringUtil.isNotNull(status)) {
//            Example example = new Example(VehicleAuthorization.class);
//            Example.Criteria criteria = example.createCriteria();
//            criteria.andEqualTo("taskId", taskId);
//            criteria.andEqualTo("authorizationStatus", status);
//            return vehicleAuthorizationRepository.selectOneByExample(example);
//        }
//        return null;
//    }
//    /**
//     * 根据用户id（手机号）获取用户信息
//     * @param userId 用户id
//     * @return
//     */
//    private SystemUser selectSystemUserByUserId(String userId){
//        if(StringUtil.isNotNull(userId)) {
//            Example example = new Example(SystemUser.class);
//            Example.Criteria criteria = example.createCriteria();
//            criteria.andEqualTo("userId", userId);
//            return systemUserRepository.selectOneByExample(example);
//        }
//        return null;
//    }
//
//    /**
//     * 批量导入收车公司——测试用
//     *
//     * @return
//     */
//    public ResponseEntity<RestResponse> test(){
//        String userName = "taimeng";
//        List<RecoveryCompanyTemp> companyList = recoveryCompanyTempRepository.selectAll();
//
//        List<String> codes = new ArrayList();
//        //取出所有收车公司注册码
//        codes = recoveryCompanyRepository.selectRegisterCode();
//
//        List<RecoveryCompany> resultList = new ArrayList();
//        RecoveryCompany company;
//        for(RecoveryCompanyTemp item : companyList){
//            company = new RecoveryCompany();
//            BeanUtils.copyProperties(item,company);
//            company.setId(null);
//            company.setRecoveryCompanyId(userName + "_" + item.getRecoveryCompanyId());
//            recoveryUserService.buildRegisterCode(company);
//            resultList.add(company);
//        }
//        recoveryCompanyRepository.insertListByMapper(resultList);
//        return new ResponseEntity<RestResponse>(
//                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","推送成功"),
//                HttpStatus.OK);
//    }
//
//    /**
//     * 构建任务工单表数据
//     * @param vo
//     * @return
//     */
//    private VehicleTask buildVehicleTask(VehicleTaskPushVo vo) {
//        VehicleTask task = new VehicleTask();
//        task.setCreateTime(new Date());
//        task.setUpdateTime(task.getCreateTime());
//        task.setCreator(vo.getLeaseCompanyUserName());
//        task.setUpdater(vo.getLeaseCompanyUserName());
//        task.setName(vo.getName());
//        task.setPlate(vo.getPlate());
//        task.setServiceFee(vo.getServiceFee());
//        task.setVehicleProvince(vo.getVehicleProvince());
//        task.setVehicleCity(vo.getVehicleCity());
//        task.setVehicleIdentifyNum(vo.getVehicleIdentifyNum());
//        task.setEngineNo(vo.getEngineNo());
//        task.setVehicleType(vo.getVehicleType());
//        task.setVehicleColor(vo.getVehicleColor());
//        task.setInsuranceInfo(vo.getInsuranceInformation());
//        task.setViolationInfo(vo.getViolationInformation());
//        task.setGpsSystemUserName(vo.getGpsSystemUserName());
//        task.setGpsSystemUserPassword(vo.getGpsSystemUserPassword());
//        task.setLeaseCompanyName(vo.getLeaseCompanyName());
//        task.setLeaseCompanyUserName(vo.getLeaseCompanyUserName());
//        task.setTaskStatus(TaskStatusEnums.NORMAL.getType());
//        //GPS有无
//        if(StringUtil.isNotNull(vo.getGpsSystemUserName()) && StringUtil.isNotNull(vo.getGpsSystemUserPassword())){
//            task.setHasGpsFlag("1");
//        } else {
//            task.setHasGpsFlag("0");
//        }
//        //违章信息有无
//        if(StringUtil.isNotNull(vo.getViolationInformation())){
//            task.setVolitionFlag("1");
//        } else {
//            task.setVolitionFlag("0");
//        }
//        //线索信息有无
//        Example example2 = new Example(ClueInfo.class);
//        Example.Criteria criteria = example2.createCriteria();
//        criteria.andEqualTo("plate", vo.getPlate());
//        List<ClueInfo> clueInfoList = clueInforRepository.selectByExampleList(example2);
//        if(clueInfoList != null && !clueInfoList.isEmpty()){
//            task.setClueFlag("1");
//        } else {
//            task.setClueFlag("0");
//        }
//        // 设定价格区间
//        Double serviceFee = vo.getServiceFee();
//        if(new Double(0) <= serviceFee && serviceFee < new Double(10000)){
//            task.setPriceRange(ServiceFeeEnum.RANGE_A.value());
//        } else if(new Double(10000) <= serviceFee && serviceFee < new Double(20000)){
//            task.setPriceRange(ServiceFeeEnum.RANGE_B.value());
//        } else if(new Double(20000) <= serviceFee && serviceFee < new Double(50000)){
//            task.setPriceRange(ServiceFeeEnum.RANGE_C.value());
//        }  else if(serviceFee >= new Double(50000)){
//            task.setPriceRange(ServiceFeeEnum.RANGE_D.value());
//        }
//        return task;
//    }
//
//    /**
//     * db操作，insert工单表和收车公司任务表
//     * @param taskId
//     * @param leaseCompanyUserName
//     * @param recoveryCompany
//     */
//    @Transactional
//    private void saveRecovery(Long taskId, String leaseCompanyUserName, RecoveryCompany recoveryCompany) {
//        Date date = new Date();
//
//        if(recoveryCompany == null){
//            //如果没有传递主系统收车公司id
//            return;
//        }
//        //insert收车公司任务表
//        VehicleTaskRecovery vehicleTaskRecovery = new VehicleTaskRecovery();
//        vehicleTaskRecovery.setTaskId(taskId);
//        vehicleTaskRecovery.setRecoveryCompanyId(recoveryCompany.getId());
//        Date startDate = new Date();
//        Calendar calendar = DateUtil.getCalendar(startDate);
//        calendar.add(Calendar.DAY_OF_YEAR,14);//该任务有效期为14天
//        Date failureTime = calendar.getTime();
//        vehicleTaskRecovery.setStartTime(startDate);
//        vehicleTaskRecovery.setFailureTime(failureTime);
//        vehicleTaskRecovery.setStatus(RecoveryTaskStatusEnums.WAITRECOVERY.getType());//状态设为初始状态：正常
//        vehicleTaskRecovery.setCreator(leaseCompanyUserName);
//        vehicleTaskRecovery.setUpdater(leaseCompanyUserName);
//        vehicleTaskRecovery.setCreateTime(date);
//        vehicleTaskRecovery.setUpdateTime(date);
//        vehicleTaskRecoveryRepository.insertOne(vehicleTaskRecovery);
//    }
//
//    /**
//     * db操作,插入收车公司表
//     * @param vo
//     */
//    @Transactional
//    private void saveRecoveryCompany(RecoveryCompanyPushVo vo) {
//        //在收车公司表中新增一条数据
//        RecoveryCompany recoveryCompany = new RecoveryCompany();
//        BeanUtils.copyProperties(vo,recoveryCompany);
//        recoveryCompany.setId(null);
//        recoveryUserService.buildRegisterCode(recoveryCompany);
//        recoveryCompany.setRecoveryCompanyId(vo.getId());
//        recoveryCompanyRepository.insertOne(recoveryCompany);
//    }
//
//    /**
//     * 验证token正确性
//     * @param token
//     * @return
//     */
//    private boolean verifyToken(String token) {
//        String [] values = token.split(":");
//        if(values.length != 2){
//            return false;
//        }
//        String userName = values[0];
//        //比对redis中的token和入参的token
//        String redisToken = (String)redisRepository.get(RedisKeys.lease_recovery + userName);
//        if(redisToken == null || !redisToken.equals(token)){
//            return false;
//        }
//        //比对成功则清空此次token值
//        redisRepository.delete(RedisKeys.lease_recovery + userName);
//        return true;
//    }
//
//    /**
//     * 推送收车公司时check必要参数是否为空
//     * @param vo
//     * @param param
//     */
//    private void checkAddCompanyParam(RecoveryCompanyPushVo vo, StringBuffer param) {
//        if(StringUtil.isNull(vo.getId())){
//            param.append("收车公司主键、");
//        }
//        if(StringUtil.isNull(vo.getRecoveryFullName())){
//            param.append("收车公司全称、");
//        }
//        if(StringUtil.isNull(vo.getContactName())){
//            param.append("收车公司联系人姓名、");
//        }
//        if(StringUtil.isNull(vo.getContactPhone())){
//            param.append("收车公司联系人号码、");
//        }
//        if(StringUtil.isNull(vo.getContactEmail())){
//            param.append("收车公司联系人邮箱、");
//        }
//        if(!param.toString().isEmpty()){
//            param.replace(param.length()-1,param.length(),"");
//        }
//    }
//
//    /**
//     * 推送工单时check必要参数是否为空
//     * @param vo
//     * @param param
//     */
//    private void checkPushTaskParam(VehicleTaskPushVo vo, StringBuffer param) {
//        if(StringUtil.isNull(vo.getName())){
//            param.append("车主姓名");
//        }
//        if(StringUtil.isNull(vo.getPlate())){
//            param.append("车牌号、");
//        }
//        if(StringUtil.isNull(vo.getVehicleIdentifyNum())){
//            param.append("车架号、");
//        }
//        if(StringUtil.isNull(vo.getServiceFee())){
//            param.append("服务费、");
//        }
//        if(!param.toString().isEmpty()){
//            param.replace(param.length()-1,param.length(),"");
//        }
//    }
//
//    /**
//     * 推送收车app信息时check必要参数是否为空
//     * @param vo
//     * @param param
//     */
//    private void checkAppInfoParam(AppInfoPushVo vo, StringBuffer param) {
//        if(StringUtil.isNull(vo.getAndroidUrl())){
//            param.append("app安卓下载链接");
//        }
//        if(StringUtil.isNull(vo.getIosUrl())){
//            param.append("app苹果下载链接、");
//        }
//        if(!param.toString().isEmpty()){
//            param.replace(param.length()-1,param.length(),"");
//        }
//    }
}
