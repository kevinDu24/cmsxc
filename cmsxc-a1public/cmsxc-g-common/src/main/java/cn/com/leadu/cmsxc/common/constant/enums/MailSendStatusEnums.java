package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * Created by huzongcheng on 2018/3/14.
 *
 * 授权成功邮件发送状态枚举类
 */
public enum MailSendStatusEnums {
    NOT_SEND("0", "未发送"),
    SENTED("1","已发送");


    private String type;
    private String value;

    MailSendStatusEnums(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static MailSendStatusEnums getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (MailSendStatusEnums e : MailSendStatusEnums.values()) {
            if (code.equals(e.getType())) {
                return e;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }
    public String getValue() {
        return value;
    }
}
