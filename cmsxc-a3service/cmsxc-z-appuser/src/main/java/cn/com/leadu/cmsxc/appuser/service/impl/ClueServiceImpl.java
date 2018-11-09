package cn.com.leadu.cmsxc.appuser.service.impl;

import cn.com.leadu.cmsxc.appuser.service.ClueService;
import cn.com.leadu.cmsxc.appuser.util.constant.enums.TargetFlag;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.appuser.repository.ClueInforRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appuser.entity.ClueInfo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.ClueVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 我的线索Service实现类
 */
@Service
public class ClueServiceImpl implements ClueService{
    private static final Logger logger = LoggerFactory.getLogger(ClueServiceImpl.class);
    @Autowired
    private ClueInforRepository clueInforRepository;
    /**
     * 根据用户id和是否命中标识分页查看线索信息
     *
     * @param userId 用户id
     * @param targetFlag 命中标志
     * @param page 当前页
     * @param size 每页个数
     * @return
     */
   public ResponseEntity<RestResponse> getClueInformation(String userId, String targetFlag, int page, int size){
       // 获得当前用户的命中或未命中的线索信息
       // 返回用
       List<ClueVo> clueVoList = new ArrayList();
       if(TargetFlag.TARGET.getType().equals(targetFlag)){
           // 已命中时，根据用户id和命中状态查看正常的
           clueVoList = clueInforRepository.selectByUserIdOnTarget(userId, targetFlag, page, size);
       } else if(TargetFlag.NOTTARGET.getType().equals(targetFlag)){
           // 未命中时，根据用户id和命中状态查找线索表
           clueVoList = clueInforRepository.selectByUserIdOutOfTarget(userId, targetFlag, page, size);
       }
       if(clueVoList != null && !clueVoList.isEmpty()){
           return new ResponseEntity<RestResponse>(
                   RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,clueVoList,""),
                   HttpStatus.OK);
       }
       return new ResponseEntity<RestResponse>(
               RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","数据加载完毕"),
               HttpStatus.OK);
    }
    /**
     * 根据用户id查找已命中未查看的线索数量（同一车牌号统计一条）
     *
     * @param userId 用户id
     * @param targetFlag 命中flag
     * @param checkFlag 查看flag
     * @return
     */
    public ResponseEntity<RestResponse> getCountNotCheckCount(String userId, String targetFlag,String checkFlag ){
        // 判断当前登录手机号是否为空
        if(StringUtil.isNull(userId)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","当前登录用户手机号为空"),
                    HttpStatus.OK);
        }
        // 已命中未查看的线索数量
        int count = clueInforRepository.selectCountOfNotCheck(userId, targetFlag, checkFlag );
        // 返回用
        Map<String,String> countMap = new HashMap<>();
        countMap.put("notCheckcount",String.valueOf(count));
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,countMap,""),
                HttpStatus.OK);
    }

    /**
     * 根据车牌号查看线索信息
     *
     * @param plate 车牌号
     * @param vehicleIdentifyNum 车架号
     * @return
     */
    public List<ClueInfo> selectByPlate(String plate, String vehicleIdentifyNum){
        if(StringUtil.isNotNull(plate) && StringUtil.isNotNull(vehicleIdentifyNum)) {
            Example example = new Example(ClueInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("plate", plate);
            criteria.orEqualTo("vehicleIdentifyNum",vehicleIdentifyNum);
            example.setOrderByClause(" upload_date desc ");
            List<ClueInfo> clueInfoList = clueInforRepository.selectByExampleList(example);
            return clueInfoList;
        }
        return null;
    }
    /**
     * 根据车牌号和用户id，命中标识查看线索信息
     *
     * @param plate 车牌号
     * @param userId 用户id
     * @param targetFlag 是否命中标志0：未命中，1：命中
     * @return
     */
    public List<ClueInfo> selectByUserIdAndPlateAndTargetFlag(String plate, String userId, String targetFlag){
        if(StringUtil.isNotNull(plate)&& StringUtil.isNotNull(userId)&& StringUtil.isNotNull(targetFlag)) {
            Example example = new Example(ClueInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("plate", plate);
            criteria.andEqualTo("userId", userId);
            criteria.andEqualTo("targetFlag", targetFlag);
            List<ClueInfo> clueInfoList = clueInforRepository.selectByExampleList(example);
            return clueInfoList;
        }
        return null;
    }
    /**
     * 根据车架号和用户id查看线索信息
     *
     * @param vehicleIdentifyNum 车架号
     * @param userId 用户id
     * @return
     */
    public List<ClueInfo> selectByUserIdAndVehicleIdentifyNum(String vehicleIdentifyNum, String userId){
        if(StringUtil.isNotNull(vehicleIdentifyNum)&& StringUtil.isNotNull(userId)) {
            Example example = new Example(ClueInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("vehicleIdentifyNum", vehicleIdentifyNum);
            criteria.andEqualTo("userId", userId);
            List<ClueInfo> clueInfoList = clueInforRepository.selectByExampleList(example);
            return clueInfoList;
        }
        return null;
    }

    /**
     * 根据车牌号和用户id查看线索信息
     *
     * @param plate 车牌号
     * @param userId 用户id
     * @return
     */
    public List<ClueInfo> selectByUserIdAndPlate(String plate, String userId){
        if(StringUtil.isNotNull(plate)&& StringUtil.isNotNull(userId)) {
            Example example = new Example(ClueInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("plate", plate);
            criteria.andEqualTo("userId", userId);
            List<ClueInfo> clueInfoList = clueInforRepository.selectByExampleList(example);
            return clueInfoList;
        }
        return null;
    }
}
