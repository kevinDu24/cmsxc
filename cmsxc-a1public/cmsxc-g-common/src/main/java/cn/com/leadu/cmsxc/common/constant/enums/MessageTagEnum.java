package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * 消息中心表消息标签枚举类
 *
 * @author qiaomengnan
 * @ClassName: TestStationEnum
 * @Description: 枚举 常量
 * @date 2018/1/7
 */
public enum MessageTagEnum {

    CLUE("11", "线索"),
    RECORD("12","记录"),
    PUSH_TASK("13","派单"),
    URGE_AUTH("14","催单");

    private String code;
    private String name;

    MessageTagEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static MessageTagEnum getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (MessageTagEnum e : MessageTagEnum.values()) {
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