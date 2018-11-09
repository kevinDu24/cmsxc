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
 * 收车公司派单任务
 */
@Data
public class VehicleTaskRecovery extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private Long taskId;// 任务id

    private String recoveryCompanyId;// 收车公司id

    private Date startTime;// 发起时间

    private Date failureTime;// 失效时间

    private String status;// 状态

    private String authorizationPaperUrl;// 授权书路径

    private String assignmentFlag;// 是否分配 "0":未分配、"1":已分配

    private String authGroupId;// 获得该任务授权的小组id

    private String finishGroupId;// 完成该任务的小组id

    private Date assignDate;//分配日期

    private String addition1;// 附加字段1

    private String addition2;// 附加字段2

    private String addition3;// 附加字段3
}
