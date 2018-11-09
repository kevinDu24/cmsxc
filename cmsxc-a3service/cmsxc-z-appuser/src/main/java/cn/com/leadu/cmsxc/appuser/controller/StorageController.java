package cn.com.leadu.cmsxc.appuser.controller;

import cn.com.leadu.cmsxc.appuser.service.StorageService;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appuser.vo.RecoveryDataVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.RecoveryEvidenceVo;
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
 * 我的线索Controller
 */
@RestController
@RequestMapping("storage")
public class StorageController {
    private static final Logger logger = LoggerFactory.getLogger(StorageController.class);
    @Autowired
    private StorageService storageService;

    /**
     * 获取入库状态
     * @param taskId 工单id
     * @return
     */
    @RequestMapping(value = "/getState", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getStorageState(Long taskId){
        try{
            return storageService.getStorageState(taskId);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取入库状态error",ex);
            throw new CmsServiceException("获取入库状态失败");
        }
    }

    /**
     * 推荐附近停车场
     * @param taskId 工单id
    * @param lat 纬度
     * @param lon 经度
     * @return
     */
    @RequestMapping(value = "/getNearbyParking", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getNearbyParking(Long taskId, Double lat, Double lon){
        try{
            return storageService.getNearbyParking(taskId,lat,lon);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("推荐附近停车场error",ex);
            throw new CmsServiceException("推荐附近停车场失败");
        }
    }

    /**
     * 提交收车完成资料
     * @param vo 提交信息
     * @return
     */
    @RequestMapping(value = "/uploadEvidence", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> uploadEvidence(@RequestBody RecoveryEvidenceVo vo, @AuthUserInfo SystemUser systemUser){
        try{
            return storageService.uploadEvidence(vo,systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("提交收车完成资料error",ex);
            throw new CmsServiceException("提交收车完成资料失败");
        }
    }

    /**
     * 获取已选停车场及二维码信息
     * @param taskId 工单id
     * @return
     */
    @RequestMapping(value = "/getSelectedParking", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getSelectedParking(Long taskId, Double lat, Double lon){
        try{
            return storageService.getSelectedParking(taskId,lat,lon);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取已选停车场及二维码信息error",ex);
            throw new CmsServiceException("获取已选停车场及二维码信息失败");
        }
    }

    /**
     * 送车人上传资料
     * @param vo 提交信息
     * @return
     */
    @RequestMapping(value = "/uploadData", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> uploadData(@RequestBody RecoveryDataVo vo, @AuthUserInfo SystemUser systemUser){
        try{
            return storageService.uploadData(vo,systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("送车人上传资料error",ex);
            throw new CmsServiceException("送车人上传资料失败");
        }
    }

}
