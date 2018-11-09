package cn.com.leadu.cmsxc.pojo.appuser.entity;

import cn.com.leadu.cmsxc.common.entity.Entity;
import lombok.Data;

import javax.persistence.Id;

/**
 * 收车公司与委托公司关系表
 * @author qiaomengnan
 * @ClassName: RecoveryLeaseRelation
 * @Description:
 * @date 2018/1/7
 */
@Data
public class RecoveryLeaseRelation implements Entity {

    @Id
    private String recoveryCompanyId; //收车公司id

    @Id
    private String leaseId; //委托公司id

}
