package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/3/7.
 *
 * 我的派单详情传参用vo
 */
@Data
public class RecoveryDetailVo {
    private String taskId;// 任务id
    private String plate;// 车牌号
    private String authorizationId;// 授权id
}
