package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/8/7.
 *
 * 佣金计算获取公里数结果集
 */
@Data
public class KilometresResVo {
    private String status;// 状态
    private List<KilometresVo> result;// 结果
    private String message;
}
