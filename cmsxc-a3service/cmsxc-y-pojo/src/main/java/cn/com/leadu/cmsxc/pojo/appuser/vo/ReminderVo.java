package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/4/17.
 *
 * 催单传参用
 */
@Data
public class ReminderVo {

   private String groupId; // 分组id
    private String plate; // 车牌号
    private String reminderContent;// 催单内容
    private String authorizationUserId;// 获取授权的人员id
}
