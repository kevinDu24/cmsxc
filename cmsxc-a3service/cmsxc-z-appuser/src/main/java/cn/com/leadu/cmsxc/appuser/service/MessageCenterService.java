package cn.com.leadu.cmsxc.appuser.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import org.springframework.http.ResponseEntity;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 消息中心Service
 */
public interface MessageCenterService {

    /**
     * 分页返回消息列表
     * @param userId 用户id
     * @param type 类型 "0":系统消息、"1":任务消息、"2":授权消息
     * @return
     */
    ResponseEntity<RestResponse> getMessageList(String userId, String type, int page, int size);

    /**
     * 消息列表首页获取各个类型消息有无未读消息
     * @param userId 用户id
     * @return
     */
    public ResponseEntity<RestResponse> getMessagePrompt(String userId);

    /**
     * 删除消息
     * @param id 消息id
     * @return
     */
    public ResponseEntity<RestResponse> deleteMessage(String id);


}
