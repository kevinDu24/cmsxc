package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/2/28.
 *
 * 用户意见反馈APP传参用vo
 */
@Data
public class FeedbackVo {
    private String feedBackContent;// 意见反馈内容
    private String connectWay;// 联系方式
}
