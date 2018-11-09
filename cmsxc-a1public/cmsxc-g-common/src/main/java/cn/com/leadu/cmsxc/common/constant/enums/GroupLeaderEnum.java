package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * 是否是组长flag枚举类
 *
 * @author qiaomengnan
 * @ClassName: TestStationEnum
 * @Description: 枚举 常量
 * @date 2018/1/7
 */
public enum GroupLeaderEnum {

    NO("0", "否"),
    YES("1","是");

    private String code;
    private String name;

    GroupLeaderEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static GroupLeaderEnum getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (GroupLeaderEnum e : GroupLeaderEnum.values()) {
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