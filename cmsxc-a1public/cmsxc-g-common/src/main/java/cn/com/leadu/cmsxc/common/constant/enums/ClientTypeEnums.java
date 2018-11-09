package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * Created by yuanzhenxia on 2018/3/7.
 */
public enum ClientTypeEnums {
    IOS("1", "苹果版"),
    ANDROID("0","安卓版");

    private String code;
    private String name;

    ClientTypeEnums(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ClientTypeEnums getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (ClientTypeEnums e : ClientTypeEnums.values()) {
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
