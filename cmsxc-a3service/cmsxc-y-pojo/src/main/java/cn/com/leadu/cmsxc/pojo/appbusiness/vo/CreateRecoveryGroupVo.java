package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/4/11.
 *
 * 添加小组传参用vo
 */
@Data
public class CreateRecoveryGroupVo {
    private String groupName;// 分组名
    private String groupLeaderId;// 组长用户id
}
