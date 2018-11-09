package cn.com.leadu.cmsxc.appuser.validator.sysuser.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/1/23.
 *
 * 积分详情
 */
@Data
public class ScoreDetailVo {
    private Date scoreTime;// 积分时间
    private String scoreOperateType;// 积分操作类型
    private Integer scoreValue;// 积分值
}
