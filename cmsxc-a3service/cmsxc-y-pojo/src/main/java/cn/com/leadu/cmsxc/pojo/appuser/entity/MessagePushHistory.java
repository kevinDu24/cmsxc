package cn.com.leadu.cmsxc.pojo.appuser.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 消息推送日志表
 */
@Data
public class MessagePushHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private String deviceTokens;//设备码集合

    private String client; //客户端类型 "0":安卓 "1":安卓

    private String content;//内容

    private Integer resultCode;//友盟返回消息码 //非异常情况返回什么code就设定什么，异常情况设定为-1

    private String status;// 推送是否成功 "0":失败、"1":成功


}
