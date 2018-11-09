package cn.com.leadu.cmsxc.pojo.system.vo;

import lombok.Data;

/**
 * @author qiaomengnan
 * @ClassName: SysUserVo
 * @Description: 用户保存时载体及验证
 * @date 2018/1/9
 */
@Data
public class SysUserSaveVo {

    private String userId;

    private String userPassword;

    private String userPhone;

    private String userName;

    private String userRole;

    private String roleName;

    private String leaseId;
}
