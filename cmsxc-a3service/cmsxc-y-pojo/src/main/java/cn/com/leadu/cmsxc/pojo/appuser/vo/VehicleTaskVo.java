package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 接口传参用
 */
@Data
public class VehicleTaskVo {

    private String hasGpsFlag;// gps有无

    private String volitionFlag;// 违章有无

    private String clueFlag;// 线索有无

    private String priceRange;// 价格区间

    private int page;// 当前页

    private int size;// 每页数目

    private Date nowDate;// 当前时间

    private String status;// 状态

    private String groupId;// 分组id

    private String plate;// 车牌号

    private List<String> vehicleProvinces;// 车辆省份
    private List<String> list = new ArrayList<>();
    private List<String> users = new ArrayList<>();

}
