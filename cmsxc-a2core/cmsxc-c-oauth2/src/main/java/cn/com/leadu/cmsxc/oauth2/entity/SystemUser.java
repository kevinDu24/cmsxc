package cn.com.leadu.cmsxc.oauth2.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by yuanzhenxia on 2018/1/15.
 *
 * 用户表
 */
@Data
public class SystemUser extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private String userId;  //登录用户名

    private String userPassword;  // 登录密码

    private String userName;// 用户姓名

    private String userPhone;// 用户手机

    private String userPhoto;// 用户头像

    private String recoveryCompanyId;// 收车公司id

    private String leaseId;// 金融机构id

    private String userRole;// 用户业务角色

    private Integer totalScore;// 我的积分

    private String enableFlag;// 禁用/启用标识  0:禁用、1：启用

}
