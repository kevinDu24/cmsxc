package cn.com.leadu.cmsxc.system.validator.sysuser.vo;

import cn.com.leadu.cmsxc.pojo.system.entity.SysUser;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;

/**
 * @author qiaomengnan
 * @ClassName: SysUserModifyVo
 * @Description:
 * @date 2018/1/11
 */
@Data
public class SysUserModifyVo {

    @NotNull(message = "用户id不能为空")
    private String id;

    @NotNull(message = "用户账号不能为空")
    private String username;

    @NotNull(message = "用户手机号不能为空")
    private String phone;

    @NotNull(message = "用户姓名不能为空")
    private String realName;

    public SysUser getSysUser(){
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(this,sysUser);
        return sysUser;
    }
}
