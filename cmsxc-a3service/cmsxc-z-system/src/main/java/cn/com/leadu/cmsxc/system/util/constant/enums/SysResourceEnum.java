package cn.com.leadu.cmsxc.system.util.constant.enums;

/**
 * @author qiaomengnan
 * @ClassName: TestStationEnum
 * @Description: 枚举 常量
 * @date 2018/1/7
 */
public enum SysResourceEnum {

    MENU(0, "菜单"),
    RES(1,"接口资源");

    private Integer type;
    private String desc;

    SysResourceEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static SysResourceEnum getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (SysResourceEnum e : SysResourceEnum.values()) {
            if (code.equals(e.getType())) {
                return e;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }


}