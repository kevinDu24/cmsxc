package cn.com.leadu.cmsxc.appuser.validator.sysuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/1/23.
 *
 * 修改密码用vo
 */
@Data
public class ModifyPasswordVo {
    private String userPhone;// 手机号
    private String oldPassword;// 旧密码
    private String newPassword;// 新密码
    private String verficationCode;// 验证码
}
