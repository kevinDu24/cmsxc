package cn.com.leadu.cmsxc.system.validator.sysuser.vo;

import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import cn.com.leadu.cmsxc.system.validator.sysuser.validator.SysUserNameValidator;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;

/**
 * @author qiaomengnan
 * @ClassName: SysUserVo
 * @Description: 用户保存时载体及验证
 * @date 2018/1/9
 */
@Data
public class SysUserSaveVo {

    @NotNull(message = "用户账号不能为空")
    @SysUserNameValidator(message = "用户名已存在")
    private String userId;

    @NotNull(message = "用户密码不能为空")
    private String userPassword;

    @NotNull(message = "用户手机号不能为空")
    private String userPhone;

    @NotNull(message = "用户姓名不能为空")
    private String userName;

    @NotNull(message = "用户角色不能为空")
    private String userRole;

    private String roleName;

    private String leaseId;

    public SystemUser getSysUser(){
        SystemUser systemUser = new SystemUser();
        BeanUtils.copyProperties(this,systemUser);
        return systemUser;
    }
}
