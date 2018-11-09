package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/2/24.
 *
 * 车辆管理系统结果集
 */
@Data
public class GpsActiveResVo {
    private String status; //状态
    private String error; //错误信息
    private GpsActiveDetailResVo data; //gps账号密码结果
 }
