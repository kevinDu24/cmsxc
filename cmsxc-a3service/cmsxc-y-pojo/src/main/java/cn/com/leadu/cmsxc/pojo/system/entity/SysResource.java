package cn.com.leadu.cmsxc.pojo.system.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author qiaomengnan
 * @ClassName: SysResource
 * @Description:
 * @date 2018/1/7
 */
@Data
public class SysResource extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private String name;

    private String res;

    private String description;

    private Integer sort;

    private String icon;

    private String parentId;

    private Integer type;

}
