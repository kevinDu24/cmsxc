package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * Created by yuanzhenxia on 2018/8/7.
 *
 * 收车入库状态
 */
public enum StorageStateEnum {

    RECOVERY_FINISH("01", "收车完成"),
    ARRIVE_PARKING("02","抵达停车场"),
    MANAGER_CONFIRM("03","内勤确认中"),
    MANAGER_REBACK("04","内勤退回待修改"),
    FILES_UPLOAD("05","资料待上传"),
    FILES_AUDITING("06","资料审核中"),
    REBACK_MODIFY("07","退回待修改"),
    STORAGE_FINISH("08","入库完成"),
    FORBID_STORAGE("09","终止入库"),
    MODIFY_PARKING("99","修改停车场");


    private String value;
    private String type;

    StorageStateEnum(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static StorageStateEnum getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (StorageStateEnum e : StorageStateEnum.values()) {
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
