package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * 消息中心表消息类型枚举类
 *
 * @author qiaomengnan
 * @ClassName: TestStationEnum
 * @Description: 枚举 常量
 * @date 2018/1/7
 */
public enum MessageTypeEnum {

    SYSTEM("0", "系统消息"),
    TASK("1","任务消息"),
    AUTH("2","授权消息");

    private String code;
    private String name;

    MessageTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static MessageTypeEnum getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (MessageTypeEnum e : MessageTypeEnum.values()) {
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