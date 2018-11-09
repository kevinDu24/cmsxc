package cn.com.leadu.cmsxc.appuser.util.constant.enums;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 判断有，无
 */
public enum FlagEnums {
    NO("0", "无"),
    YES("1","有");

    private String value;
    private String type;

    FlagEnums(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static FlagEnums getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (FlagEnums e : FlagEnums.values()) {
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
