package cn.com.leadu.cmsxc.webclient.system.rpc;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by yuanzhenxia on 2018/2/2.
 *
 * 文件上传
 */
@FeignClient(name = "fileRpc", url = "${cmsxc.feigns.serverNames.cmsCAgent}")
public interface FileRpc {
    @RequestMapping(value = "api/system/file/download/{type}/{loadDate}/{fileName}" , method = RequestMethod.GET)
    byte[] downloadFile(@PathVariable("type") String type, @PathVariable("fileName") String fileName, @PathVariable("loadDate") String loadDate);
}
