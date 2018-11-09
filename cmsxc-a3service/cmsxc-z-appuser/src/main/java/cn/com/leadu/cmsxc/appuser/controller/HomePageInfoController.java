package cn.com.leadu.cmsxc.appuser.controller;

import cn.com.leadu.cmsxc.appuser.service.HomePageInfoService;
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
 * Created by yuanzhenxia on 2018/1/18.
 *
 *首页新闻Controller
 */
@RestController
@RequestMapping("information")
public class HomePageInfoController {
    private static final Logger logger = LoggerFactory.getLogger(HomePageInfoController.class);
    @Autowired
    private HomePageInfoService homePageInfoService;
    /**
     * 首页获取新闻列表
     *
     * @param type "1":新闻(bunner图)、"2":首页广告、"3":用户协议、"4":常见问题、"5":关于我们
     * @param systemUser 当前用户
     * @return
     */
    @RequestMapping(value = "/getInformation", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getInformation(String type, @AuthUserInfo SystemUser systemUser) {
        try{
            return homePageInfoService.getInformation(type);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取新闻列表error",ex);
            throw new CmsServiceException("获取新闻列表失败");
        }
    }
    /**
     * 首页获取数据统计
     *
     * @param systemUser 当前用户
     * @return
     */
    @RequestMapping(value = "/getHomePageDataStatistics", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getHomePageDataStatistics( @AuthUserInfo SystemUser systemUser) {
        try{
            return homePageInfoService.getHomePageDataStatistics(systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("首页获取数据统计error",ex);
            throw new CmsServiceException("首页获取数据统计失败");
        }
    }

    /**
     * 首页获取派单数量，消息中心数量
     *
     * @param systemUser 当前用户
     * @return
     */
    @RequestMapping(value = "/getHomeCountInfo", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getHomeCountInfo( @AuthUserInfo SystemUser systemUser) {
        try{
            return homePageInfoService.getHomeCountInfo(systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("首页获取派单数量，消息中心数量error",ex);
            throw new CmsServiceException("首页获取派单数量，消息中心数量失败");
        }
    }

    /**
     * 首页获取滚播消息
     *
     * @return
     */
    @RequestMapping(value = "/getHomeFinishInfo", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getHomeFinishInfo() {
        try{
            return homePageInfoService.getHomeFinishInfo();
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("首页获取滚播消息error",ex);
            throw new CmsServiceException("首页获取滚播消息失败");
        }
    }

    /**
     * 首页获取今日推荐
     *
     * @return
     */
    @RequestMapping(value = "/getTodayRecommend", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getTodayRecommend(@AuthUserInfo SystemUser systemUser, String province) {
        try{
            return homePageInfoService.getTodayRecommend(systemUser.getUserId(), province);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("首页获取今日推荐error",ex);
            throw new CmsServiceException("首页获取今日推荐失败");
        }
    }

}

