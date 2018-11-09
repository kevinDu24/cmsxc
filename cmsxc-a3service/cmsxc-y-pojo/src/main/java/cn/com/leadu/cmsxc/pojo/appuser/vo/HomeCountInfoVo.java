package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/2/28.
 *
 * 获取首页数量用vo
 */
@Data
public class HomeCountInfoVo {
    private int taskCount;// 派单管理数量
    private int messageCount;// 未读消息数量
}
