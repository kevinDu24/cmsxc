package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/8/7.
 *
 * 佣金计算获取公里数vo
 */
@Data
public class KilometresVo {
    private DistanceVo distance;// 里程数据
    private DurationVo duration;// 时长数据

}
