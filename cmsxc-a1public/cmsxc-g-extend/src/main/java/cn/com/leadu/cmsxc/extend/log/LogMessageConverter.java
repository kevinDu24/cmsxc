package cn.com.leadu.cmsxc.extend.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.com.leadu.cmsxc.extend.util.UserInfoUtil;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qiaomengnan
 * @ClassName: LogMessageConverter
 * @Description: 日志格式转换器
 * @date 2018/1/7
 */
public class LogMessageConverter extends ClassicConverter {

    public static final String hostAddress;

    static {
        StringBuffer msg = new StringBuffer("");
        String hostAddressTemp = System.getenv("eureka.instance.ip-address");
        String serverPort = System.getenv("server.port");
        if(StringUtil.isNotNull(hostAddressTemp) && StringUtil.isNotNull(serverPort)) {
            msg.append(hostAddressTemp);
            msg.append(":");
            msg.append(serverPort);
            msg.append("-");
            hostAddress = msg.toString();
        }else{
            hostAddress = "";
        }
    }

    @Override
    public String convert(ILoggingEvent event) {
        if(UserInfoUtil.userInfoUtil == null)
            return hostAddress;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(request != null){
            StringBuffer msg = new StringBuffer();
            if(StringUtil.isNotNull(hostAddress)){
                msg.append(hostAddress);
                msg.append("-");
            }
            String userName = UserInfoUtil.getUserName();
            if(StringUtil.isNotNull(userName)){
                msg.append(userName);
                msg.append("-");
            }
            String chainHeader = UserInfoUtil.getChain();
            if(StringUtil.isNotNull(chainHeader)) {
                msg.append(chainHeader);
                msg.append("-");
            }
            return msg.toString();
        }
        return hostAddress;
    }

}
