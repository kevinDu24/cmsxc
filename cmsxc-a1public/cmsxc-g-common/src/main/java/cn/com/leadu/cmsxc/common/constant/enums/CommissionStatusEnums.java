package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * Created by yuanzhenxia on 2018/8/9.
 *
 * 佣金结算状态
 */
public enum CommissionStatusEnums {
    APPLY("01", "申请中"),
    FIRST_AUDITING_PASS("02","初审通过"),
    FIRST_AUDITING_REFUSE("03","初审拒绝"),
    LAST_AUDITING_PASS("04","终审通过"),
    LAST_AUDITING_REFUSE("05","终审拒绝");

    private String code;
    private String name;

    CommissionStatusEnums(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static CommissionStatusEnums getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (CommissionStatusEnums e : CommissionStatusEnums.values()) {
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
