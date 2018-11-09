package cn.com.leadu.cmsxc.appuser.util.constant.enums;

/**
 * Created by yuanzhenxia on 2018/1/25.
 *
 * 积分代码
 */
public enum ScoreCodeEnums {
    REGISTER("01", "注册"),
    VIEW("03", "查看悬赏"),
    SCAN("04","车牌扫描"),
    SEND("05","赠送积分"),
    ACCEPT("06","领取积分");

    private String value;
    private String type;

    ScoreCodeEnums(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static ScoreCodeEnums getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (ScoreCodeEnums e : ScoreCodeEnums.values()) {
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
