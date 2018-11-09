package cn.com.leadu.cmsxc.assistant.controller;

import cn.com.leadu.cmsxc.assistant.service.MessageCenterService;
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
 *消息中心Controller
 */
@RestController
@RequestMapping("messageCenter")
public class MessageCenterController {
    private static final Logger logger = LoggerFactory.getLogger(MessageCenterController.class);
    @Autowired
    private MessageCenterService messageCenterService;

    /**
     * 查询未读消息数量
     * @param systemUser 用户信息
     * @size size 每页个数
     * @return
     */
    @RequestMapping(value = "/getMessageCount", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getMessageCount(@AuthUserInfo SystemUser systemUser) {
        try{
            return messageCenterService.getMessageCount(systemUser.getUserId());
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("分页返回消息列表error",ex);
            throw new CmsServiceException("分页返回消息列表失败");
        }
    }

    /**
     * 分页返回消息列表
     * @param systemUser 用户信息
     * @param page 当前页
     * @size size 每页个数
     * @return
     */
    @RequestMapping(value = "/getMessageList", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getInformation(@AuthUserInfo SystemUser systemUser, int page, int size) {
        try{
            return messageCenterService.getMessageList(systemUser.getUserId(),page,size);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("分页返回消息列表error",ex);
            throw new CmsServiceException("分页返回消息列表失败");
        }
    }

    /**
     * 删除消息
     * @param id 消息id
     * @return
     */
    @RequestMapping(value = "/deleteMessage", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> deleteMessage(String id) {
        try{
            return messageCenterService.deleteMessage(id);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("删除消息error",ex);
            throw new CmsServiceException("删除消息失败");
        }
    }
}
