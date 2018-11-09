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
 * GPS激活日志表
 */
@Data
public class GpsActiveHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private String simCode;// 设备号

    private String vehicleIdentifyNum;// 车架号

    private String plate;// 车牌号

    private String result;// 激活是否成功 "0":失败,"1":成功

    private String gpsSystemUserName;// gps系统账号

    private String gpsSystemUserPassword;// gps系统密码

    private Date operateTime; //操作时间

}
