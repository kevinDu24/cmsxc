package cn.com.leadu.cmsxc.webclient.system.rpc;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.system.vo.information.NewPublishVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by yuanzhenxia on 2018/2/5.
 *
 * 新闻
 */
@FeignClient("${cmsxc.feigns.serverNames.cmsCAgent}")
public interface HomePageInfoRpc {

    @RequestMapping(value = "api/system/informations/newsPublish" , method = RequestMethod.POST)
    ResponseEntity<RestResponse> newsPublish(@RequestBody NewPublishVo newPublishVo);
}
