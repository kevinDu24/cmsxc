package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * 授权延期枚举类
 *
 * @author qiaomengnan
 * @ClassName: TestStationEnum
 * @Description: 枚举 常量
 * @date 2018/1/7
 */
public enum AuthDelayEnum {

    NO_DELAY("0", "未延期"),
    DELAY("1","延期");

    private String code;
    private String name;

    AuthDelayEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static AuthDelayEnum getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (AuthDelayEnum e : AuthDelayEnum.values()) {
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