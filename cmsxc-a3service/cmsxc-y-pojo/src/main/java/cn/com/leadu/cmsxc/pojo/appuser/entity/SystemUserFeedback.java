package cn.com.leadu.cmsxc.pojo.appuser.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by yuanzhenxia on 2018/2/28.
 *
 * 用户意见反馈表
 */
@Data
public class SystemUserFeedback extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private String userId;  //登录手机号

    private String feedbackContent;// 意见反馈内容

    private String connectWay;// 联系方式

    private String addition1;// 附加字段1

    private String addition2;// 附加字段2

    private String addition3; // 附加字段3


}
