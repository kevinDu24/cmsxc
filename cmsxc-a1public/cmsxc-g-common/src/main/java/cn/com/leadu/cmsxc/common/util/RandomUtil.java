package cn.com.leadu.cmsxc.common.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @description:
 * @author:qianh .
 * @since:2017/11/18
 */
public class RandomUtil {

    /**
     * @Title: getRandNum
     * @Description:按区间产生随机数
     * @param min
     * @param max
     * @return int
     * @throws
     *
     * @author qiaohao
     * @date 2017/11/18 02:24:47
     */
	public static int getRandNum(int min, int max) {
		int randNum = min + (int) (Math.random() * ((max - min) + 1));
		return randNum;
	}

    /**
     * @Title: getSixRandomCode
     * @Description:产生6位随机验证码
     * @param
     * @return java.lang.String
     * @throws
     *
     * @author qiaohao
     * @date 2017/11/18 02:24:23
     */
	public static String getSixRandomCode() {
		return String.valueOf(getRandNum(100000, 999999));
	}

	public static String getRegisterCode() {
		String[] beforeShuffle = new String[] { "2", "3", "4", "5", "6", "7",
				"8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
				"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z" };
		List list = Arrays.asList(beforeShuffle);
		Collections.shuffle(list);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i));
		}
		String afterShuffle = sb.toString();
		String result = afterShuffle.substring(5, 9);
		return result;
	}
}
