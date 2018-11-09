package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 轻松查车接口访问的header信息
 */
@Data
public class QsccHeaderInfoVo {

    private String userInfo;// 账号密码base64编译信息

    private String channelId;// 手机channelId

    private String deviceType; // 安卓：3、苹果：4

    private String uniqueMark;// 接口访问凭证

}
