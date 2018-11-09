package cn.com.leadu.cmsxc.appuser.util.constant.enums;

/**
 * Created by yuanzhenxia on 2018/1/29.
 *
 * 是否命中flag
 */
public enum TaskAssignOperateEnums {
    ASSIGN_TASK("0", "分配任务"),
    RESTORE_TASK("1","小组解散"),
    CHANGE_TASK("2","任务改派"),
    CANCEL_TASK("3","任务重置");

    private String value;
    private String type;

    TaskAssignOperateEnums(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static TaskAssignOperateEnums getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (TaskAssignOperateEnums e : TaskAssignOperateEnums.values()) {
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
