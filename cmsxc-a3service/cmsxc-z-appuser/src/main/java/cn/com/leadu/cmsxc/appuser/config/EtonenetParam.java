package cn.com.leadu.cmsxc.appuser.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by pengchao on 2017/2/18.
 *
 * 网关信息
 */
@Data
@ConfigurationProperties(prefix = "etonenetParam")
public class EtonenetParam {
    private String mtUrl;
    private String command;
    private String spid;
    private String sppassword;
    private String spsc;
    private String sa;
    private String haltPhoneNum;


    public String getSpsc(){
        if("0".equals(spsc))
            return "00";
        else
            return spsc;
    }
}