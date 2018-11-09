package cn.com.leadu.cmsxc.pojo.appuser.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/8/7.
 *
 * 结算流程日志表
 */
@Data
public class CommissionSettlementHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;// 主键id

    private String commissionSettlementInfoId;// 结算信息表主键

    private String operateContent;// 操作内容

    private Date operateDate;//  操作时间

    private String operateUserId;// 操作人账号

    private String operateUserName;// 操作人姓名

    private String remark;// 备注

}
