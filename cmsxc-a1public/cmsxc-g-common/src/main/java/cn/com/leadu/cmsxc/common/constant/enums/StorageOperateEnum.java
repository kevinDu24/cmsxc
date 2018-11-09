package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * Created by yuanzhenxia on 2018/8/7.
 *
 * 收车入库状态
 */
public enum StorageOperateEnum {

    RECOVERY_FINISH("01", "收车完成"),
    ARRIVE_PARKING("02","抵达停车场"),
    RECOVERY_SUBMIT("03","送车人资料上传"),
    PARKING_SUBMIT("06","停车场资料上传"),
    REBACK_MODIFY("07","资料驳回"),
    STORAGE_FINISH("08","入库完成"),
    FORBID_STORAGE("09","终止入库"),
    MODIFY_PARKING("99","修改停车场");


    private String value;
    private String type;

    StorageOperateEnum(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static StorageOperateEnum getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (StorageOperateEnum e : StorageOperateEnum.values()) {
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
