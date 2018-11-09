package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/8/1.
 *
 * 车辆管理系统最新位置信息及常去地信息结果集
 */
@Data
public class LatestPositionAndResortResVo {
    private String status; //状态
    private String error; //错误信息
    private LatestPositionAndResortVo data;// 最新位置信息及常去地信息
}
