package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * Created by yuanzhenxia on 2018/4/25.
 *
 * 邮件类型
 */
public enum MailTypeEnum {
    AUTHORIZATION("0", "授权发送邮件"),
    CANCEL("1","任务取消发送邮件"),
    PUSH("2","任务推送发送邮件");

    private String code;
    private String name;

    MailTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static MailTypeEnum getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (MailTypeEnum e : MailTypeEnum.values()) {
            if (code.equals(e.getCode())) {
                return e;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }
}
