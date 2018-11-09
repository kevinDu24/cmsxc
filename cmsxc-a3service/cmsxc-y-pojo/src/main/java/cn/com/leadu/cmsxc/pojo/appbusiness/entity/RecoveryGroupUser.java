package cn.com.leadu.cmsxc.pojo.appbusiness.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by yuanzhenxia on 2018/4/9.
 *
 * 组员表
 */
@Data
public class RecoveryGroupUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private String recoveryGroupId;// 分组表id

    private String groupUserId;// 组员id
}
