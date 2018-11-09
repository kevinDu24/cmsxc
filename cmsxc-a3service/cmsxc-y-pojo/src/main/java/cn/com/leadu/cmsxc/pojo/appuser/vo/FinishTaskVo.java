package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/2/28.
 *
 * 获取首页滚播消息用vo
 */
@Data
public class FinishTaskVo {
    private String plate;// 车牌号
    private String serviceFee;// 未读消息数量
}
