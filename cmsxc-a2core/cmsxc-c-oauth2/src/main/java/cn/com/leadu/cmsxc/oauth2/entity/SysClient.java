package cn.com.leadu.cmsxc.oauth2.entity;

import lombok.Data;

import javax.persistence.Id;

/**
 * @author qiaomengnan
 * @ClassName: SysClient
 * @Description:
 * @date 2018/1/7
 */
@Data
public class SysClient extends BaseEntity {

    @Id
    private Integer id;

    private String clientId;//client标识

    private String clientSecret;//密钥

    private String scopes;

    private String auth_types;

    private String roles;

}
