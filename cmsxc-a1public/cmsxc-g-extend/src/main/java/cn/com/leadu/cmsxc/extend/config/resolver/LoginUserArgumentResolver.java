package cn.com.leadu.cmsxc.extend.config.resolver;

import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.util.UserInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author qiaomengnan
 * @ClassName: LoginUserArgumentResolver
 * @Description: 注入登录用户
 * @date 2018/1/7
 */

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    UserInfoUtil userInfoUtil;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(AuthUserInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        return userInfoUtil.getUser(methodParameter.getParameterType());
    }

}
