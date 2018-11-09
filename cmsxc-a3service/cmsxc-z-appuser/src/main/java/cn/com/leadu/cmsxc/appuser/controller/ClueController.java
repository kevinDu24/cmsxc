package cn.com.leadu.cmsxc.appuser.controller;

import cn.com.leadu.cmsxc.appuser.service.ClueService;
import cn.com.leadu.cmsxc.appuser.util.constant.enums.ClueCheckFlag;
import cn.com.leadu.cmsxc.appuser.util.constant.enums.TargetFlag;
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
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 我的线索Controller
 */
@RestController
@RequestMapping("clue")
public class ClueController {
    private static final Logger logger = LoggerFactory.getLogger(ClueController.class);
    @Autowired
    private ClueService clueService;
    /**
     * 根据用户id和是否命中标识分页查看线索信息
     *
     * @param systemUser 当前用户
     * @param targetFlag 命中标志
     * @param page 当前页
     * @param size 每页个数
     * @return
     */
    @RequestMapping(value = "/getClueInformation", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getClueInformation(@AuthUserInfo SystemUser systemUser, @RequestParam String targetFlag,
                                                           @RequestParam(required = true) int page, @RequestParam(required = true) int size) {
        try{
            return clueService.getClueInformation(systemUser.getUserId(),targetFlag,page,size);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("根据手机号和是否命中标识查看线索信息error",ex);
            throw new CmsServiceException("查询失败");
        }
    }
    /**
     * 根据用户id查找已命中未查看的线索数量（同一车牌号统计一条）
     *
     * @param systemUser 用户信息
     * @return
     */
    @RequestMapping(value = "/getCountNotCheckCount", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getCountNotCheckCount(@AuthUserInfo SystemUser systemUser ){
        try{
            return clueService.getCountNotCheckCount(systemUser.getUserId(), TargetFlag.TARGET.getType(), ClueCheckFlag.NOTCHECK.getType());
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("查找已命中未查看的线索数量error",ex);
            throw new CmsServiceException("查询失败");
        }
    }
}
