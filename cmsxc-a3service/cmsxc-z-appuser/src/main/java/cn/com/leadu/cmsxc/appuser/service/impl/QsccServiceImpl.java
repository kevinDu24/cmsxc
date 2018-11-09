package cn.com.leadu.cmsxc.appuser.service.impl;

import cn.com.leadu.cmsxc.appuser.service.QsccService;
import cn.com.leadu.cmsxc.extend.message.Message;
import cn.com.leadu.cmsxc.extend.message.MessageType;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appuser.vo.QsccHeaderInfoVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.StopTimeInfoVo;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/18.
 * <p>
 * 轻松查车ServiceImpl
 */
@Service
public class QsccServiceImpl implements QsccService {
    private static final Logger logger = LoggerFactory.getLogger(QsccServiceImpl.class);
    @Autowired
    private QsccInterface qsccInterface;
    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 登录接口
     *
     * @return
     */
    public ResponseEntity<RestResponse> login() {
        QsccHeaderInfoVo headerInfo = buildHeaderInfo();
        try {
            ResponseEntity<Message> result =
                    qsccInterface.login(headerInfo.getUserInfo(), headerInfo.getChannelId(), headerInfo.getDeviceType());
            return castResult(result);
        } catch (Exception e) {
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE, "", "登录定位系统失败"),
                    HttpStatus.OK);
        }
    }

    /**
     * 根据车架号，获取车辆所有设备的设备号和设备类型
     *
     * @return
     */
    public ResponseEntity<RestResponse> getDeviceList(String vehicleIdentifyNum) {
        QsccHeaderInfoVo headerInfo = buildHeaderInfo();
        try {
            ResponseEntity<Message> result =
                    qsccInterface.getDeviceList(headerInfo.getUniqueMark(), vehicleIdentifyNum);
            return castResult(result);
        } catch (Exception e) {
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE, "", "获取车辆设备列表失败"),
                    HttpStatus.OK);
        }
    }

    /**
     * 根据登录轻松查车的用户名（即车架号），获取车辆的车架号，申请编号，SIM卡号
     *
     * @return
     */
    public ResponseEntity<RestResponse> getVehicleInfo(String userName) {
        QsccHeaderInfoVo headerInfo = buildHeaderInfo();
        try {
            ResponseEntity<Message> result =
                    qsccInterface.getVehicleInfo(headerInfo.getUniqueMark(), userName);
            return castResult(result);
        } catch (Exception e) {
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE, "", "获取车辆信息失败"),
                    HttpStatus.OK);
        }
    }

    /**
     * 获取最新位置
     *
     * @return
     */
    public ResponseEntity<RestResponse> newPosition(String simCode) {
        QsccHeaderInfoVo headerInfo = buildHeaderInfo();
        try {
            ResponseEntity<Message> result =
                    qsccInterface.newPosition(headerInfo.getUniqueMark(), simCode);
            return castResult(result);
        } catch (Exception e) {
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE, "", "获取最新位置失败"),
                    HttpStatus.OK);
        }
    }

    /**
     * 获取客户家庭、常驻、工作地址信息的经纬度
     *
     * @return
     */
    public ResponseEntity<RestResponse> getAddLatLon(String leaseCusId) {
        QsccHeaderInfoVo headerInfo = buildHeaderInfo();
        try {
            ResponseEntity<Message> result =
                    qsccInterface.getAddLatLon(headerInfo.getUniqueMark(), leaseCusId);
            return castResult(result);
        } catch (Exception e) {
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE, "", "获取客户家庭、常驻、工作地址信息的经纬度失败"),
                    HttpStatus.OK);
        }
    }

    /**
     * 获取历史轨迹
     *
     * @return
     */
    public ResponseEntity<RestResponse> getHistory(String simCode, String beginTime, String endTime) {
        QsccHeaderInfoVo headerInfo = buildHeaderInfo();
        try {
            ResponseEntity<Message> result =
                    qsccInterface.getHistory(headerInfo.getUniqueMark(), simCode, beginTime, endTime);
            return castResult(result);
        } catch (Exception e) {
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE, "", "获取历史轨迹失败"),
                    HttpStatus.OK);
        }
    }

    /**
     * 根据经纬度获取地址
     *
     * @return
     */
    public ResponseEntity<RestResponse> getAddress(Double lon, Double lat) {
        QsccHeaderInfoVo headerInfo = buildHeaderInfo();
        try {
            ResponseEntity<Message> result =
                    qsccInterface.getAddress(headerInfo.getUniqueMark(), lon, lat);
            return castResult(result);
        } catch (Exception e) {
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE, "", "获取历史轨迹失败"),
                    HttpStatus.OK);
        }
    }

    /**
     * 根据设备ID获取GPS数据的统计信息
     *
     * @return
     */
    public ResponseEntity<RestResponse> getGpsDataDetail(String id) {
        QsccHeaderInfoVo headerInfo = buildHeaderInfo();
        try {
            ResponseEntity<Message> result =
                    qsccInterface.getGpsDataDetail(headerInfo.getUniqueMark(), id);
            return castResult(result);
        } catch (Exception e) {
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE, "", "根据设备ID获取GPS数据的统计信息失败"),
                    HttpStatus.OK);
        }
    }

    /**
     * 历史停留
     *
     * @return
     */
    public ResponseEntity<RestResponse> stopHistory(String simCode, String vehicleIdentifyNum, String applyNum, String beginTime, String endTime, String beginTimeNm, String endTimeNm, String beginTimeMs, String endTimeMs) {
        QsccHeaderInfoVo headerInfo = buildHeaderInfo();
        try {
            ResponseEntity<Message> result =
                    qsccInterface.stopHistory(headerInfo.getUniqueMark(), simCode, vehicleIdentifyNum, applyNum, beginTime, endTime, beginTimeNm, endTimeNm, beginTimeMs, endTimeMs);
            return castResult(result);
        } catch (Exception e) {
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE, "", "获取过往停留信息失败"),
                    HttpStatus.OK);
        }
    }

    /**
     * 计算某个停留点的时间分布
     *
     * @return
     */
    public ResponseEntity<RestResponse> calculateStopInfo(List<StopTimeInfoVo> timeList) {
        QsccHeaderInfoVo headerInfo = buildHeaderInfo();
        try {
            ResponseEntity<Message> result =
                    qsccInterface.calculateStopInfo(headerInfo.getUniqueMark(), timeList);
            return castResult(result);
        } catch (Exception e) {
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE, "", "计算某个停留点的时间分布失败"),
                    HttpStatus.OK);
        }
    }

    /**
     * http请求头部信息获取
     *
     * @return
     */
    private QsccHeaderInfoVo buildHeaderInfo() {
        QsccHeaderInfoVo vo = new QsccHeaderInfoVo();
        vo.setUserInfo(httpServletRequest.getHeader("userInfo"));
        vo.setChannelId(httpServletRequest.getHeader("channelId"));
        vo.setDeviceType(httpServletRequest.getHeader("deviceType"));
        vo.setUniqueMark(httpServletRequest.getHeader("uniqueMark"));
        return vo;
    }

    /**
     * 访问结果转化
     *
     * @param result
     * @return
     */
    private ResponseEntity<RestResponse> castResult(ResponseEntity<Message> result) {
        Message message = result.getBody();
        if (MessageType.MSG_TYPE_SUCCESS.equals(message.getStatus())) {
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, message.getData(), message.getError()),
                    result.getStatusCode());
        } else if (MessageType.MSG_TYPE_ERROR.equals(message.getStatus())) {
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE, message.getData(), message.getError()),
                    result.getStatusCode());
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE, message.getData(), message.getError()),
                result.getStatusCode());
    }
}
