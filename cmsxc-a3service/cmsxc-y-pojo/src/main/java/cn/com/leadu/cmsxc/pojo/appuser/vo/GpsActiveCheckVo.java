package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/2/24.
 *
 * 贴gps时check客户输入的车架号后六位对应信息Vo
 */
@Data
public class GpsActiveCheckVo {
    private String vehicleIdentifyNum; //车架号
    private String plate; //车牌号
    private String taskStatus; //工单状态
 }
