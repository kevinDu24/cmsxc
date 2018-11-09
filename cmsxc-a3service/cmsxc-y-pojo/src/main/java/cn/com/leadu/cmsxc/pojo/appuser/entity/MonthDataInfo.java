package cn.com.leadu.cmsxc.pojo.appuser.entity;

import cn.com.leadu.cmsxc.common.entity.Entity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 月度数据统计表
 */
@Data
public class MonthDataInfo implements Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private Long taskId;// 工单id

    private String recoveryCompanyId;// 收车公司id

    private String recoveryCompanyName;// 收车公司名称

    private String groupId;// 小组id

    private String groupName; // 小组名称

    private Double serviceFee; // 服务费

    private Date finishDate; //完成时间

    private Date createDate;// 创建时间

}
