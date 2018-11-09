package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/1/17.
 *
 * 金融机构推送收车app信息
 */
@Data
public class AppInfoPushVo {

    private String LeaseCompanyUserName;// 委托公司用户名

    private String androidUrl;// 安卓下载链接

    private String iosUrl;// appStore跳转地址

    public AppInfoPushVo(){}
}
