package cn.com.leadu.cmsxc.appuser.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appuser.entity.ClueInfo;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 我的线索Service
 */
public interface ClueService {
    /**
     * 根据用户id和是否命中标识分页查看线索信息
     *
     * @param userId 用户id
     * @param targetFlag 命中标志
     * @param page 当前页
     * @param size 每页个数
     * @return
     */
    ResponseEntity<RestResponse> getClueInformation(String userId, String targetFlag, int page, int size);
    /**
     * 根据用户id查找已命中未查看的线索数量（同一车牌号统计一条）
     *
     * @param userId 用户id
     * @param targetFlag 命中flag
     * @param checkFlag 查看flag
     * @return
     */
    ResponseEntity<RestResponse> getCountNotCheckCount(String userId, String targetFlag,String checkFlag );
    /**
     * 根据车牌号查看线索信息
     *
     * @param plate 车牌号
     * @param vehicleIdentifyNum 车架号
     * @return
     */
    public List<ClueInfo> selectByPlate(String plate, String vehicleIdentifyNum);
    /**
     * 根据车牌号和用户id，命中标识查看线索信息
     *
     * @param plate 车牌号
     * @param userId 用户id
     * @param targetFlag 是否命中标志0：未命中，1：命中
     * @return
     */
    List<ClueInfo> selectByUserIdAndPlateAndTargetFlag(String plate, String userId, String targetFlag);

    /**
     * 根据车牌号和用户id查看线索信息
     *
     * @param plate 车牌号
     * @param userId 用户id
     * @return
     */
    List<ClueInfo> selectByUserIdAndPlate(String plate, String userId);

    /**
     * 根据车架号和用户id查看线索信息
     *
     * @param vehicleIdentifyNum 车架号
     * @param userId 用户id
     * @return
     */
    public List<ClueInfo> selectByUserIdAndVehicleIdentifyNum(String vehicleIdentifyNum, String userId);

}
