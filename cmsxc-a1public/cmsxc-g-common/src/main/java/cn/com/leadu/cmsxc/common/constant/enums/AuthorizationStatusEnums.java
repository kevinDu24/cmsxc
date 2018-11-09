package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 授权状态
 */
public enum AuthorizationStatusEnums {
    APPLYING("01", "申请中"),
    REFUSE("02","被拒绝"),
    AUTHORIZETED("03","已授权"),
    OUTTIME("04","申请失效"),
    FINISH("05","已完成"),
    CANCEL("06","已取消"),
    AUTHORIZATIONOUTTIME("07","授权失效"),
    LEASECANCEL("08","委托公司取消");

    private String value;
    private String type;

    AuthorizationStatusEnums(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static AuthorizationStatusEnums getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (AuthorizationStatusEnums e : AuthorizationStatusEnums.values()) {
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
