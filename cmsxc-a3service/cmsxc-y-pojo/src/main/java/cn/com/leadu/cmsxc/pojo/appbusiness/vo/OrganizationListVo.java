package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/10.
 *
 * 组织架构初始化列表返回用vo
 */
@Data
public class OrganizationListVo {
    private String recoveryCompanyName;// 收车公司名称
    private List<GroupListVo> groupListVos;
    private List<UserListVo> userListVos;

}
