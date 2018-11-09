package cn.com.leadu.cmsxc.pojo.appuser.entity;

import cn.com.leadu.cmsxc.common.entity.Entity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 审核人员片区划分表
 * @author qiaomengnan
 * @ClassName: RecoveryLeaseRelation
 * @Description:
 * @date 2018/1/7
 */
@Data
public class AuditorArea implements Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private String userId;// 用户名

    private String province;// 省份

}
