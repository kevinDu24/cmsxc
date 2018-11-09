package cn.com.leadu.cmsxc.agent.rpc.cmsoauth2;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by qiaohao on 2017/11/14.
 */
//@FeignClient("${cmsxc.feigns.serverNames.oauth2Server}")
@FeignClient("cmsxc-c-oauth2")
public interface CmsOauth2Rpc {

    @RequestMapping(value = "/oauth/users/getTokenExpiresIn",method = RequestMethod.POST)
    ResponseEntity<Integer> getTokenExpiresIn(@RequestHeader("Authorization") String token);

}
