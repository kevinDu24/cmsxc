package cn.com.leadu.cmsxc.assistant.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ParkingDataVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.springframework.http.ResponseEntity;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 入库管理Service
 */
public interface StorageManagerService {

    /**
     * 入库管理列表查询_库管/停车场公司管理员
     *
     * @param userId 用户名
     * @param userRole 角色code
     * @param status 状态 01：入库中，02：已结束
     * @param parkingId 所选停车场id
     * @param plate 车牌号
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    ResponseEntity<RestResponse> getParkingStorageList(String userId, String userRole, String parkingId, String status, String plate, int page, int size);


    /**
     * 获得停车场管理员下对应的停车场集合
     *
     * @param userId 用户名
     * @return
     */
    ResponseEntity<RestResponse> getParkingListAdmin(String userId);

    /**
     * 获取该任务最新入库状态
     *
     * @param storageId 用户名
     * @return
     */
    public ResponseEntity<RestResponse> getLatestState(String storageId);

    /**
     * 确认抵达停车场
     * @param storageId 入库id
     * @param loginUser 当前登录人信息
     * @return
     */
    ResponseEntity<RestResponse> arriveParking(String storageId, SystemUser loginUser);

    /**
     * 获取入库管理车辆详情
     * @param storageId 入库id
     * @return
     */
    ResponseEntity<RestResponse> getVehicleInfo(String storageId);

    /**
     * 停车场相关人员上传资料
     * @param vo 资料对象
     * @param loginUser 当前登录人信息
     * @return
     */
    ResponseEntity<RestResponse> uploadData(ParkingDataVo vo, SystemUser loginUser);

    /**
     * 获取已提交页面信息接口
     * @param storageId 入库id
     * @return
     */
    ResponseEntity<RestResponse> getSubmitedInfo(String storageId);

}
