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
 * 结算信息表
 */
@Data
public class CommissionSettlementInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;// 主键id

    private String taskId;// 工单id

    private String storageInfoId;// 入库信息表id

    private Double flatBedFee;// 板车费用

    private Double flatBedDistance;// 使用板车公里数

    private Double keyFee;// 使用钥匙的费用

    private String status;// 结算状态

    private String applyRemark;// 申请备注

    private String firstAuditingRemark;// 初审备注

    private String lastAuditingRemark;// 终审备注

    private Date applyDate;// 申请时间

    private Date firstAuditingDate;// 初审时间

    private Date lastAuditingDate;// 终审时间

    private String startAddress;// 板车起点

    private String startLon;// 起点经度

    private String startLat;// 起点纬度

    private String recoveryCompanyId;// 收车公司id

    private String userId;// 申请人账号




}
