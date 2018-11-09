package cn.com.leadu.cmsxc.common.util;

import java.util.Arrays;
import java.util.List;

/**
 * Created by qiaohao on 2017/8/25.
 */
public class ArrayUtil {

    /**
     * 判断list是否为空或者长度为0
     * @param list
     * @return
     */
    public static boolean isNullOrLengthZero(List list){
        return list == null || list.size() == 0;
    }

    /**
     * 判断list是否不为空并且长度为0
     * @param list
     * @return
     */
    public static boolean isNotNullAndLengthNotZero(List list){
        return !isNullOrLengthZero(list);
    }

    /**
     * 判断list是否为空或者长度为0
     * @param list
     * @return
     */
    public static boolean isNullOrLengthZero(Object [] list){
        return list == null || list.length == 0;
    }

    /**
     * 判断list是否不为空并且长度为0
     * @param list
     * @return
     */
    public static boolean isNotNullAndLengthNotZero(Object [] list){
        return !isNullOrLengthZero(list);
    }

    /**
     * 数组转list
     * @param a
     * @param <T>
     * @return
     */
    public static <T> List<T> asList(T... a) {
        return Arrays.asList(a);
    }

    /**
     * 模糊匹配list中是否包含该字符串
     * @param list
     * @param val
     * @return
     */
    public static boolean vagueContains(List<String> list,String val){
        for(String str : list){
            if(str.indexOf(val) != -1){
                return true;
            }
        }
        return false;
    }


    /**
     * 模糊匹配字符串中是否包含list中保存的值
     * @param list
     * @param val
     * @return
     */
    public static boolean vagueContains(String val,List<String> list){
        for(String str : list){
            if(val.indexOf(str) != -1){
                return true;
            }
        }
        return false;
    }

    public static boolean equalsContains(List<String> list,String val){
        for(String str : list){
            if(str.equals(val)){
                return true;
            }
        }
        return false;
    }


    public static boolean notEqualsContains(List<String> list,String val){
        return !equalsContains(list,val);
    }


    /**
     * 求总和
     * @param params
     * @return
     */
    public static Integer count(List<Integer> params){
        Integer count = 0;
        for(Integer integer : params){
            count += integer;
        }
        return count;
    }


}
