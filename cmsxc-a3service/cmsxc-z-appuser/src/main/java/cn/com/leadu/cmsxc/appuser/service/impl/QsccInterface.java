package cn.com.leadu.cmsxc.appuser.service.impl;

import cn.com.leadu.cmsxc.extend.message.Message;
import cn.com.leadu.cmsxc.pojo.appuser.vo.StopTimeInfoVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by LEO on 16/10/10.
 */
@FeignClient(name = "qsccInterface", url = "${request.qsccUrl}")
public interface QsccInterface {

    @RequestMapping(value = "/sys/login", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> login(@RequestHeader(value="Authorization") String authorization,
                                  @RequestHeader(value="channelId") String channelId,
                                  @RequestHeader(value="deviceType") String deviceType);

    @RequestMapping(value = "/recovery/getDeviceList", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getDeviceList(@RequestHeader(value="uniqueMark") String uniqueMark,
                                  @RequestParam(value = "vehicleIdentifyNum", required = true) String vehicleIdentifyNum);

    @RequestMapping(value = "/recovery/getVehicleInfo", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getVehicleInfo(@RequestHeader(value="uniqueMark") String uniqueMark,
                                           @RequestParam(value = "userName", required = true) String userName);

    @RequestMapping(value = "/recovery/newPosition", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> newPosition(@RequestHeader(value="uniqueMark") String uniqueMark,
                                           @RequestParam(value = "simCode", required = true) String simCode);

    @RequestMapping(value = "/recovery/getAddLatLon", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getAddLatLon(@RequestHeader(value="uniqueMark") String uniqueMark,
                                        @RequestParam(value = "leaseCusId", required = true) String leaseCusId);

    @RequestMapping(value = "/recovery/getHistory", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getHistory(@RequestHeader(value="uniqueMark") String uniqueMark,
                                         @RequestParam(value = "simCode", required = true) String simCode,
                                       @RequestParam(value = "beginTime", required = true) String beginTime,
                                       @RequestParam(value = "endTime", required = true) String endTime);

    @RequestMapping(value = "/recovery/getAddress", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getAddress(@RequestHeader(value="uniqueMark") String uniqueMark,
                                       @RequestParam(value = "lon", required = true) Double lon,
                                       @RequestParam(value = "lat", required = true) Double lat);

    @RequestMapping(value = "/recovery/getGpsDataDetail", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getGpsDataDetail(@RequestHeader(value="uniqueMark") String uniqueMark,
                                         @RequestParam(value = "id", required = true) String id);

    @RequestMapping(value = "/recovery/stopHistory", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> stopHistory(@RequestHeader(value="uniqueMark") String uniqueMark,
                                       @RequestParam(value = "simCode", required = true) String simCode,
                                        @RequestParam(value = "vehicleIdentifyNum", required = true) String vehicleIdentifyNum,
                                        @RequestParam(value = "applyNum", required = true) String applyNum,
                                       @RequestParam(value = "beginTime", required = false) String beginTime,
                                       @RequestParam(value = "endTime", required = false) String endTime,
                                        @RequestParam(value = "beginTimeNm", required = false) String beginTimeNm,
                                        @RequestParam(value = "endTimeNm", required = false) String endTimeNm,
                                        @RequestParam(value = "beginTimeMs", required = false) String beginTimeMs,
                                        @RequestParam(value = "endTimeMs", required = false) String endTimeMs);

    @RequestMapping(value = "/recovery/calculateStopInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> calculateStopInfo(@RequestHeader(value="uniqueMark") String uniqueMark,
                                              @RequestBody List<StopTimeInfoVo> timeList);

}
