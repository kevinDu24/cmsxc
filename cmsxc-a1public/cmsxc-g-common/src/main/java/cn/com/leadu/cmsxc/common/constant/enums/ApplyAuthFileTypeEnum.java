package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * 授权附件类型枚举类
 *
 * @author qiaomengnan
 * @ClassName: TestStationEnum
 * @Description: 枚举 常量
 * @date 2018/1/7
 */
public enum ApplyAuthFileTypeEnum {

    VEDIO("0", "视频"),
    OTHER_PHOTO("1","其他图片"),
    FRONT_PHOTO("2","车辆正面照"),
    EXTERIOR_PHOTO("3","车辆外观照");

    private String code;
    private String name;

    ApplyAuthFileTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ApplyAuthFileTypeEnum getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (ApplyAuthFileTypeEnum e : ApplyAuthFileTypeEnum.values()) {
            if (code.equals(e.getCode())) {
                return e;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

}