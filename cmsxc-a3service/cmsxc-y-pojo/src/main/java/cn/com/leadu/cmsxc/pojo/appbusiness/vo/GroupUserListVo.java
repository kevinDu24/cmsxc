package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/17.
 *
 * 指定小组所有成员以及需要推送的成员集合vo
 */
@Data
public class GroupUserListVo {

    private List<String> groupUsers;// 小组所有成员id集合

    List<GroupUserClientVo> androidUsers;//小组中安卓设备用户id集合

    List<GroupUserClientVo> iosUsers;//小组中ios设备用户id集合

    public GroupUserListVo(){
        this.groupUsers = new ArrayList();
        this.androidUsers = new ArrayList();
        this.iosUsers = new ArrayList();
    }
}
