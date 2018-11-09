package cn.com.leadu.cmsxc.appuser.util.constant.enums;

/**
 * Created by yuanzhenxia on 2018/1/29.
 *
 * 是否命中flag
 */
public enum TargetFlag {
    NOTTARGET("0", "未命中"),
    TARGET("1","已命中");

    private String value;
    private String type;

    TargetFlag(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static TargetFlag getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (TargetFlag e : TargetFlag.values()) {
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
