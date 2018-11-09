package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * 消息中心表消息触发类型枚举类
 *
 * @author qiaomengnan
 * @ClassName: TestStationEnum
 * @Description: 枚举 常量
 * @date 2018/1/7
 */
public enum MessageTriggerEnum {

    TASK_100("100", "委托公司推送工单"),
    TASK_101("101", "内勤分配任务"),
    TASK_102("102", "内勤改派任务"),
    TASK_103("103", "独家线索命中"),
    TASK_104("104", "线索命中"),
    TASK_105("105", "新增催收记录"),
    TASK_106("106", "内勤催单"),
    TASK_107("107", "审核专员催单"),
    TASK_108("108", "任务取消"),
    AUTH_201("201", "获得授权"),
    AUTH_202("202", "授权延期"),
    AUTH_203("203", "授权失效"),
    AUTH_204("204","授权被拒"),
    AUTH_205("205","申请授权");

    private String code;
    private String name;

    MessageTriggerEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static MessageTriggerEnum getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (MessageTriggerEnum e : MessageTriggerEnum.values()) {
            if (code.equals(e.getCode())) {
                return e;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getNane() {
        return name;
    }

}