package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * 消息中心表消息是否已读枚举类
 *
 * @author qiaomengnan
 * @ClassName: TestStationEnum
 * @Description: 枚举 常量
 * @date 2018/1/7
 */
public enum MessageReadEnum {

    NO_READ("0", "未读"),
    READED("1","已读");

    private String code;
    private String name;

    MessageReadEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static MessageReadEnum getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (MessageReadEnum e : MessageReadEnum.values()) {
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