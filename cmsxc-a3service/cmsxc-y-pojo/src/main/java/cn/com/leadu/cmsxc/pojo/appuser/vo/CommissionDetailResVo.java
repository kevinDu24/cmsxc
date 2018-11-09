package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/8/9.
 *
 * 佣金详情结果集
 */
@Data
public class CommissionDetailResVo {
    private String message; // 消息
    private String code;// 代码
    private List<CommissionDetailVo> data;// 佣金详情
}
