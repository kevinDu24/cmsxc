package cn.com.leadu.cmsxc.pojo.appuser.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTask;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 审批授权邮件发送记录表
 */
@Data
public class AuthMailRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private String name;// 姓名

    private String plate;// 车牌号

    private String vehicleIdentifyNum;// 车架号

    private String vehicleType;// 车型

    private String vehicleColor;// 车辆颜色

    private String leaseCompanyName;// 委托公司名称

    private String url;//授权书url

    private String email;//收车公司邮箱

    private String sendStatus; //发送状态 0：未发送，1：已发送

    private Integer retryTimes; //重试次数

    private Date finishDate;//失效时间

    private String mailType;// 邮件类型 0：授权发送邮件  1：任务取消发送邮件  2:新增工单发送邮件

    public AuthMailRecord(){

    }

    public AuthMailRecord(VehicleTask task) {
        this.name = task.getName();
        this.plate = task.getPlate();
        this.vehicleIdentifyNum = task.getVehicleIdentifyNum();
        this.vehicleType = task.getVehicleType();
        this.vehicleColor = task.getVehicleColor();
        this.leaseCompanyName = task.getLeaseCompanyName();
    }
}
