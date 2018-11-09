package cn.com.leadu.cmsxc.pojo.system.vo;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import lombok.Data;

import java.util.List;

/**
 * @author qiaomengnan
 * @ClassName: SysRoleVo
 * @Description: 角色信息载体
 * @date 2018/1/12
 */
@Data
public class SysRoleDeleteVo extends PageQuery {

    private List<String> rowsIds;

}
