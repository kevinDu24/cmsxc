package cn.com.leadu.cmsxc.common.entity;

import lombok.Data;

/**
 * @author qiaomengnan
 * @ClassName: BaseUser
 * @Description: user基类 用于 oauth2 认证使用
 * @date 2018/1/7
 */
@Data
public class BaseUser extends BaseEntity {

    private String username;

    private String password;

}
