package cn.com.leadu.cmsxc.appuser.controller;

import cn.com.leadu.cmsxc.appuser.service.LatestPositionAndResortService;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yuanzhenxia on 2018/8/1.
 *
 * 根据车架号查询最新位置信息及常去地址
 */
@RestController
@RequestMapping("latestpositionandresort")
public class LatestPositionAndResortController {

    private static final Logger logger = LoggerFactory.getLogger(LatestPositionAndResortController.class);
    @Autowired
    private LatestPositionAndResortService latestPositionAndResortService;
    /**
     * 根据车架号查询最新位置信息及常去地址
     *
     * @param vehicleIdentifyNum  车架号
     * @param systemUser 当前用户
     * @return
     */
    @RequestMapping(value = "/getLatestPositionAndResort", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getInformation(String vehicleIdentifyNum, @AuthUserInfo SystemUser systemUser) {
        try{
            return latestPositionAndResortService.getLatestPositionAndResort(vehicleIdentifyNum);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("根据车架号查询最新位置信息及常去地址error",ex);
            throw new CmsServiceException("根据车架号查询最新位置信息及常去地址失败");
        }
    }
}
