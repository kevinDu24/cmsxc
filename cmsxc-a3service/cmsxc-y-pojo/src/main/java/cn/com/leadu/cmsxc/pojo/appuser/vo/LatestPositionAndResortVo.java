package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/8/1.
 *
 *最新位置信息及常去地址vo
 */
@Data
public class LatestPositionAndResortVo {

    private String offLineStatus;// 离线状态
    private String offLineDays;// 离线天数
    private String latestPosition;// 最新位置
    private String resortPosition1;// 常去位置1
    private String resortPosition2;// 常去位置2
    private String resortPosition3;// 常去位置3

}
