package cn.com.leadu.cmsxc.appuser.service.impl;

import cn.com.leadu.cmsxc.appuser.service.MessageCenterService;
import cn.com.leadu.cmsxc.appuser.util.constant.enums.FlagEnums;
import cn.com.leadu.cmsxc.common.constant.enums.DeleteFlagEnum;
import cn.com.leadu.cmsxc.common.constant.enums.MessageReadEnum;
import cn.com.leadu.cmsxc.common.constant.enums.MessageTypeEnum;
import cn.com.leadu.cmsxc.data.appuser.repository.MessageCenterRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appuser.entity.MessageCenter;
import cn.com.leadu.cmsxc.pojo.appuser.vo.MessagePromptVo;
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
     * 分页返回消息列表，并更新制定消息为已读
     * @param userId 用户id
     * @param type 类型 "0":系统消息、"1":任务消息、"2":授权消息
     * @return
     */
    public ResponseEntity<RestResponse> getMessageList(String userId, String type, int page, int size){
        // 获取新闻信息
        List<MessageCenter> messageList =  messageCenterRepository.selectMessageList(userId,type,page,size);
        if(messageList == null || messageList.isEmpty()){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","数据加载完毕"),
                  HttpStatus.OK);
        }
        //查询列表时同时更新该类型下所有消息为已读
        messageCenterRepository.updateMessageList(userId, type);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, messageList,""),
                HttpStatus.OK);
    }

    /**
     * 消息列表首页获取各个类型消息有无未读消息
     * @param userId 用户id
     * @return
     */
    public ResponseEntity<RestResponse> getMessagePrompt(String userId){
        MessagePromptVo vo = new MessagePromptVo();
        vo.setSysMessage(FlagEnums.NO.getType()); //初始化系统消息为无未读消息
        vo.setTaskMessage(FlagEnums.NO.getType());//初始化任务消息为无未读消息
        vo.setAuthMessage(FlagEnums.NO.getType());//初始化授权消息为无未读消息
        // 获取所有未读消息
        Example example = new Example(MessageCenter.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("receiver", userId);
        criteria.andEqualTo("isReaded", MessageReadEnum.NO_READ.getCode());
        criteria.andEqualTo("deleteFlag", DeleteFlagEnum.ON.getCode());
        List<MessageCenter> messageList =  messageCenterRepository.selectByExampleList(example);
        if(messageList == null || messageList.isEmpty()){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, vo,""),
                    HttpStatus.OK);
        }
        //遍历消息
        for(MessageCenter item : messageList){
            // 如果三种类型的消息都为已读消息，跳出循环
            if(FlagEnums.YES.getType().equals(vo.getSysMessage())
                    && FlagEnums.YES.getType().equals(vo.getTaskMessage())
                    && FlagEnums.YES.getType().equals(vo.getAuthMessage())){
                break;
            }
            String messageType = item.getType();
            //如果是任务消息
            if(MessageTypeEnum.TASK.getCode().equals(messageType)){
                vo.setTaskMessage(FlagEnums.YES.getType());
            } else if(MessageTypeEnum.AUTH.getCode().equals(messageType)){ //如果是授权消息
                vo.setAuthMessage(FlagEnums.YES.getType());
            } else if(MessageTypeEnum.SYSTEM.getCode().equals(messageType)){ //如果是系统消息
                vo.setSysMessage(FlagEnums.YES.getType());
            }
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, vo,""),
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
