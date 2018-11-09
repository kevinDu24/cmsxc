package cn.com.leadu.cmsxc.system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 *委托机构推送Controller
 */
@RestController
@RequestMapping("taskPush")
public class TaskPushController {
    private static final Logger logger = LoggerFactory.getLogger(TaskPushController.class);

//    @Autowired
//    private TaskPushService taskPushService;
//
//    /**
//     * 委托机构推送工单
//     *
//     * @param userName 用户名
//     * @param password 密码
//     * @return
//     */
//    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
//    public ResponseEntity<RestResponse> getToken(String userName, String password) {
//        try{
//            return taskPushService.getToken(userName, password);
//        }catch(Exception ex){
//            ex.printStackTrace();
//            logger.error("获取token error",ex);
//            throw new CmsServiceException("获取token失败");
//        }
//    }
//
//    /**
//     * 委托机构推送工单
//     *
//     * @param token 访问凭证
//     * @return
//     */
//    @RequestMapping(value = "/task", method = RequestMethod.POST)
//    public ResponseEntity<RestResponse> pushVehicleTask(String token, @RequestBody VehicleTaskPushVo vo) {
//        try{
//            return taskPushService.pushVehicleTask(token, vo);
//        }catch(Exception ex){
//            ex.printStackTrace();
//            logger.error("委托机构推送工单error",ex);
//            throw new CmsServiceException("委托机构推送工单失败");
//        }
//    }
//
//    /**
//     * 委托机构推送收车app相关信息
//     *
//     * @param token 访问凭证
//     * @return
//     */
//    @RequestMapping(value = "/appInfo", method = RequestMethod.POST)
//    public ResponseEntity<RestResponse> pushAppInfo(String token, @RequestBody AppInfoPushVo vo) {
//        try{
//            return taskPushService.pushAppInfo(token, vo);
//        }catch(Exception ex){
//            ex.printStackTrace();
//            logger.error("委托机构推送工单error",ex);
//            throw new CmsServiceException("委托机构推送工单失败");
//        }
//    }
//
//    /**
//     * 委托机构推送收车公司相关信息，委托公司在新增收车公司的时候调用
//     *
//     * @param token 访问凭证
//     * @return
//     */
//    @RequestMapping(value = "/addRecoveryCompany", method = RequestMethod.POST)
//    public ResponseEntity<RestResponse> recoveryCompany(String token, @RequestBody RecoveryCompanyPushVo vo) {
//        try{
//            return taskPushService.pushRecoveryCompany(token, vo);
//        }catch(Exception ex){
//            ex.printStackTrace();
//            logger.error("委托机构推送收车公司error",ex);
//            throw new CmsServiceException("委托机构推送收车公司失败");
//        }
//    }
//    /**
//     * 委托公司推送完成收车
//     *
//     * @param token 访问凭证
//     * @param vo
//     * @return
//     */
//    @RequestMapping(value = "/recoveryFinish", method = RequestMethod.POST)
//    public ResponseEntity<RestResponse> pushRecoveryFinish(String token, @RequestBody RecoveryStatusPushVo vo) {
//        try{
//            return taskPushService.pushRecoveryFinish(token, vo);
//        }catch(Exception ex){
//            ex.printStackTrace();
//            logger.error("委托公司推送完成收车error",ex);
//            throw new CmsServiceException("委托公司推送完成收车失败");
//        }
//    }
//    /**
//     * 收车任务取消时，确认该收车任务是否授权给其他收车公司
//     *
//     * @param token 访问凭证
//     * @param vo 参数信息
//     * @return
//     */
//    @RequestMapping(value = "/cancelComfirm", method = RequestMethod.POST)
//    public ResponseEntity<RestResponse> cancelComfirm(String token, @RequestBody RecoveryStatusPushVo vo) {
//        try{
//            return taskPushService.cancelComfirm(token, vo);
//        }catch(Exception ex){
//            ex.printStackTrace();
//            logger.error("委托公司取消收车确认error",ex);
//            throw new CmsServiceException("委托公司取消收车确认失败");
//        }
//    }
//    /**
//     * 委托公司推送取消收车
//     *
//     * @param token 访问凭证
//     * @param vo
//     * @return
//     */
//    @RequestMapping(value = "/recoveryCanal", method = RequestMethod.POST)
//    public ResponseEntity<RestResponse> pushRecoveryCanal(String token, @RequestBody RecoveryStatusPushVo vo) {
//        try{
//            return taskPushService.pushRecoveryCanal(token, vo);
//        }catch(Exception ex){
//            ex.printStackTrace();
//            logger.error("委托公司推送取消收车error",ex);
//            throw new CmsServiceException("委托公司推送取消收车失败");
//        }
//    }
//
//    /**
//     * 批量导入收车公司——测试用
//     *
//     * @return
//     */
//    @RequestMapping(value = "/test", method = RequestMethod.POST)
//    public ResponseEntity<RestResponse> test() {
//        try{
//            return taskPushService.test();
//        }catch(Exception ex){
//            ex.printStackTrace();
//            logger.error("批量导入收车公司——测试用error",ex);
//            throw new CmsServiceException("批量导入收车公司——测试用失败");
//        }
//    }
}
