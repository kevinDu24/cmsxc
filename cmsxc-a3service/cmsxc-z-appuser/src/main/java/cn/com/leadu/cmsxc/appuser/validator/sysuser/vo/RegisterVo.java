package cn.com.leadu.cmsxc.appuser.validator.sysuser.vo;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by yuanzhenxia on 2018/1/15.
 *
 * 注册传参用vo
 */
@Data
public class RegisterVo extends PageQuery {
    //登录手机号
    private String userId;
    // 登录密码
    private String userPassword;
    // 用户姓名
    private String userName;
    // 收车公司注册码
    private String recoComRegCode;
    // 验证码
    @NotBlank
    private String verficationCode;
}
