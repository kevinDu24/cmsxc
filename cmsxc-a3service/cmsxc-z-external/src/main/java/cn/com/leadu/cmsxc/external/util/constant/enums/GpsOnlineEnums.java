package cn.com.leadu.cmsxc.external.util.constant.enums;

/**
 * Created by yuanzhenxia on 2018/1/17.
 *
 * GPS是否在线枚举类
 */
public enum GpsOnlineEnums {
    OUT("0", "在线"),
    ON("1", "离线");

    private String value;
    private String type;

    GpsOnlineEnums(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static GpsOnlineEnums getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (GpsOnlineEnums e : GpsOnlineEnums.values()) {
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
