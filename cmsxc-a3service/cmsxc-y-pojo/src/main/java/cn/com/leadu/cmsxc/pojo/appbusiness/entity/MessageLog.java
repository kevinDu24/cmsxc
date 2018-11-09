package cn.com.leadu.cmsxc.pojo.appbusiness.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 短信验证码日志
 */
@Data
public class MessageLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private String phone;  //发送手机号

    private Date sendTime; //发送时间

    private String content;; //发送内容

    private String projectName;//项目名称 例如 赏金寻车

    private String serviceName;//业务名称  例如 用户注册

    private String classFunctionName;//类与方法名称 例如 cn.leadu.control.service.impl.类名.方法名
}
