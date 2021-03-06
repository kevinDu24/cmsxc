package cn.com.leadu.cmsxc.system.validator.sysrole.vo;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import cn.com.leadu.cmsxc.pojo.system.entity.SysRole;
import cn.com.leadu.cmsxc.pojo.system.vo.SysResourceVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author qiaomengnan
 * @ClassName: SysUserModifyVo
 * @Description:
 * @date 2018/1/11
 */
@Data
public class SysRoleModifyVo  extends PageQuery {

    @NotNull(message = "角色id不能为空")
    private String id;

    @NotNull(message = "角色名称不能为空")
    private String roleName;

    private List<SysResourceVo> resources;

    public SysRole getSysRole(){
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(this,sysRole);
        return sysRole;
    }

}
