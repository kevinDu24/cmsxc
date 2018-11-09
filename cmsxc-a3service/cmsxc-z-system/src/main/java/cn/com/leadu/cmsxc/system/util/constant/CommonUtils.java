package cn.com.leadu.cmsxc.system.util.constant;

import java.text.DecimalFormat;

/**
 * Created by huzongcheng on 2018/2/5.
 */
public class CommonUtils {
    public static Boolean isCardId(String str){
        return str.matches("(\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)");
    }

    public static Boolean isNumber(String str){
        return str.matches("[0-9]+");
    }

    public static String getFileSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

}
