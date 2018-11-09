package cn.com.leadu.cmsxc.pojo.system.entity;

import cn.com.leadu.cmsxc.common.entity.Entity;
import lombok.Data;

import javax.persistence.Id;

/**
 * @author qiaomengnan
 * @ClassName: SysRoleResource
 * @Description:
 * @date 2018/1/7
 */
@Data
public class SysRoleResource implements Entity {

    @Id
    private String roleId;

    @Id
    private String resourceId;

}
