package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 授权状态
 */
public enum TaskNodeEnums {
    APPLY_AUTH("1", "申请授权"),
    CANCEL_APPLY("2","取消申请"),
    REFUSE_AUTH("3","拒绝授权"),
    OBTAIN_AUTH("4","获得授权"),
    AUTH_DELAY("5","授权延期"),
    AUTH_EXPIRY("6","授权失效"),
    FINISH_TASK("7","工单完成"),
    LEASE_CANCEL_TASK("8","委托方取消工单");

    private String value;
    private String type;

    TaskNodeEnums(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static TaskNodeEnums getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (TaskNodeEnums e : TaskNodeEnums.values()) {
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
