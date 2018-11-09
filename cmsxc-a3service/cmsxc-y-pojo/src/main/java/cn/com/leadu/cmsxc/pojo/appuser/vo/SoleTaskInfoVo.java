package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/2/24.
 *
 * 独家任务线索信息vo
 */
@Data
public class SoleTaskInfoVo {
    private Long taskId;// 工单id
    private String recoveryTaskId;// 收车任务表id
    private String plate; // 车牌号
    private String userId; //任务工单对应的委托公司用户名
 }
