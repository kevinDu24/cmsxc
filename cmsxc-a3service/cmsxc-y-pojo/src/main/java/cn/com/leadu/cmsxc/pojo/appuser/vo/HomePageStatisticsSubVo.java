package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/4/24.
 *
 * 首页数据统计
 */
@Data
public class HomePageStatisticsSubVo {
    private Long sumCount;// 完成任务量
    private String status;// 状态
}
