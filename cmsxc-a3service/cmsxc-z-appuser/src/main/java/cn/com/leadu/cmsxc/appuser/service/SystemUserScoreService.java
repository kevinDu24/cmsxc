package cn.com.leadu.cmsxc.appuser.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import org.springframework.http.ResponseEntity;

/**
 * Created by yuanzhenxia on 2018/1/23.
 *
 * 用户积分Service
 */
public interface SystemUserScoreService {
    /**
     * 赠送积分
     *
     * @param userId 用户id
     * @param scoreValue 积分值
     * @param scoreAcceptUserId 受领用户
     * @return
     */
    ResponseEntity<RestResponse> sendScore(String userId , String scoreValue, String scoreAcceptUserId);
    /**
     * 积分列表
     *
     * @param userId 用户id
     * @param searchTime 查询时间
     * @param page 当前页
     * @param size 每页数目
     * @return
     */
    ResponseEntity<RestResponse> scoreList(String userId , String searchTime, int page, int size);
}
