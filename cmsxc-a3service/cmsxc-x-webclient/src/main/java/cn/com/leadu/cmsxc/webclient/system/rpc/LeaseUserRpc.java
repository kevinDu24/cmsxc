package cn.com.leadu.cmsxc.webclient.system.rpc;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.LeaseCompanyVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by yuanzhenxia on 2018/2/2.
 *
 * 委托公司用户管理
 */
@FeignClient("${cmsxc.feigns.serverNames.cmsCAgent}")
public interface LeaseUserRpc {
    @RequestMapping(value = "api/system/lease_user/findLeaseUserByPage" , method = RequestMethod.GET)
    ResponseEntity<RestResponse> findLeaseUserByPage(@RequestParam Map<String,Object> LeaseUserListVo);
    @RequestMapping(value = "api/system/lease_user/getLeaseUser" , method = RequestMethod.GET)
    ResponseEntity<RestResponse> getLeaseUser();
    @RequestMapping(value = "api/system/lease_user/register" , method = RequestMethod.POST)
    ResponseEntity<RestResponse> register(@RequestBody LeaseCompanyVo LeaseCompanyVo);
}
