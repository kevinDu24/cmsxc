package cn.com.leadu.cmsxc.webclient.oauth2.rpc;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author qiaomengnan
 * @ClassName: Oauth2Rpc
 * @Description: oauth2认证远程调用
 * @date 2018/1/9
 */
@FeignClient("${cmsxc.feigns.serverNames.cmsCAgent}")
public interface Oauth2Rpc {

    @RequestMapping(value = "api/auth/oauth/token",method = RequestMethod.POST)
    ResponseEntity<RestResponse> oauthToken(@RequestParam("grant_type") String grantType,
                                            @RequestParam("username") String username,
                                            @RequestParam("pwd") String pwd,
                                            @RequestHeader("authorization") String basic);

}
