package cn.com.leadu.cmsxc.pojo.appuser.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 我的线索信息
 */
@Data
public class ClueInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private String userId;// 用户手机号

    private String plate;// 车牌号

    private String vehicleIdentifyNum;// 车架号

    private String type;// 扫描类型，0:车牌号、1:车架号

    private String appAddr;// 手机定位地址

    private String appLon;// 手机定位经度

    private String appLat;// 手机定位纬度

    private String photoUrl;// 线索照片保存url

    private String targetFlag;// 命中flag (0:未命中；1：命中)

    private Date uploadDate;// 上传时间

    private String checkFlag;// 是否查看过flag （0：未查看；1：已查看）
}
