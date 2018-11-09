package cn.com.leadu.cmsxc.pojo.system.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author qiaomengnan
 * @ClassName: SysUser
 * @Description: 系统用户
 * @date 2018/1/7 下午5:33
 */
@Data
public class SysUser extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private String username;

    private String password;

    private String phone;

    private String realName;

}
