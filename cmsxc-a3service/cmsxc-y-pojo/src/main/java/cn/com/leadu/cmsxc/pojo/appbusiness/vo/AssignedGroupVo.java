package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/11.
 *
 * 获取所有分组及任务分配情况返回用vo
 */
@Data
public class AssignedGroupVo {
    private List<RecoveryGroupVo> allGroups;// 所有小组集合
    private List<String> assignedGroupId;// 分组id
}
