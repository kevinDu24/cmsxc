package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/3/6.
 *
 * 用户信息返回用vo
 */
@Data
public class UserInfoVo {
    private String userRole;// 用户角色
    private String userRoleCode;// 用户角色code
    private String recoveryCompanyName;// 收车公司名字
    private String userPhoto;// 用户头像
    private String leaseCompanyName;// 所属委托公司名字
    private String groupLeaderFlag;// 是否是组长
}
