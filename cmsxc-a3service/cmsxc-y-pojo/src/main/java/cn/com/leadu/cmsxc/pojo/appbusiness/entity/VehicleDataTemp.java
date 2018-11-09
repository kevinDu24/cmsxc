package cn.com.leadu.cmsxc.pojo.appbusiness.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 车辆任务工单拆分临时表
 */
@Data
public class VehicleDataTemp extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private String vehicleIdentifyNum;// 车架号

    private String idCard; //身份证号码

    private String phoneNum; //车主手机号码

    private String workAddress; //家庭地址

    private String homeAddress; //家庭地址

    private String lifeAddress; //常住地址

    private String fpProvince; //FP所在省份
}
