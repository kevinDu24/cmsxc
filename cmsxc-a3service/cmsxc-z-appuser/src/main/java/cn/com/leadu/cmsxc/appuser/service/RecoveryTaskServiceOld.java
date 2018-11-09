package cn.com.leadu.cmsxc.appuser.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appuser.vo.RecoveryVo;
import org.springframework.http.ResponseEntity;

/**
 * Created by yuanzhenxia on 2018/2/12.
 *
 * 收车公司派单任务service_一期老接口
 */
public interface RecoveryTaskServiceOld {

    /**
     * 获取收车公司任务派单列表
     *
     * @param recoveryVo 画面参数
     * @param userId 用户id
     * @return
     */
    ResponseEntity<RestResponse> getRecoveryTaskList(String userId, RecoveryVo recoveryVo);
    /**
     * 获取各状态收车公司任务派单数量
     *
     * @param userId 用户id
     * @return
     */
    ResponseEntity<RestResponse> getRecoveryTaskCountByStatus(String userId);

}
