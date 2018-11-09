package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import lombok.Data;

/**
 * Created by Yuanzhenxia on 2018/4/10.
 *
 * 所有分组
 */
@Data
public class GroupListVo {
    private String groupId;// 分组id
    private String groupName;// 组名
    private int countNum;// 组员数量
}
