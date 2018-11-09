package cn.com.leadu.cmsxc.appuser.service.impl;

import cn.com.leadu.cmsxc.appuser.service.GpsInterface;
import cn.com.leadu.cmsxc.appuser.service.LatestPositionAndResortService;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appuser.vo.LatestPositionAndResortResVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by yuanzhenxia on 2018/8/1.
 *
 * 根据车架号查询最新位置信息及常去地址
 *
 */
@Service
public class LatestPositionAndResortServiceImpl implements LatestPositionAndResortService{
    private static final Logger logger = LoggerFactory.getLogger(LatestPositionAndResortServiceImpl.class);

    @Autowired
    private GpsInterface gpsInterface;
    @Autowired
    ObjectMapper objectMapper;
    /**
     * 根据车架号查询最新位置信息及常去地址
     *
     * @param vehicleIdentifyNum 车架号
     * @return
     */
    public ResponseEntity<RestResponse> getLatestPositionAndResort(String vehicleIdentifyNum){
        String jsonData = "";
        try {
            //调用车辆管理系统接口
            jsonData = gpsInterface.getLatestPositionAndResort(vehicleIdentifyNum);
        } catch (Exception e){
            e.printStackTrace();
            logger.error("根据车架号查询最新位置信息及常去地址接口异常");
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","根据车架号查询最新位置信息及常去地址失败"),
                    HttpStatus.OK);
        }
        LatestPositionAndResortResVo res = new LatestPositionAndResortResVo();
        try {
            res = objectMapper.readValue(jsonData, LatestPositionAndResortResVo.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","系统异常，请重试"),
                    HttpStatus.OK);
        }
        //如果返回结果为成功
        if("SUCCESS".equals(res.getStatus())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,res.getData(),""),
                    HttpStatus.OK);
        }else {
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","查询最新位置信息及常去地址失败"+res.getError()),
                    HttpStatus.OK);
        }
    }
}
