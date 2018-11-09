package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/1/17.
 *
 * 消息推送载体类vo
 */
@Data
public class GroupUserClientVo {

    private String userId;// 用户名

    private String userPhone;//用户手机号

    private String deviceToken;// 推送用的设备token

    private String client;//客户端 "0":安卓,"1":ios

    public GroupUserClientVo(){}
}
