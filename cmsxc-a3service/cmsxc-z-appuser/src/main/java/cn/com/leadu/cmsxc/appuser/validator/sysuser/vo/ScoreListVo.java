package cn.com.leadu.cmsxc.appuser.validator.sysuser.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/23.
 *
 * 积分列表返回值用vo
 */
@Data
public class ScoreListVo {
    private Integer expendScore;// 支出积分数
    private Integer incomeScore;// 收取积分数
    private Integer totalScore;// 总积分
    private List<ScoreDetailVo> scoreDetails = new ArrayList<>();// 积分详情
}
