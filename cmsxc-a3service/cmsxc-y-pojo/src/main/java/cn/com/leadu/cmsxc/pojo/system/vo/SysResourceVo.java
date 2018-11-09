package cn.com.leadu.cmsxc.pojo.system.vo;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qiaomengnan
 * @ClassName: SysResourceVo
 * @Description: 菜单信息载体
 * @date 2018/1/12
 */
@Data
public class SysResourceVo extends PageQuery {

    private String id;

    private String name;

    private String res;

    private String description;

    private Integer sort;

    private String icon;

    private String parentId;

    private Integer type;

    private List<SysResourceVo> children = new ArrayList<>();

}
