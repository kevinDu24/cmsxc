package cn.com.leadu.cmsxc.pojo.assistant.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/4/13.
 *
 * 停车场公司列表返回用vo
 */
@Data
public class ParkingCompanyListVo {
    private String userId;// 用户名id
    private String userName;// 用户姓名
    private String enableFlag;// 账号状态 0:禁用、1:正常
    private Long num;// 停车场公司下停车场数量
}
