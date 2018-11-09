package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/3/2.
 *
 * 消息中心首页红点提示Vo
 */
@Data
public class MessagePromptVo {
    private String sysMessage; // 系统消息 "0":无提示;"1":"有提示"
    private String taskMessage; // 任务消息 "0":无提示;"1":"有提示"
    private String authMessage;// 授权消息 "0":无提示;"1":"有提示"
}
