package cn.com.leadu.cmsxc.oauth2.entity;

import lombok.Data;

/**
 * @author qiaomengnan
 * @ClassName: SysUser
 * @Description: 系统用户
 * @date 2018/1/7 下午5:33
 */
@Data
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;

    private String username;

    private String password;
//
//    @Transient
//    private List<SysRole> roles;

}
