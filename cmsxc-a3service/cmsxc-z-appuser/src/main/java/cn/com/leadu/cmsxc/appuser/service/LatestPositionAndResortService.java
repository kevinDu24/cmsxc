package cn.com.leadu.cmsxc.appuser.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import org.springframework.http.ResponseEntity;

/**
 * Created by yuanzhenxia on 2018/8/1.
 *
 * 根据车架号查询最新位置信息及常去地址
 */
public interface LatestPositionAndResortService {
    /**
     * 根据车架号查询最新位置信息及常去地址
     *
     * @param vehicleIdentifyNum 车架号
     * @return
     */
    ResponseEntity<RestResponse> getLatestPositionAndResort(String vehicleIdentifyNum);

}
