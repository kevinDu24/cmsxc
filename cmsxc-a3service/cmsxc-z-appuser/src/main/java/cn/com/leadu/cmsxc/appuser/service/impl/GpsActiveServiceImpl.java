package cn.com.leadu.cmsxc.appuser.service.impl;

import cn.com.leadu.cmsxc.appuser.service.CoreSystemInterface;
import cn.com.leadu.cmsxc.appuser.service.GpsActiveService;
import cn.com.leadu.cmsxc.appuser.service.GpsInterface;
import cn.com.leadu.cmsxc.appuser.util.constant.enums.GpsActiveEnums;
import cn.com.leadu.cmsxc.common.util.ArrayUtil;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.appbusiness.repository.VehicleTaskRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.GpsActiveHistoryRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTask;
import cn.com.leadu.cmsxc.pojo.appuser.entity.GpsActiveHistory;
import cn.com.leadu.cmsxc.pojo.appuser.vo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 消息中心Service实现类
 */
@Service
public class GpsActiveServiceImpl implements GpsActiveService {
    private static final Logger logger = LoggerFactory.getLogger(GpsActiveServiceImpl.class);
    @Autowired
    private GpsActiveHistoryRepository gpsActiveHistoryRepository;
    @Autowired
    private GpsInterface gpsInterface;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private VehicleTaskRepository vehicleTaskRepository;
    @Autowired
    private CoreSystemInterface coreSystemInterface;


    private static String URL= "GetTheTypeOfCarMachine"; // 调用主系统的.url

    /**
     * 激活check，验证对应车架号是否存在及是否对应多个车架号
     * @param vehicleIdentifyNum 车架号后六位
     * @return
     */
    public ResponseEntity<RestResponse> check(String vehicleIdentifyNum){
        if(vehicleIdentifyNum == null || vehicleIdentifyNum.length() != 6){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","您输入的车架号信息不正确"),
                    HttpStatus.OK);
        }
        List<GpsActiveCheckVo> result = gpsActiveHistoryRepository.selectByLastSixNum(vehicleIdentifyNum);
        if(ArrayUtil.isNullOrLengthZero(result)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未查询到对应的车辆信息"),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, result,""),
                    HttpStatus.OK);
        }
    }

    /**
     * 贴gps
     * @param gpsActiveVo
     * @return
     */
    public ResponseEntity<RestResponse> active(GpsActiveVo gpsActiveVo,String userId){
        gpsActiveVo.setInstallPhoneNum(userId); //设定安装人员手机号
        // 调用主统接口，获取车机类型
        String result = coreSystemInterface.getTheTypeOfCarMachine(URL, gpsActiveVo.getSimCode());
        // json 解析
        GpsResultVo gpsResultVo = new GpsResultVo();
        try {
            gpsResultVo = objectMapper.readValue(result, GpsResultVo.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","系统异常，请重试"),
                    HttpStatus.OK);
        }
        if(!"00000000".equals(gpsResultVo.getCode())){
            logger.error(gpsResultVo.getMessage());
            if(StringUtil.isNotNull(gpsResultVo.getMessage())){
                return new ResponseEntity<RestResponse>(
                        RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"",gpsResultVo.getMessage()),
                        HttpStatus.OK);
            }
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","获取车机类型失败"),
                    HttpStatus.OK);
        }
        gpsActiveVo.setVehicleType(gpsResultVo.getVehicleType());// 设值，车机类型
        String jsonData = "";
        try {
            //调用车辆管理系统接口
            jsonData = gpsInterface.active(gpsActiveVo);
        } catch (Exception e){
            e.printStackTrace();
            logger.error("调用GPS激活接口异常");
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","激活失败"),
                    HttpStatus.OK);
        }
        GpsActiveResVo res = new GpsActiveResVo();
        try {
            res = objectMapper.readValue(jsonData, GpsActiveResVo.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","系统异常，请重试"),
                   HttpStatus.OK);
        }
        GpsActiveHistory gpsActiveHistory = new GpsActiveHistory();
        Date now = new Date();
        gpsActiveHistory.setVehicleIdentifyNum(gpsActiveVo.getVehicleIdentifyNum()); //车架号
        gpsActiveHistory.setPlate(gpsActiveVo.getPlate()); //车牌号
        gpsActiveHistory.setSimCode(gpsActiveVo.getSimCode()); //设备号
        gpsActiveHistory.setOperateTime(now); //激活操作时间
        gpsActiveHistory.setCreateTime(now);
        gpsActiveHistory.setCreator(userId);
        gpsActiveHistory.setUpdateTime(now);
        gpsActiveHistory.setUpdater(userId);
        String gpsSystemUserName = "";
        String gpsSystemUserPassword = "";
        //如果返回结果为成功
        if("SUCCESS".equals(res.getStatus())){
            gpsActiveHistory.setResult(GpsActiveEnums.SUCCESS.getType()); //设定结果为成功
            GpsActiveDetailResVo detalRes = res.getData();
            if(detalRes == null || StringUtil.isNull(detalRes.getGpsSystemUserName())){ //如果没有返回gps账号和密码，表示已经存在对应的gps用户名和密码，从工单表中取
                Example example = new Example(VehicleTask.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("vehicleIdentifyNum", gpsActiveVo.getVehicleIdentifyNum());
                List<VehicleTask> vehicleTaskList = vehicleTaskRepository.selectByExampleList(example);
                //如果存在
                if(vehicleTaskList != null && !vehicleTaskList.isEmpty()){
                    gpsSystemUserName = vehicleTaskList.get(0).getGpsSystemUserName();
                    gpsSystemUserPassword =vehicleTaskList.get(0).getGpsSystemUserPassword();
                }
            } else {
                gpsSystemUserName = detalRes.getGpsSystemUserName();
                gpsSystemUserPassword =detalRes.getGpsSystemUserPassword();
                //更新工单表的gps账号和密码
                vehicleTaskRepository.updateByVehicleIdentifyNum(gpsSystemUserName, gpsSystemUserPassword, gpsActiveVo.getVehicleIdentifyNum());
            }
            gpsActiveHistory.setGpsSystemUserName(gpsSystemUserName); //设定gps用户名
            gpsActiveHistory.setGpsSystemUserPassword(gpsSystemUserPassword); //设定gps密码
            //插入gps激活历史表
            gpsActiveHistoryRepository.insertOne(gpsActiveHistory);
        } else {
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","激活失败"+res.getError()),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, "","激活成功"),
                HttpStatus.OK);
    }

    /**
     * 分页返回激活历史列表
     * @param userId 用户id
     * @return
     */
    public ResponseEntity<RestResponse> getActiveList(String userId, int page, int size){
        // 获取新闻信息
        List<GpsActiveHistory> resultList =  gpsActiveHistoryRepository.selectActiveList(userId,page,size);
        if(resultList == null || resultList.isEmpty()){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","数据加载完毕"),
                    HttpStatus.OK);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, resultList,""),
                HttpStatus.OK);
    }
}
