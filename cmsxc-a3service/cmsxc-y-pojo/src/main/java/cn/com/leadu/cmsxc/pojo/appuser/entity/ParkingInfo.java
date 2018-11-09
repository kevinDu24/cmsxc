package cn.com.leadu.cmsxc.pojo.appuser.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by yuanzhenxia on 2018/4/13.
 *
 * 停车场信息表
 */
@Data
public class ParkingInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;
    private String parkingManager;// 库管用户名
    private String parkingName;// 停车场名称
    private String parkingAdminId;// 停车场公司管理员用户名
    private String state;// 状态 0：金庸、1：启用
    private String address;// 中文地址
    private String province;// 省份
    private String city;// 城市
    private String lat;// 纬度
    private String lon;// 经度
    private String openTime;// 营业开始时间(08:30)
    private String closeTime;// 营业结束时间(22:00)
    private String size;// 长宽高，用","隔开
    private String type;// 停车场类型，1:室内、2:室外、3:地库
    private String plateCarFlag;// 板车是否可入 0:不可入、1:可入
    private String truckFlag;// 货车是否可入 0:不可入、1:可入
    private String maxPlaceNum;// 最大可容纳车位数量

}
