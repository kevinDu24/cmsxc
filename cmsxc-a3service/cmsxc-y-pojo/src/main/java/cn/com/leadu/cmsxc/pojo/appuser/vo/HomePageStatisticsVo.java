package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/4/24.
 *
 * 首页数据统计
 */
@Data
public class HomePageStatisticsVo {
    private Long totalCount;// 派单总量
    private Long selfFinishCount;// 自己完成总量
    private Long othersFinishCount;// 他人完成总量
    private Long notFinishCount;// 未完成总量
    private Long cancelCount;// 已取消总量
    private Long revardFinishCount;// 悬赏列表完成总量
    private Double sumServiceFee;// 累计总费用
    private Long sumFinishCount;// 总完成量
    public HomePageStatisticsVo(){
        this.cancelCount = 0L;
        this.totalCount = 0L;
        this.selfFinishCount = 0L;
        this.othersFinishCount = 0L;
        this.notFinishCount = 0L;
        this.revardFinishCount = 0L;
        this.sumFinishCount = 0L;
        this.sumServiceFee = 0.0;
    }
}
