package cn.com.leadu.cmsxc.common.constant.enums;

/**
 * @author qiaomengnan
 * @ClassName: TestStationEnum
 * @Description: 枚举 常量
 * @date 2018/1/7
 */
public enum CityCodeEnum {

    SHANG_HAI("101", "上海"),
    GUANG_ZHOU("102","广州"),
    BEI_JING("103","北京");

    private String code;
    private String name;

    CityCodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static CityCodeEnum getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (CityCodeEnum e : CityCodeEnum.values()) {
            if (code.equals(e.getCode())) {
                return e;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public static void main(String[] args) {
        System.out.println(CityCodeEnum.SHANG_HAI.code);
        System.out.println(CityCodeEnum.BEI_JING.name);
    }

}