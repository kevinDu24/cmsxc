package cn.com.leadu.cmsxc.appuser.controller;

import cn.com.leadu.cmsxc.appuser.service.QsccService;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appuser.vo.StopTimeInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.bouncycastle.asn1.x509.X509ObjectIdentifiers.id;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 轻松查车Controller
 */
@RestController
@RequestMapping("qscc")
public class QsccController {
    private static final Logger logger = LoggerFactory.getLogger(QsccController.class);
    @Autowired
    private QsccService qsccService;

    /**
     * 登录接口
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> login() {
        try{
            return qsccService.login();
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("登录定位系统error",ex);
            throw new CmsServiceException("登录定位系统失败");
        }
    }

    /**
     * 根据车架号，获取车辆所有设备的设备号和设备类型
     *
     * @return
     */
    @RequestMapping(value = "/getDeviceList", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getDeviceList(String vehicleIdentifyNum) {
        try{
            return qsccService.getDeviceList(vehicleIdentifyNum);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取车辆设备列表error",ex);
            throw new CmsServiceException("获取车辆设备列表失败");
        }
    }

    /**
     * 根据登录轻松查车的用户名（即车架号），获取车辆的车架号，申请编号，SIM卡号
     *
     * @return
     */
    @RequestMapping(value = "/getVehicleInfo", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getVehicleInfo(String userName) {
        try{
            return qsccService.getVehicleInfo(userName);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取车辆信息error",ex);
            throw new CmsServiceException("获取车辆信息失败");
        }
    }

    /**
     * 获取最新位置
     *
     * @return
     */
    @RequestMapping(value = "/newPosition", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> newPosition(String simCode) {
        try{
            return qsccService.newPosition(simCode);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取最新位置error",ex);
            throw new CmsServiceException("获取最新位置失败");
        }
    }

    /**
     * 获取客户家庭、常驻、工作地址信息的经纬度
     *
     * @return
     */
    @RequestMapping(value = "/getAddLatLon", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getAddLatLon(String leaseCusId) {
        try{
            return qsccService.getAddLatLon(leaseCusId);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取客户家庭、常驻、工作地址信息的经纬度error",ex);
            throw new CmsServiceException("获取客户家庭、常驻、工作地址信息的经纬度失败");
        }
    }

    /**
     * 获取历史轨迹
     *
     * @return
     */
    @RequestMapping(value = "/getHistory", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getHistory(String simCode, String beginTime, String endTime) {
        try{
            return qsccService.getHistory(simCode,beginTime,endTime);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取历史轨迹error",ex);
            throw new CmsServiceException("获取历史轨迹失败");
        }
    }

    /**
     * 根据经纬度获取地址
     *
     * @return
     */
    @RequestMapping(value = "/getAddress", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getAddress(Double lon, Double lat) {
        try{
            return qsccService.getAddress(lon,lat);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("根据经纬度获取地址error",ex);
            throw new CmsServiceException("根据经纬度获取地址失败");
        }
    }

    /**
     * 获取GPS数据的统计信息
     *
     * @return
     */
    @RequestMapping(value = "/getGpsDataDetail", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getGpsDataDetail(String id) {
        try{
            return qsccService.getGpsDataDetail(id);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取GPS数据的统计信息error",ex);
            throw new CmsServiceException("获取GPS数据的统计信息失败");
        }
    }

    /**
     * 过往停留
     *
     * @return
     */
    @RequestMapping(value = "/stopHistory", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> stopHistory(String simCode, String vehicleIdentifyNum, String applyNum, String beginTime , String endTime, String beginTimeNm, String endTimeNm, String beginTimeMs, String endTimeMs){
        try{
            return qsccService.stopHistory(simCode, vehicleIdentifyNum, applyNum, beginTime, endTime,beginTimeNm, endTimeNm,beginTimeMs,endTimeMs );
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取过往停留error",ex);
            throw new CmsServiceException("获取过往停留信息失败");
        }
    }

    /**
     * 计算某个停留点的时间分布
     *
     * @return
     */
    @RequestMapping(value = "/calculateStopInfo", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> calculateStopInfo(@RequestBody List<StopTimeInfoVo> timeList) {
        try{
            return qsccService.calculateStopInfo(timeList);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("计算某个停留点的时间分布error",ex);
            throw new CmsServiceException("计算某个停留点的时间分布失败");
        }
    }


}
