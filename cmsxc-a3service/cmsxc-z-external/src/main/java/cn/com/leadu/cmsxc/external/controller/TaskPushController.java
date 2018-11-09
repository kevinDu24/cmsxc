package cn.com.leadu.cmsxc.external.controller;

import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.external.service.TaskPushService;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.AppInfoPushVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryCompanyPushVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryStatusPushVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.VehicleTaskPushVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @Autowired
    private TaskPushService taskPushService;

    /**
     * 委托机构推送工单
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getToken(String userName, String password) {
        try{
            return taskPushService.getToken(userName, password);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取token error",ex);
            throw new CmsServiceException("获取token失败");
        }
    }

    /**
     * 委托机构推送工单
     *
     * @param token 访问凭证
     * @return
     */
    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> pushVehicleTask(String token, @RequestBody VehicleTaskPushVo vo) {
        try{
            return taskPushService.pushVehicleTask(token, vo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("委托机构推送工单error",ex);
            throw new CmsServiceException("委托机构推送工单失败");
        }
    }

    /**
     * 委托机构推送收车app相关信息
     *
     * @param token 访问凭证
     * @return
     */
    @RequestMapping(value = "/appInfo", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> pushAppInfo(String token, @RequestBody AppInfoPushVo vo) {
        try{
            return taskPushService.pushAppInfo(token, vo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("委托机构推送工单error",ex);
            throw new CmsServiceException("委托机构推送工单失败");
        }
    }

    /**
     * 委托机构推送收车公司相关信息，委托公司在新增收车公司的时候调用
     *
     * @param token 访问凭证
     * @return
     */
    @RequestMapping(value = "/addRecoveryCompany", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> recoveryCompany(String token, @RequestBody RecoveryCompanyPushVo vo) {
        try{
            return taskPushService.pushRecoveryCompany(token, vo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("委托机构推送收车公司error",ex);
            throw new CmsServiceException("委托机构推送收车公司失败");
        }
    }
    /**
     * 委托公司推送完成收车
     *
     * @param token 访问凭证
     * @param vo
     * @return
     */
    @RequestMapping(value = "/recoveryFinish", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> pushRecoveryFinish(String token, @RequestBody RecoveryStatusPushVo vo) {
        try{
            return taskPushService.pushRecoveryFinish(token, vo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("委托公司推送完成收车error",ex);
            throw new CmsServiceException("委托公司推送完成收车失败");
        }
    }
    /**
     * 主系统收车车辆gps在线变离线时的推送接口
     *
     * @param token 访问凭证
     * @param vehicleIdentifyNum 车架号
     * @return
     */
    @RequestMapping(value = "/pushGpsOnline", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> pushGpsOnline(String token, String vehicleIdentifyNum) {
        try{
            return taskPushService.pushGpsOnline(token, vehicleIdentifyNum);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("委托公司收车车辆gps在线变离线时的推送error",ex);
            throw new CmsServiceException("委托收车车辆gps在线变离线时的推送失败");
        }
    }/**
     * 主系统收车车辆服务费变更推送
     *
     * @param token 访问凭证
     * @param vehicleIdentifyNum 车架号
     * @return
     */
    @RequestMapping(value = "/pushServiceFee", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> pushServiceFee(String token, String vehicleIdentifyNum, String serviceFee) {
        try{
            return taskPushService.pushServiceFee(token, vehicleIdentifyNum, serviceFee);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("委托公司收车车辆服务费变更推送error",ex);
            throw new CmsServiceException("委托公司收车车辆服务费变更推送失败");
        }
    }

    /**
     * 收车任务取消时，确认该收车任务是否授权给其他收车公司
     *
     * @param token 访问凭证
     * @param vo 参数信息
     * @return
     */
    @RequestMapping(value = "/cancelComfirm", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> cancelComfirm(String token, @RequestBody RecoveryStatusPushVo vo) {
        try{
            return taskPushService.cancelComfirm(token, vo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("委托公司取消收车确认error",ex);
            throw new CmsServiceException("委托公司取消收车确认失败");
        }
    }
    /**
     * 委托公司推送取消收车
     *
     * @param token 访问凭证
     * @param vo
     * @return
     */
    @RequestMapping(value = "/recoveryCanal", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> pushRecoveryCanal(String token, @RequestBody RecoveryStatusPushVo vo) {
        try{
            return taskPushService.pushRecoveryCanal(token, vo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("委托公司推送取消收车error",ex);
            throw new CmsServiceException("委托公司推送取消收车失败");
        }
    }

    /**
     * 批量导入收车公司——测试用
     *
     * @return
     */
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> test() {
        try{
            return taskPushService.test();
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("批量导入收车公司——测试用error",ex);
            throw new CmsServiceException("批量导入收车公司——测试用失败");
        }
    }

    /**
     * 赏金寻车二期上线时，寄存数据价格区间重新修正
     *
     * @return
     */
    @RequestMapping(value = "/rebuildPriceRange", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> rebuildPriceRange() {
        try{
            return taskPushService.rebuildPriceRange();
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("赏金寻车二期上线时，寄存数据价格区间重新修正error",ex);
            throw new CmsServiceException("赏金寻车二期上线时，寄存数据价格区间重新修正失败");
        }
    }

    /**
     * 赏金寻车二期上线时，填充收车公司老板注册码
     *
     * @return
     */
    @RequestMapping(value = "/buildBossCode", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> buildBossCode() {
        try{
            return taskPushService.buildBossCode();
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("赏金寻车二期上线时，填充收车公司老板注册码error",ex);
            throw new CmsServiceException("赏金寻车二期上线时，填充收车公司老板注册码失败");
        }
    }

    /**
     * 赏金三期更新工单表历史数据三址、fp所在省份
     *
     * @return
     */
    @RequestMapping(value = "/buildHistoryData", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> buildHistoryData() {
        try{
            return taskPushService.buildHistoryData();
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("赏金三期更新工单表历史数据三址、fp所在省份error",ex);
            throw new CmsServiceException("赏金三期更新工单表历史数据三址、fp所在省份失败");
        }
    }
}
