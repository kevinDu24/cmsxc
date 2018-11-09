package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * Created by yuanzhenxia on 2018/1/17.
 *
 * 车辆工单状态
 */
public enum TaskStatusEnums {
    NORMAL("0", "正常"),
    FINISH("1", "完成"),
    CANCEL("2","取消");

    private String value;
    private String type;

    TaskStatusEnums(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static TaskStatusEnums getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (TaskStatusEnums e : TaskStatusEnums.values()) {
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
