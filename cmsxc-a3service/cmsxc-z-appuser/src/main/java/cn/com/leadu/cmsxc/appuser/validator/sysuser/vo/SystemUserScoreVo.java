package cn.com.leadu.cmsxc.appuser.validator.sysuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/1/23.
 *
 * 赠送积分传参用
 */
@Data
public class SystemUserScoreVo {
    private String scoreValue;// 积分值
    private String scoreAcceptUserId;// 受领账号
}
