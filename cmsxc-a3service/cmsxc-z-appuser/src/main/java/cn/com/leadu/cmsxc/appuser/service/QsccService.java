package cn.com.leadu.cmsxc.appuser.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appuser.vo.StopTimeInfoVo;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 轻松查车Service
 */
public interface QsccService {
    /**
     * 登录
     *
     * @return
     */
    ResponseEntity<RestResponse> login();

    /**
     * 根据车架号，获取车辆所有设备的设备号和设备类型
     *
     * @return
     */
    ResponseEntity<RestResponse> getDeviceList(String vehicleIdentifyNum);

    /**
     * 根据登录轻松查车的用户名（即车架号），获取车辆的车架号，申请编号，SIM卡号
     *
     * @return
     */
    ResponseEntity<RestResponse> getVehicleInfo(String userName);

    /**
     * 获取最新位置
     *
     * @return
     */
    ResponseEntity<RestResponse> newPosition(String simCode);

    /**
     * 获取客户家庭、常驻、工作地址信息的经纬度
     *
     * @return
     */
    ResponseEntity<RestResponse> getAddLatLon(String leaseCusId);

    /**
     * 获取历史轨迹
     *
     * @return
     */
    ResponseEntity<RestResponse> getHistory(String simCode, String beginTime, String endTime);

    /**
     * 根据经纬度获取地址
     *
     * @return
     */
    ResponseEntity<RestResponse> getAddress(Double lon, Double lat);

    /**
     * 根据设备ID获取GPS数据的统计信息
     *
     * @return
     */
    ResponseEntity<RestResponse> getGpsDataDetail(String id);

    /**
     * 历史停留
     *
     * @return
     */
    ResponseEntity<RestResponse> stopHistory(String simCode, String vehicleIdentifyNum, String applyNum, String beginTime , String endTime, String beginTimeNm, String endTimeNm, String beginTimeMs, String endTimeMs);

    /**
     * 计算某个停留点的时间分布
     *
     * @return
     */
    ResponseEntity<RestResponse> calculateStopInfo(List<StopTimeInfoVo> timeList);
}
