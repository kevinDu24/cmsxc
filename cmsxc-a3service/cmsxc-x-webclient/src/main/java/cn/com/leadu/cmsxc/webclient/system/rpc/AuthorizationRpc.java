package cn.com.leadu.cmsxc.webclient.system.rpc;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.system.vo.AuthorizationOperateVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by yuanzhenxia on 2018/2/5.
 *
 * 授权操作
 */
@FeignClient("${cmsxc.feigns.serverNames.cmsCAgent}")
public interface AuthorizationRpc {

    @RequestMapping(value = "api/system/authorization/findAuthorizationListByPage" , method = RequestMethod.GET)
    ResponseEntity<RestResponse> findAuthorizationListByPage(@RequestParam Map<String,Object> authorizationVo);
    @RequestMapping(value = "api/system/authorization/authorization" , method = RequestMethod.POST)
    ResponseEntity<RestResponse> authorization(@RequestBody AuthorizationOperateVo vo);
    @RequestMapping(value = "api/system/authorization/refuse" , method = RequestMethod.POST)
    ResponseEntity<RestResponse> refuse(@RequestBody AuthorizationOperateVo vo);
    @RequestMapping(value = "api/system/authorization/delay" , method = RequestMethod.POST)
    ResponseEntity<RestResponse> delay(@RequestBody AuthorizationOperateVo vo);
    @RequestMapping(value="api/system/authorization/getPhotoList",method = RequestMethod.GET)
    ResponseEntity<RestResponse> getPhotoList(@RequestParam("photoUuid") String photoUuid);
}
