package cn.com.leadu.cmsxc.assistant.service.impl;

import cn.com.leadu.cmsxc.assistant.service.MessageCenterService;
import cn.com.leadu.cmsxc.common.constant.enums.DeleteFlagEnum;
import cn.com.leadu.cmsxc.common.constant.enums.MessageReadEnum;
import cn.com.leadu.cmsxc.data.appuser.repository.MessageCenterRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appuser.entity.MessageCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 消息中心Service实现类
 */
@Service
public class MessageCenterServiceImpl implements MessageCenterService {
    private static final Logger logger = LoggerFactory.getLogger(MessageCenterServiceImpl.class);
    @Autowired
    private MessageCenterRepository messageCenterRepository;

    /**
     * 查询未读消息数量
     * @param userId 用户id
     * @return
     */
    public ResponseEntity<RestResponse> getMessageCount(String userId){
        Example example = new Example(MessageCenter.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("receiver", userId);
        criteria.andEqualTo("deleteFlag", DeleteFlagEnum.ON.getCode());
        criteria.andEqualTo("isReaded", MessageReadEnum.NO_READ.getCode());
        List<MessageCenter> messageCenters = messageCenterRepository.selectByExampleList(example);
        //如果没有查到数据，则无未读消息
        if(messageCenters == null || messageCenters.isEmpty()) {
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, 0, ""),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, messageCenters.size(),""),
                HttpStatus.OK);
    }

    /**
     * 分页返回消息列表，并更新制定消息为已读
     * @param userId 用户id
     * @return
     */
    public ResponseEntity<RestResponse> getMessageList(String userId, int page, int size){
        // 获取新闻信息
        List<MessageCenter> messageList =  messageCenterRepository.selectMessageList(userId,null,page,size);
        if(messageList == null || messageList.isEmpty()){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","数据加载完毕"),
                  HttpStatus.OK);
        }
        //查询列表时同时更新该类型下所有消息为已读
        messageCenterRepository.updateMessageList(userId, null);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, messageList,""),
                HttpStatus.OK);
    }

    /**
     * 删除消息
     * @param id 消息id
     * @return
     */
    public ResponseEntity<RestResponse> deleteMessage(String id){
        MessageCenter message = messageCenterRepository.selectByPrimaryKey(id);
        if(message == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, "","消息删除成功"),
                    HttpStatus.OK);
        }
        message.setDeleteFlag(DeleteFlagEnum.OFF.getCode());
        messageCenterRepository.updateByPrimaryKey(message);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, "","消息删除成功"),
                HttpStatus.OK);
    }
}
