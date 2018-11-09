package cn.com.leadu.cmsxc.appuser.util.constant.enums;

/**
 * Created by yuanzhenxia on 2018/1/29.
 *
 * 是否查分配枚举类
 */
public enum AssignmentEnums {
    NO_ASSIGNED("0", "未分配"),
    ASSIGNED("1","已分配");

    private String value;
    private String type;

    AssignmentEnums(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static AssignmentEnums getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (AssignmentEnums e : AssignmentEnums.values()) {
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
