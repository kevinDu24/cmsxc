package cn.com.leadu.cmsxc.pojo.assistant.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/3/7.
 *
 * 审批详情申请账号信息返回用vo
 */
@Data
public class ApplyRecoveryInfoVo {
    private String userId;// 用户id
    private String userName;// 用户姓名
    private String userRole;// 用户角色
    private String recoveryCompanyName;// 收车公司名称
    private String recoveryCompanyPhone;// 收车公司联系方式
}
