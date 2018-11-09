package cn.com.leadu.cmsxc.appuser.service;

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
    String getTheTypeOfCarMachine(@RequestParam(value = ".url", required = true) String url,
                                  @RequestParam(value = "simCode", required = true) String simCode);

    @RequestMapping(value = "/JSKHInterface", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getAttchmentFile(@RequestParam(value = ".url", required = true) String url,
                                  @RequestParam(value = "vehicleIdentifyNum", required = true) String vehicleIdentifyNum);

    @RequestMapping(value = "/JSKHInterface", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getCommissionDetail(@RequestParam(value = ".url", required = true) String url,
                            @RequestParam(value = "vehicleIdentifyNum", required = true) String vehicleIdentifyNum);

}
