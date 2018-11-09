package cn.com.leadu.cmsxc.pojo.appbusiness.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by yuanzhenxia on 2018/2/2.
 *
 * 委托公司表
 */
@Data
public class LeaseCompany extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private String leaseShortName;// 委托公司简称

    private String leaseFullName;// 委托公司全称

    private String contactName;// 联系人姓名

    private String contactPhone;// 联系人电话

}
