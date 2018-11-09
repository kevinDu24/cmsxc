package cn.com.leadu.cmsxc.appuser.controller;

import cn.com.leadu.cmsxc.appuser.service.DataStatisticsService;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appuser.vo.DataStatisticsParamVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by yuanzhenxia on 2018/4/23.
 *
 * 数据统计controller
 */
@RestController
@RequestMapping("datastatistics")
public class DataStatisticsController {
    private static final Logger logger = LoggerFactory.getLogger(DataStatisticsController.class);
    @Autowired
    private DataStatisticsService dataStatisticsService;
    /**
     * 根据收车公司id获取收车公司所有小组
     *
     * @param systemUser 当前用户
     * @return
     */
    @RequestMapping(value = "/getLeaseCompanys", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getLeaseCompanys(@AuthUserInfo SystemUser systemUser) {
        try{
            return dataStatisticsService.getLeaseCompanys(systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("根据收车公司id获取收车公司所有小组error",ex);
            throw new CmsServiceException("根据收车公司id获取收车公司所有小组失败");
        }
    }
    /**
     * 统计信息
     *
     * @param systemUser 当前用户
     * @return
     */
    @RequestMapping(value = "/getDataStatistics", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> getDataStatistics(@AuthUserInfo SystemUser systemUser, @RequestBody DataStatisticsParamVo dataStatisticsParamVo) {
        try{
            return dataStatisticsService.getDataStatistics(systemUser,dataStatisticsParamVo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("统计信息error",ex);
            throw new CmsServiceException("统计信息失败");
        }
    }
}
