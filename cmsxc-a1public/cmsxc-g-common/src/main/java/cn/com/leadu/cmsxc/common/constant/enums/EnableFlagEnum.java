package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * 禁用/启用flag枚举类
 *
 * @author qiaomengnan
 * @ClassName: TestStationEnum
 * @Description: 枚举 常量
 * @date 2018/1/7
 */
public enum EnableFlagEnum {

    OFF("0", "禁用"),
    ON("1","启用");

    private String code;
    private String name;

    EnableFlagEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static EnableFlagEnum getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (EnableFlagEnum e : EnableFlagEnum.values()) {
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