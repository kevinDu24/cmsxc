package cn.com.leadu.cmsxc.appuser.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appuser.vo.GpsActiveVo;
import org.springframework.http.ResponseEntity;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 贴gpsService
 */
public interface GpsActiveService {

    /**
     * 激活check，验证对应车架号是否存在及是否对应多个车架号
     * @param vehicleIdentifyNum 车架号后六位
     * @return
     */
    public ResponseEntity<RestResponse> check(String vehicleIdentifyNum);

    /**
     * 贴gps
     * @param gpsActiveVo
     * @return
     */
    public ResponseEntity<RestResponse> active(GpsActiveVo gpsActiveVo, String userId);

    /**
     * 分页返回激活历史列表
     * @param userId 用户id
     * @return
     */
    public ResponseEntity<RestResponse> getActiveList(String userId, int page, int size);


}
