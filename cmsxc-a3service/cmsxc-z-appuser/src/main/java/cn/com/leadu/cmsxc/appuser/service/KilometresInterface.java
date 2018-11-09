package cn.com.leadu.cmsxc.appuser.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by yuanzhenxia on 2018/8/7.
 *
 * 获取公里数外部接口
 */
@FeignClient(name = "kilometresInterface", url = "${request.kilometresUrl}")
public interface KilometresInterface {

    @RequestMapping( method = RequestMethod.GET)
    String getKilometres(@RequestParam("origins")  String origins,
                         @RequestParam("destinations")  String destinations,
                         @RequestParam("output")  String output,
                         @RequestParam("ak")  String ak,
                         @RequestParam("coord_type")  String coord_type);
}
