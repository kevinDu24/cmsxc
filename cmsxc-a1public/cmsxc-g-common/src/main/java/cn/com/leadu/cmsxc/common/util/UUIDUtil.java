package cn.com.leadu.cmsxc.common.util;

import java.util.UUID;

/**
 * Created by qiaohao on 2017/9/12.
 */
public class UUIDUtil {

    public static String getUUID(){
        String uuid = UUID.randomUUID().toString().replace("-","");
        return uuid;
    }

}
