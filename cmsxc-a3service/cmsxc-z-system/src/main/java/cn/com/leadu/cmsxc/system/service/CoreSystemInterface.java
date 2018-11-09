package cn.com.leadu.cmsxc.system.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by LEO on 16/10/10.
 */
@FeignClient(name = "coreSystemInterface", url = "${request.coreSystemUrl}")
public interface CoreSystemInterface {

    @RequestMapping(value = "/JSKHInterface", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getAuthPdf(@RequestParam(value = ".url", required = true) String url,
                          @RequestParam(value = "plate", required = true) String plate,
                          @RequestParam(value = "vehicleIdentifyNum", required = true) String vehicleIdentifyNum);

}
