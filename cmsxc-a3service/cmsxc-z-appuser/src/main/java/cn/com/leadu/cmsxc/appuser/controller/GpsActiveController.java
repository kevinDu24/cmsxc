package cn.com.leadu.cmsxc.appuser.controller;

import cn.com.leadu.cmsxc.appuser.service.GpsActiveService;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appuser.vo.GpsActiveVo;
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
 贴GPSController
 */
@RestController
@RequestMapping("gpsActive")
public class GpsActiveController {
    private static final Logger logger = LoggerFactory.getLogger(GpsActiveController.class);
    @Autowired
    private GpsActiveService gpsActiveService;

    /**
     * 激活check，验证对应车架号是否存在及是否对应多个车架号
     * @param vehicleIdentifyNum 车架号后六位
     * @return
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> check(String vehicleIdentifyNum) {
        try{
            return gpsActiveService.check(vehicleIdentifyNum);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("check车架号后六位是否有效error",ex);
            throw new CmsServiceException("check车架号后六位是否有效失败");
        }
    }

    /**
     * 贴gps
     * @param gpsActiveVo gps相关信息
     * @return
     */
    @RequestMapping(value = "/active", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> active(@AuthUserInfo SystemUser systemUser, @RequestBody GpsActiveVo gpsActiveVo) {
        try{
            return gpsActiveService.active(gpsActiveVo, systemUser.getUserId());
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("贴gpserror",ex);
            throw new CmsServiceException("贴gps失败");
        }
    }

    /**
     * 分页返回激活历史列表
     * @param systemUser 用户信息
     * @param page 当前页
     * @size size 每页个数
     * @return
     */
    @RequestMapping(value = "/getActiveList", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getActiveList(@AuthUserInfo SystemUser systemUser, int page, int size) {
        try{
            return gpsActiveService.getActiveList(systemUser.getUserId(),page,size);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("分页返回激活历史列表error",ex);
            throw new CmsServiceException("分页返回激活历史列表失败");
        }
    }
}
