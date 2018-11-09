package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/8/7.
 *
 * 计算公里数传参用vo
 */
@Data
public class KilometresParamVo {

    private String taskId;// 工单id
    private String startLon;// 起点经度
    private String startLat;// 起点纬度

}
