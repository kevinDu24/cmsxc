package cn.com.leadu.cmsxc.pojo.appuser.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by yuanzhenxia on 2018/8/7.
 *
 * 扣款项目表
 */
@Data
public class CutPaymentItem extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;// 主键id

    private String commissionSettlementInfoId;// 结算信息表主键

    private String cutPaymentName;// 扣款项目名称

    private Double cutPaymentCount;// 扣款项目金额

}
