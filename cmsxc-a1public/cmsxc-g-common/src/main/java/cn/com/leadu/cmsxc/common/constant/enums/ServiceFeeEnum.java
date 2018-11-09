package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * Created by tianshuai on 2017/4/3.
 */
public enum ServiceFeeEnum {

    /**
     * {@code a 服务分区间[0,10000)}.
     */
    RANGE_A("a"),

    /**
     * {@code b 服务分区间[10000,20000)}.
     */
    RANGE_B("b"),

    /**
     * {@code c 服务分区间[20000,30000)}.
     */
    RANGE_C("c"),

    /**
     * {@code d 服务分区间[30000,40000)}.
     */
    RANGE_D("d"),

    /**
     * {@code e 服务分区间[40000,∞)}.
     */
    RANGE_E("e"),

    /**
     * {@code x 服务分区间不限}.
     */
    RANGE_X("x");

    private final String value;
    public String value() {
        return this.value;
    }

    ServiceFeeEnum(String value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return this.value;
    }


}
