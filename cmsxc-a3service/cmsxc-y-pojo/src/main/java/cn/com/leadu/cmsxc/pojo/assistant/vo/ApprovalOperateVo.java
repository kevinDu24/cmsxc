package cn.com.leadu.cmsxc.pojo.assistant.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/2/5.
 *
 * 授权审批操作画面端用vo
 */
@Data
public class ApprovalOperateVo {
    private String authorizationId;//授权id
    private String remark;// 审批备注
    private String userId;// 申请人用户id
    private String plate;// 车牌号
    private String authEndDate;// 授权截止日期 yyyyMMdd

}
