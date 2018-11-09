package cn.com.leadu.cmsxc.webclient.system.rpc;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryCompanyVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by yuanzhenxia on 2018/2/1.
 *
 * 收车公司用户管理
 */
@FeignClient("${cmsxc.feigns.serverNames.cmsCAgent}")
public interface RecoveryUserRpc {
    @RequestMapping(value = "api/system/recovery_user/findRecoveryUserByPage" , method = RequestMethod.GET)
    ResponseEntity<RestResponse> findRecoveryUserByPage(@RequestParam Map<String,Object> recoveryUserListVo);
    @RequestMapping(value = "api/system/recovery_user/register" , method = RequestMethod.POST)
    ResponseEntity<RestResponse> register(@RequestBody RecoveryCompanyVo recoveryCompanyVo);
}
