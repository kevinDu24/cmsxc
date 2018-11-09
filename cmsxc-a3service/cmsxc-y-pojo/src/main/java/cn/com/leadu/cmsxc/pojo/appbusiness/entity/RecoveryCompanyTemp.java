package cn.com.leadu.cmsxc.pojo.appbusiness.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 收车公司
 */
@Data
public class RecoveryCompanyTemp extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private String recoveryShortName;// 收车公司简称

    private String recoveryFullName;// 收车公司全称

    private String contactName;// 联系人姓名

    private String contactPhone;// 联系人电话

    private String contactAddress;// 联系人地址

    private String recoveryCompanyId;//主系统收车公司id

    private String contactEmail;// 联系人邮箱


}
