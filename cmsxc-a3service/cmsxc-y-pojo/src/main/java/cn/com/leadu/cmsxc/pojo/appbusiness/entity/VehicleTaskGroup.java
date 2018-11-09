package cn.com.leadu.cmsxc.pojo.appbusiness.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/2/12.
 *
 * 小组任务表
 */
@Data
public class VehicleTaskGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private String vehicleTaskRecoveryId;// 收车公司任务表主键

    private String groupId;// 分组表主键

    private Date assignmentTime;// 分配时间

}
