package cn.com.leadu.cmsxc.appuser.util.constant.enums;

/**
 * Created by yuanzhenxia on 2018/1/29.
 *
 * 扫描方式枚举类
 */
public enum ScanTypeEnums {
    PLATE("0", "车牌号"),
    VEHICLE_IDENTIFY_NUM("1","车架号");

    private String value;
    private String type;

    ScanTypeEnums(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static ScanTypeEnums getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (ScanTypeEnums e : ScanTypeEnums.values()) {
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
