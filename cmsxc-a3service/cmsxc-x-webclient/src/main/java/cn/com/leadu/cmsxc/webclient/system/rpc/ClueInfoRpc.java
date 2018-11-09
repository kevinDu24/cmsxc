package cn.com.leadu.cmsxc.webclient.system.rpc;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.system.vo.ClueListParamVo;
import cn.com.leadu.cmsxc.pojo.system.vo.ClueListVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserSaveVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserSearchListVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author qiaomengnan
 * @ClassName: SysUserRpc
 * @Description: 系统用户远程Rpc调用
 * @date 2018/1/9
 */
@FeignClient("${cmsxc.feigns.serverNames.cmsCAgent}")
public interface ClueInfoRpc {
    /**
     * @Title:
     * @Description: 远程调用注册用户
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 11:26:47
     */
    @RequestMapping(value = "api/system/clueinfo/findClueInfoListByPage" , method = RequestMethod.GET)
    ResponseEntity<RestResponse> findClueInfoListByPage( @RequestParam Map<String,Object> clueListParamVo);
    /**
     * @Title:
     * @Description: 导出注册账户信息
     * @param
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 02:26:33
     */
    @RequestMapping(value = "api/system/clueinfo/findClueInfoList", method = RequestMethod.GET)
    List<ClueListVo> findClueInfoList(@RequestParam Map<String,Object> clueListParamVo);
}
