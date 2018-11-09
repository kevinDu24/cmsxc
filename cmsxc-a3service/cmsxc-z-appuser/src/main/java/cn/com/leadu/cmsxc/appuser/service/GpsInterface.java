package cn.com.leadu.cmsxc.appuser.service;

import cn.com.leadu.cmsxc.pojo.appuser.vo.GpsActiveVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by LEO on 16/10/10.
 */
@FeignClient(name = "gpsSystemUrlInterface", url = "${request.gpsSystemUrl}")
public interface GpsInterface {

    @RequestMapping(value = "/gpsApp/active", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String active(@RequestBody GpsActiveVo gpsActiveVo);

    @RequestMapping(value = "/gpsApp/getLatestPositionAndResort", method = RequestMethod.GET)
    String getLatestPositionAndResort(@RequestParam("vehicleIdentifyNum")  String vehicleIdentifyNum);

}
