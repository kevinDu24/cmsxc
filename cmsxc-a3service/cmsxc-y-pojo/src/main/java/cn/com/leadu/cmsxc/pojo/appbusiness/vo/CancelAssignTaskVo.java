package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/5/7.
 *
 * 取消分配用vo
 */
@Data
public class CancelAssignTaskVo {
    private String taskId;// 任務id
    private String groupId;// 分組id
    private String groupName;// 分組名稱
}
