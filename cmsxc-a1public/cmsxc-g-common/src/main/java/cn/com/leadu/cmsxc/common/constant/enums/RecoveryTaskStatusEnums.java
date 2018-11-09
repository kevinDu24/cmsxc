package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * Created by yuanzhenxia on 2018/2/12.
 *
 * 收车公司派单任务状态
 */
public enum RecoveryTaskStatusEnums {
    WAITRECOVERY("01", "待收车"),
    OUTTIME("02","已过期"),
    OTHERAUTHORIZETED("03","他人已授权"),
    SELFFINISH("04","自己已完成"),
    OTHERFINISH("05","他人已完成"),
    CANCEL("06","已取消"),
    SELFAUTHORIZATION("07","自己已授权");


    private String value;
    private String type;

    RecoveryTaskStatusEnums(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static RecoveryTaskStatusEnums getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (RecoveryTaskStatusEnums e : RecoveryTaskStatusEnums.values()) {
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
