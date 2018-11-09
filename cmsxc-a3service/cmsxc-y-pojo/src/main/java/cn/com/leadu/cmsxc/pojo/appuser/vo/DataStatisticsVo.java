package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/23.
 *
 * 数据统计返回用vo
 */
@Data
public class DataStatisticsVo {
    private Long sumCount;// 完成总量
    private Double sumFee; // 总服务费
    private Long selfSumCount;// 派单自己完成总量
    private Long othersSumCount;// 悬赏任务完成总量
    private List<DataStatisticsSubVo> dataStatisticsSubVo;// 各小组完成情况
    public DataStatisticsVo(){
        this.sumCount = 0L;
        this.sumFee = 0.0;
        this.selfSumCount = 0L;
        this.othersSumCount = 0L;
    }

}
