package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/4/12.
 *
 * 搜索业务员返回用vo
 */
@Data
public class SearchSalesmanVo {
    private String userId;// 用户id
    private String userName;// 用户姓名
    private String groupName;// 分组名称
    private String leaderFlag;// 是否为组长flag 0：不是，1：是
    private String photoUrl;// 用户头像url
}
