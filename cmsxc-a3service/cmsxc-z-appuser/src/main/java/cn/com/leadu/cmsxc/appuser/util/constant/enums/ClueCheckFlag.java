package cn.com.leadu.cmsxc.appuser.util.constant.enums;

/**
 * Created by yuanzhenxia on 2018/1/29.
 *
 * 是否查看过
 */
public enum ClueCheckFlag {
    NOTCHECK("0", "未查看"),
    CHECK("1","已查看");

    private String value;
    private String type;

    ClueCheckFlag(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static ClueCheckFlag getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (ClueCheckFlag e : ClueCheckFlag.values()) {
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
