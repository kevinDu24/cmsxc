package cn.com.leadu.cmsxc.pojo.appuser.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/1/15.
 *
 * 用户积分流水表
 */
@Data
public class SystemUserScore extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private String userId;  //登录手机号

    private String scoreCode;// 积分code

    private Integer scoreValue;// 积分值

    private String remark;// 备注

    private Date scoreTime; // 积分时间


}
