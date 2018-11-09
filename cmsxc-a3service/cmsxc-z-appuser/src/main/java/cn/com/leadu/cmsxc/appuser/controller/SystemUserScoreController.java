package cn.com.leadu.cmsxc.appuser.controller;

import cn.com.leadu.cmsxc.appuser.service.SystemUserScoreService;
import cn.com.leadu.cmsxc.appuser.validator.sysuser.vo.SystemUserScoreVo;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by yuanzhenxia on 2018/1/23.
 *
 * 用户积分Controller
 */
@RestController
@RequestMapping("systemuserscore")
public class SystemUserScoreController {
    private static final Logger logger = LoggerFactory.getLogger(SystemUserScoreController.class);
    @Autowired
    private SystemUserScoreService systemUserScoreService;
    /**
     * 赠送积分
     *
     * @param systemUser  用户信息
     * @param systemUserScoreVo 赠送积分信息
     * @return
     */
    @RequestMapping(value = "/sendScore", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> sendScore(@AuthUserInfo SystemUser systemUser , @RequestBody SystemUserScoreVo systemUserScoreVo) {
        try{
            return systemUserScoreService.sendScore(systemUser.getUserId(), systemUserScoreVo.getScoreValue(), systemUserScoreVo.getScoreAcceptUserId() );
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("赠送积分error",ex);
            throw new CmsServiceException("赠送积分失败");
        }
    }
    /**
     * 积分列表
     *
     * @param systemUser 用户信息
     * @param searchTime 查询时间
     * @return
     */
    @RequestMapping(value = "/scoreList", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> scoreList(@AuthUserInfo SystemUser systemUser , @RequestParam String searchTime,
                                        @RequestParam(required = true) int page, @RequestParam(required = true) int size) {
        try{
            return systemUserScoreService.scoreList(systemUser.getUserId(),searchTime,page,size );
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("查看积分列表error",ex);
            throw new CmsServiceException("查看积分列表失败");
        }
    }
}
