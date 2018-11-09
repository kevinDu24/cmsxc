package cn.com.leadu.cmsxc.agent.rpc.cmsuser;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author qiaomengnan
 * @ClassName: CmsUserRpc
 * @Description: 远程调用user微服务
 * @date 2018/1/7
 */
@FeignClient(name = "${cmsxc.feigns.serverNames.cmsZSystem}")
public interface CmsSystemRpc {

    @RequestMapping(value = "/sys_resource/findSysResByUsername",method = RequestMethod.GET)
    ResponseEntity<RestResponse<List<String>>> findSysResByUsername(@RequestParam("username") String username);

    @RequestMapping(value = "/sys_user/findSysUserByUsername",method = RequestMethod.GET)
    ResponseEntity<RestResponse<Map<String,Object>>> findSysUserByUsername(@RequestParam("username") String username);

}
