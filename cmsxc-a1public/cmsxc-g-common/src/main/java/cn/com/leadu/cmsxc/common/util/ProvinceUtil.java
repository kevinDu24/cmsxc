package cn.com.leadu.cmsxc.common.util;

/**
 * 省份共通类
 * Created by system on 2017/11/17.
 */
public class ProvinceUtil {

    /**
     * 根据省份获取省份简称共同
     *
     * @param province
     * @return 省份简称
     */
    public static String getProvinceShortByName(String province) {
        if(StringUtil.isNull(province)){
            return null;
        }
        if(province.indexOf("北京") != -1)
            return "京";
        else if(province.indexOf("天津") != -1)
            return "津";
        else if(province.indexOf("重庆") != -1)
            return "渝";
        else if(province.indexOf("上海") != -1)
            return "沪";
        else if(province.indexOf("河北") != -1)
            return "冀";
        else if(province.indexOf("山西") != -1)
            return "晋";
        else if(province.indexOf("辽宁") != -1)
            return "辽";
        else if(province.indexOf("吉林") != -1)
            return "吉";
        else if(province.indexOf("黑龙江") != -1)
            return "黑";
        else if(province.indexOf("江苏") != -1)
            return "苏";
        else if(province.indexOf("浙江") != -1)
            return "浙";
        else if(province.indexOf("安徽") != -1)
            return "皖";
        else if(province.indexOf("福建") != -1)
            return "闽";
        else if(province.indexOf("江西") != -1)
            return "赣";
        else if(province.indexOf("山东") != -1)
            return "鲁";
        else if(province.indexOf("河南") != -1)
            return "豫";
        else if(province.indexOf("湖北") != -1)
            return "鄂";
        else if(province.indexOf("湖南") != -1)
            return "湘";
        else if(province.indexOf("广东") != -1)
            return "粤";
        else if(province.indexOf("海南") != -1)
            return "琼";
        else if(province.indexOf("四川") != -1)
            return "川";
        else if(province.indexOf("贵州") != -1)
            return "黔";
        else if(province.indexOf("云南") != -1)
            return "云";
        else if(province.indexOf("陕西") != -1)
            return "陕";
        else if(province.indexOf("甘肃") != -1)
            return "甘";
        else if(province.indexOf("青海") != -1)
            return "青";
        else if(province.indexOf("台湾") != -1)
            return "台";
        else if(province.indexOf("内蒙古") != -1)
            return "蒙";
        else if(province.indexOf("广西") != -1)
            return "桂";
        else if(province.indexOf("宁夏") != -1)
            return "宁";
        else if(province.indexOf("新疆") != -1)
            return "新";
        else if(province.indexOf("西藏") != -1)
            return "藏";
        else if(province.indexOf("香港") != -1)
            return "港";
        else if(province.indexOf("澳门") != -1)
            return "澳";
        else
            return null;
    }
}
