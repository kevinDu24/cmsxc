package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/3/6.
 *
 * 我的派单返回用vo
 */
@Data
public class TaskManagerCountVo {

    private int assignedCount;// 已处理数量

    private int unAssignedCount;// 未处理数量

}
