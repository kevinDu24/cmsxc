package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/23.
 *
 * 数据统计传参用vo
 */
@Data
public class DataStatisticsParamVo {
    private Date selectDate;// 画面选中时间
    private List<String> users;// 收车公司所有用户

}
