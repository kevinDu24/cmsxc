package cn.com.leadu.cmsxc.pojo.system.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/2/5.
 *
 * 授权操作画面端用vo
 */
@Data
public class AuthorizationOperateVo {
    private String authorizationId;//授权id
    private String remark;// 授权状态
    private String userId;// 用户id
    private String plate;// 车牌号
    private Date delayDate;// 延期天数

}
