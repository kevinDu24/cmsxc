package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * Created by yuanzhenxia on 2018/1/17.
 *
 * 用户角色枚举类
 */
public enum UserRoleEnums {
    ADMIN("0", "超级管理员"),
//    RECOVERY_ADMIN("01", "收车公司一级管理员"),
    RECOVERY_MANAGER("02","内勤人员"),
    RECOVERY_MEMBER("03","收车公司业务员"),
    RECOVERY_OTHER("04","散户"),
    RECOVERY_BOSS("09","总经理"),
    LEASE_ADMIN("11","委托公司一级管理员"),
    LEASE_AUDITOR("12","委托公司审核专员"),
    PARKING_ADMIN("21","停车场管理员"),
    PARKING_MANAGER("22","库管");

    private String type;
    private String value;

    UserRoleEnums(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static UserRoleEnums getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (UserRoleEnums e : UserRoleEnums.values()) {
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
