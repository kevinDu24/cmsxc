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
 * 新闻表
 */
@Data
public class HomePageInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private String imgUrl;// 封面

    private String title;// 标题

    private String content;// 内容

    private Integer readCount;// 阅读次数

    private Date createTime; // 发布时间

    private String type; // "1":新闻(bunner图)、"2":首页广告、"3":用户协议、"4":常见问题、"5":关于我们

}
