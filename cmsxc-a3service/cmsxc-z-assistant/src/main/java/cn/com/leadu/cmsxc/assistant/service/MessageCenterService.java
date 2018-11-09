package cn.com.leadu.cmsxc.assistant.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import org.springframework.http.ResponseEntity;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 消息中心Service
 */
public interface MessageCenterService {

    /**
     * 查询未读消息数量
     * @param userId 用户id
     * @return
     */
    public ResponseEntity<RestResponse> getMessageCount(String userId);

    /**
     * 分页返回消息列表
     * @param userId 用户id
     * @return
     */
    ResponseEntity<RestResponse> getMessageList(String userId, int page, int size);

    /**
     * 删除消息
     * @param id 消息id
     * @return
     */
    public ResponseEntity<RestResponse> deleteMessage(String id);


}
