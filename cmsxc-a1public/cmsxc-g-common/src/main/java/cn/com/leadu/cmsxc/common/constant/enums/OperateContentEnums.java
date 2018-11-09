package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * Created by yuanzhenxia on 2018/2/8.
 *
 * 授权日志表操作内容
 */
public enum OperateContentEnums {

    APPLYING("01", "申请授权"),
    REFUSE("02","拒绝申请"),
    AUTHORIZETED("03","授权操作"),
    OUTTIME("04","申请失效"),
    FINISH("05","完成"),
    CANCEL("06","申请取消"),
    AUTHORIZATIONOUTTIME("07","授权失效"),
    LEASECANCEL("08","委托公司取消"),
    DELAY("09","授权延期");

    private String value;
    private String type;

    OperateContentEnums(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static OperateContentEnums getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (OperateContentEnums e : OperateContentEnums.values()) {
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
