package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/4/10.
 *
 * 所有未分组成员信息
 */
@Data
public class UserListVo {
    private String userId;// 用户id
    private String userName;// 用户名
    private String photoUrl;// 用户头像url
}
