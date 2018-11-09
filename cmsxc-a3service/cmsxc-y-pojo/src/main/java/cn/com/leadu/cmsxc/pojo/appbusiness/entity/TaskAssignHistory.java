package cn.com.leadu.cmsxc.pojo.appbusiness.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by yuanzhenxia on 2018/2/12.
 *
 * 任务分配日志表
 */
@Data
public class TaskAssignHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private String groupId;// 小组id

    private String groupName;// 组名称

    private Long taskId;// 工单id

    private String recoveryCompanyId;// 收车公司表id

    private String operateContent;// 操作内容



}
