package cn.com.leadu.cmsxc.appuser.util.constant.enums;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 贴gps激活结果
 */
public enum GpsActiveEnums {
    FAIL("0", "激活失败"),
    SUCCESS("1","激活成功");

    private String value;
    private String type;

    GpsActiveEnums(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static GpsActiveEnums getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (GpsActiveEnums e : GpsActiveEnums.values()) {
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
