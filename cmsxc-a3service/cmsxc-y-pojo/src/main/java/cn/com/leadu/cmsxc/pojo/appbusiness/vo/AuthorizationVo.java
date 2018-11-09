package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/2/5.
 *
 * 授权列表画面参数用vo
 */
@Data
public class AuthorizationVo extends PageQuery {
    private String authorizationStatus;// 授权状态
    private String applicantName; // 申请人姓名
    private String vehiclePlate; // 车牌号码
    private String vehicleIdentifyNum; // 车架号
    private String userId; // 移动端用户名
    private String applicantPhone; // 申请人手机号
}
