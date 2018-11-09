package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * 逻辑删除flag枚举类
 *
 * @author qiaomengnan
 * @ClassName: TestStationEnum
 * @Description: 枚举 常量
 * @date 2018/1/7
 */
public enum DeleteFlagEnum {

    ON("0", "未删除"),
    OFF("1","已删除");

    private String code;
    private String name;

    DeleteFlagEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static DeleteFlagEnum getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (DeleteFlagEnum e : DeleteFlagEnum.values()) {
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