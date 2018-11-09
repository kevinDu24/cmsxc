package cn.com.leadu.cmsxc.pojo.appbusiness.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/1/17.
 *
 * 工单查询历史表
 */
@Data
public class VehicleTaskSearchHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private Long taskId;// 工单id

    private String userId;// 用户id

    private String plate;// 车牌号

    private Date searchTime;// 查询时间

}
