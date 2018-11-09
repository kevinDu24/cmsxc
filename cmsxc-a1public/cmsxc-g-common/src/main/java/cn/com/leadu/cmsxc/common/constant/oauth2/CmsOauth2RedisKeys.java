package cn.com.leadu.cmsxc.common.constant.oauth2;

/**
 * Created by qiaohao on 2017/11/16.
 */
public enum CmsOauth2RedisKeys {

    CMS_MESSAGE_SMS_AUTHTOKEN("cmsxc:message:sms:authtoken:","用户短信redis前缀");

    private String prefix;
    private String desc;

    CmsOauth2RedisKeys(String prefix, String desc) {
        this.prefix = prefix;
        this.desc = desc;
    }

    public String getPrefix() {
        return prefix;
    }
}
