package cn.com.leadu.cmsxc.oauth2.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qiaohao on 2017/8/23.
 */
public class StringUtil {

    /**
     * 返回是否为空
     *
     * @param param
     * @return
     */
    public static boolean isNull(Object param) {
        if (param == null)
            return true;
        else if (param.toString().trim().equals(""))
            return true;
        else
            return false;
    }

    /**
     * 返回是否不为空
     * @param param
     * @return
     */
    public static boolean isNotNull(Object param) {
        return !isNull(param);
    }

    /**
     * 是否包含不为空
     * @param params
     * @return
     */
    public static boolean isContainNotNull(Object ...params) {
        for(Object param : params) {
            if (param != null && !param.toString().trim().equals(""))
                return true;
        }
        return false;
    }

    /**
     * 判断两个值是否相等
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equals(String str1, String str2) {
        return str1 == null?str2 == null:str1.equals(str2);
    }

    /**
     * 判断两个值是否不相等
     * @param str1
     * @param str2
     * @return
     */
    public static boolean notEquals(String str1, String str2) {
        return !equals(str1,str2);
    }

    /**
     * 判断str1 是否包含 str2
     * @param str1
     * @param str2
     * @return
     */
    public static boolean container(String str1, String str2){
        return str1.indexOf(str2) != -1;
    }

    /**
     * 下划线转驼峰
     * @param str
     * @return
     */
    public static String lineToHump(String str){
        Pattern linePattern = Pattern.compile("_(\\w)");
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰转下划线
     * @param str
     * @return
     */
    public static String humpToLine(String str) {
        String regexStr = "[A-Z]";
        Matcher matcher = Pattern.compile(regexStr).matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String g = matcher.group();
            matcher.appendReplacement(sb, "_" + g.toLowerCase());
        }
        matcher.appendTail(sb);
        if (sb.charAt(0) == '_') {
            sb.delete(0, 1);
        }
        return sb.toString();
    }

}
