package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/4/23.
 *
 * 数据统计
 */
@Data
public class DataStatisticsSubVo {
    private String groupName;// 小组名称
    private Long sumCount;// 完成总量
    private Double sumFee; // 总服务费
    private String proportion;// 占比

    public DataStatisticsSubVo(){
        this.sumCount = 0L;
        this.sumFee = 0.0;
    }
}
