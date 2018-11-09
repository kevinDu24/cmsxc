package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * 新闻资讯枚举类
 *
 * @author qiaomengnan
 * @ClassName: TestStationEnum
 * @Description: 枚举 常量
 * @date 2018/1/7
 */
public enum InformationEnum {

    NEWS("1", "BUNNER图"),
    AD("2","广告"),
    AGREEMENT("3","用户协议"),
    QUESTION("4","常见问题"),
    ABOUTUS("5","关于我们");

    private String code;
    private String name;

    InformationEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static InformationEnum getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (InformationEnum e : InformationEnum.values()) {
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