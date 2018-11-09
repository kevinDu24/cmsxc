package cn.com.leadu.cmsxc.extend.interceptor;

import cn.com.leadu.cmsxc.extend.config.WebProperties;
import cn.com.leadu.cmsxc.extend.service.LogService;
import cn.com.leadu.cmsxc.extend.vo.AppRequestLogTmpVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Enumeration;

/**
 * @author qiaomengnan
 * @ClassName: ParameterInterceptor
 * @Description: 拦截请求参数并打印
 * @date 2018/1/7
 */

public class ParameterInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParameterInterceptor.class);

    private LogService logService;

    private WebProperties webProperties;

    public ParameterInterceptor(){}

    public ParameterInterceptor(LogService logService,WebProperties webProperties){
        this.logService = logService;
        this.webProperties = webProperties;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {

            String requestParams = null;
            //如果两个都没进就只打印路径
            boolean isture = true;
            //1.from表单提交(get也能抓到参数)
            Enumeration<String> parameterNames = request.getParameterNames();
            if (parameterNames != null && parameterNames.hasMoreElements()) {
                StringBuffer stringBuffer = new StringBuffer("{");
                while (parameterNames.hasMoreElements()) {
                    String value = (String) parameterNames.nextElement();//调用nextElement方法获得元素
                    String parameter = request.getParameter(value);
                    stringBuffer.append("\"" + value + "\":\"" + parameter + "\",");
                }
                StringBuffer sb = new StringBuffer(stringBuffer.toString().substring(0, stringBuffer.length() - 1));
                sb.append("}");
                requestParams = sb.toString();
                StringBuffer parameter = new StringBuffer("request_url=[").append(request.getRequestURI()).append("] request_params:[").append(requestParams).append("]");
                LOGGER.info(parameter.toString());
                isture = false;
            }
            //2.json,xml提交
            ServletInputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(isr);
                StringBuffer sb = new StringBuffer();
                String result = "";
                while ((result = br.readLine()) != null) {
                    sb.append(result);
                }
                if (sb.length() > 0) {
                    requestParams = sb.toString();
                    StringBuffer parameter = new StringBuffer("request_url=[").append(request.getRequestURI()).append("] request_params_body:[").append(requestParams).append("]");
                    LOGGER.info(parameter.toString());
                    isture = false;
                }
            }
            //如果两个都没进就只打印路径
            if (isture) {
                StringBuffer parameter = new StringBuffer("request_url=[").append(request.getRequestURI()).append("]");
                LOGGER.info(parameter.toString());
            }
            /**
             * 如果有人继承了该service说明需要对于本次请求做出处理
             */
            if (logService != null) {
                AppRequestLogTmpVo appRequestLogTmpVo = new AppRequestLogTmpVo();
                appRequestLogTmpVo.setRequestUrl(request.getRequestURI());
                appRequestLogTmpVo.setRequestParams(requestParams);
                Object client = request.getHeader(webProperties.getAppHeaderClient());
                if (client != null)
                    appRequestLogTmpVo.setClient(Integer.valueOf(client.toString()));
                appRequestLogTmpVo.setVersion(request.getHeader(webProperties.getAppHeaderVersion()));
                logService.saveLog(appRequestLogTmpVo);
            }

        } catch (Exception ex) {
            LOGGER.error("request_resolve_error:", ex);
        } finally {
            return true;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
