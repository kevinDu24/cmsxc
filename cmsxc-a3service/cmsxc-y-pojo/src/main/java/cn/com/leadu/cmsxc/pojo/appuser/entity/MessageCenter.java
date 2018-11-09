package cn.com.leadu.cmsxc.pojo.appuser.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 消息中心表
 */
@Data
public class MessageCenter extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private String type;//[消息类型] "0":系统消息、"1":任务消息、"2":授权消息

    private String tag;//"[消息标签] 11":线索、"12":记录、"13":派单、"14":催单

    private String content;//内容

    private String activity; //触发动作

    private String receiver; //接收人

    private String isReaded; //是否已读 "0":未读、"1":已读

    private Date pushDate; //推送时间

    private Date readDate; //阅读时间

    private String deleteFlag; //逻辑删除 "0":未删除、"1":已删除

}
