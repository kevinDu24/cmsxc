package cn.com.leadu.cmsxc.pojo.appuser.entity;

import cn.com.leadu.cmsxc.common.entity.Entity;
import lombok.Data;

import javax.persistence.Id;

/**
 * 停车场公司与委托公司关系表
 * @author qiaomengnan
 * @Description:
 * @date 2018/1/7
 */
@Data
public class ParkingLeaseRelation implements Entity {

    @Id
    private String parkingAdminId; //停车场公司管理员id

    @Id
    private String leaseId; //委托公司id

}
