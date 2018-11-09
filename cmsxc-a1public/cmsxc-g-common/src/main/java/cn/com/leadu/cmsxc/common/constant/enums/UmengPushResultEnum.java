package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * 调用友盟推送结果枚举类
 *
 * @author qiaomengnan
 * @ClassName: TestStationEnum
 * @Description: 枚举 常量
 * @date 2018/1/7
 */
public enum UmengPushResultEnum {

    FAIL("0","失败"),
    SUCCESS("1", "成功");

    private String code;
    private String name;

    UmengPushResultEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static UmengPushResultEnum getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (UmengPushResultEnum e : UmengPushResultEnum.values()) {
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