package cn.com.leadu.cmsxc.pojo.assistant.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/1/30.
 *
 * 角色列表返回用vo
 */
@Data
public class UserRoleListVo {
    private String id; //用户表主键
    private String userId; //用户名
    private String userName; //用户姓名
    private String userRole; //用户角色
    private String enableFlag; //禁用/启用标识
    private String userPhoto; //头像

}
