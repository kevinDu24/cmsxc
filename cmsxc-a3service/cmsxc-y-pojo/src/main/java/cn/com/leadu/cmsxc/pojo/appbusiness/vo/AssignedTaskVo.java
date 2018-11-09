package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/11.
 *
 * 任务分配vo
 */
@Data
public class AssignedTaskVo {
    private String taskId;// 工单id
    private List<RecoveryGroupVo> groupIds;// 小组集合
}
