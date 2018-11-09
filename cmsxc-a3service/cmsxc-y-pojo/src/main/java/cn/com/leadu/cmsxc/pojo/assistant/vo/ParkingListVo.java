package cn.com.leadu.cmsxc.pojo.assistant.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/4/13.
 *
 * 停车场列表、筛选返回用vo
 */
@Data
public class ParkingListVo {
    private String id; //停车场id
    private String parkingName;// 停车场名称
    private String parkingManager;// 库管用户名
    private String parkingManagerName;// 库管姓名
    private String state;// 账号状态 0:禁用、1:正常
    private String address;// 停车场位置
    private String parkingCompanyName;// 停车场公司名称
}
