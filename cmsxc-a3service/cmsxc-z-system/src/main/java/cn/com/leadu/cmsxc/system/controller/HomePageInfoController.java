package cn.com.leadu.cmsxc.system.controller;

import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.system.vo.information.NewPublishVo;
import cn.com.leadu.cmsxc.system.service.HomePageInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页新闻controller
 * Created by LEO on 16/9/29.
 */
@RestController
@RequestMapping("/informations")
public class HomePageInfoController {

    private static final Logger logger = LoggerFactory.getLogger(LeaseUserController.class);

    @Autowired
    private HomePageInfoService homePageInfoService;

    /**
     * 发布新闻
     * @param newPublishVo
     * @return
     */
    @RequestMapping(value = "/newsPublish", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> newsPublish(@RequestBody NewPublishVo newPublishVo){
        try{
            return homePageInfoService.newsPublish(newPublishVo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("发布新闻error",ex);
            throw new CmsServiceException("发布新闻失败");
        }
    }

}
