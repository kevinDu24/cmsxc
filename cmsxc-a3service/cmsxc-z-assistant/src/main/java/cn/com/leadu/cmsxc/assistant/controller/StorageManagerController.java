package cn.com.leadu.cmsxc.assistant.controller;

import cn.com.leadu.cmsxc.assistant.service.StorageManagerService;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ParkingDataVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
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
 *入库管理Controller
 */
@RestController
@RequestMapping("storageManager")
public class StorageManagerController {
    private static final Logger logger = LoggerFactory.getLogger(StorageManagerController.class);
    @Autowired
    private StorageManagerService storageManagerService;

    /**
     * 入库管理列表查询_库管/停车场公司管理员
     *
     * @param systemUser 当前登录人信息
     * @param status 状态 01：入库中，02：已结束
     * @param parkingId 所选停车场id
     * @param plate 车牌号
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    @RequestMapping(value = "/getManagerStorageList", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getManagerStorageList(@AuthUserInfo SystemUser systemUser, String status, String parkingId, String plate, int page, int size) {
        try{
            return storageManagerService.getParkingStorageList(systemUser.getUserId(), systemUser.getUserRole(), parkingId, status, plate, page, size);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("入库管理列表查询_库管/停车场公司管理员error",ex);
            throw new CmsServiceException("入库管理列表查询_库管/停车场公司管理员失败");
        }
    }

    /**
     * 获得停车场管理员下对应的停车场集合
     *
     * @param systemUser 登录人信息
     * @return
     */
    @RequestMapping(value = "/getParkingListAdmin", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getParkingListAdmin(@AuthUserInfo SystemUser systemUser){
        try{
            return storageManagerService.getParkingListAdmin(systemUser.getUserId());
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获得停车场管理员下对应的停车场集合error",ex);
            throw new CmsServiceException("获得停车场管理员下对应的停车场集合失败");
        }
    }

    /**
     * 获取该任务最新入库状态
     *
     * @param storageId 入库id
     * @return
     */
    @RequestMapping(value = "/getLatestState", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getLatestState(String storageId){
        try{
            return storageManagerService.getLatestState(storageId);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取该任务最新入库状态error",ex);
            throw new CmsServiceException("获取该任务最新入库状态失败");
        }
    }

    /**
     * 确认抵达停车场操作
     * @param storageId 入库id
     * @param systemUser 当前登录人信息
     * @return
     */
    @RequestMapping(value = "/arriveParking", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> arriveParking(String storageId, @AuthUserInfo SystemUser systemUser){
        try{
            return storageManagerService.arriveParking(storageId, systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("确认抵达停车场操作error",ex);
            throw new CmsServiceException("确认抵达停车场操作失败");
        }
    }

    /**
     * 获取入库管理车辆详情
     * @param storageId 入库id
     * @return
     */
    @RequestMapping(value = "/getVehicleInfo", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getVehicleInfo(String storageId){
        try{
            return storageManagerService.getVehicleInfo(storageId);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取入库管理车辆详情error",ex);
            throw new CmsServiceException("获取入库管理车辆详情失败");
        }
    }

    /**
     * 停车场相关人员上传资料
     * @param vo 资料对象
     * @param systemUser 当前登录人信息
     * @return
     */
    @RequestMapping(value = "/uploadData", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> uploadData(@RequestBody ParkingDataVo vo, @AuthUserInfo SystemUser systemUser){
        try{
            return storageManagerService.uploadData(vo, systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("停车场相关人员上传资料error",ex);
            throw new CmsServiceException("停车场相关人员上传资料失败");
        }
    }

    /**
     * 获取已提交页面信息接口
     * @param storageId 入库id
     * @return
     */
    @RequestMapping(value = "/getSubmitedInfo", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getSubmitedInfo(String storageId){
        try{
            return storageManagerService.getSubmitedInfo(storageId);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取已提交页面信息接口error",ex);
            throw new CmsServiceException("获取已提交页面信息接口失败");
        }
    }
}
