package cn.com.leadu.cmsxc.appuser.controller;

import cn.com.leadu.cmsxc.appuser.service.VehicleService;
import cn.com.leadu.cmsxc.appuser.validator.sysuser.vo.VehicleVo;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appuser.vo.TaskListVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import cn.com.leadu.cmsxc.pojo.appuser.vo.VehicleTaskVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by yuanzhenxia on 2018/1/17.
 *
 * 车辆相关信息Controller
 */
@RestController
@RequestMapping("vehicle")
public class VehicleController {
    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);
    @Autowired
    private VehicleService vehicleService;
    /**
     * 根据车牌号，查看车辆是否存在，并返回是否第一次查看
     *
     * @param systemUser 用户信息
     * @param taskListVo 参数信息
     * @return
     */
    @RequestMapping(value = "/checkSearchBefore", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> checkSearchBefore( @AuthUserInfo SystemUser systemUser, @RequestBody TaskListVo taskListVo) {
        try{
            return vehicleService.checkSearchBefore(systemUser.getUserId(), taskListVo.getPlate(),taskListVo.getTaskStatus());
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("车辆是否存在error",ex);
            throw new CmsServiceException("查询失败");
        }
    }

    /**
     * 判断当前数据工单状态是否更改为“正常”之外的情况
     *
     * @param plate 车牌号码
     * @return
     */
    @RequestMapping(value = "/checkTaskStatus", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> checkTaskStatus(@RequestParam String plate) {
        try{
            return vehicleService.checkTaskStatus(plate);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("车辆是否存在error",ex);
            throw new CmsServiceException("查询失败");
        }
    }

    /**
     * 根据车牌号，查看当前用户是否查看过此车辆，判断是否需要扣除积分
     *
     * @param systemUser 用户信息
     * @param taskListVo 参数信息
     * @return
     */
    @RequestMapping(value = "/checkSearchBeforeForList", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> checkSearchBeforeForList(@AuthUserInfo SystemUser systemUser, @RequestBody TaskListVo taskListVo) {
        try{
            return vehicleService.checkSearchBeforeForList(systemUser.getUserId(), taskListVo.getPlate(), taskListVo.getTaskStatus());
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("车辆是否存在error",ex);
            throw new CmsServiceException("查询失败");
        }
    }
    /**
     * 取工单状态为正常的车辆任务工单数量
     *
     * @return
     */
    @RequestMapping(value = "/getNormalCount", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getNormalCount() {
        try{
            return vehicleService.getNormalCount();
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("取工单状态为正常的车辆任务工单数量error",ex);
            throw new CmsServiceException("查询失败");
        }
    }
    /**
     * 根据价格区间，gps有无，线索有无，违章有无等条件，搜索车辆信息
     *
     * @param vehicleTaskVo 画面参数信息
     * @param systemUser 用户信息
     * @return
     */
    @RequestMapping(value = "/getVehicleTaskList", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> getVehicleTaskList(@AuthUserInfo SystemUser systemUser, @RequestBody VehicleTaskVo vehicleTaskVo){
        try{
            return vehicleService.getVehicleTaskList(systemUser.getUserId(), vehicleTaskVo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("根据价格区间，gps有无，线索有无，违章有无等条件，搜索车辆信息error",ex);
            throw new CmsServiceException("查询失败");
        }
    }

    /**
     * 根据车牌号，搜索车辆信息
     *
     * @param systemUser 用户信息
     * @param vehicleVo 画面参数信息
     * @return
     */
    @RequestMapping(value = "/searchVehicleDetail", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> searchVehicleDetail(@AuthUserInfo SystemUser systemUser, @RequestBody(required = false) VehicleVo vehicleVo) {
        try{
            return vehicleService.searchVehicleDetail(systemUser.getUserId(), vehicleVo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("搜索车辆信息error",ex);
            throw new CmsServiceException("搜索车辆信息失败");
        }
    }
    /**
     * 用户查询历史
     *
     * @param systemUser 用户信息
     * @return
     */
    @RequestMapping(value = "/userSearchHistory", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> userSearchHistory(@AuthUserInfo SystemUser systemUser,
                                                          @RequestParam(required = true) int page, @RequestParam(required = true) int size){
        try{
            return vehicleService.userSearchHistory(systemUser.getUserId(), page, size);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("查看用户查询历史error",ex);
            throw new CmsServiceException("查询失败");
        }
    }

    /**
     * 判断积分是否充足
     *
     * @param systemUser 用户信息
     * @return
     */
    @RequestMapping(value = "/searchUserScore", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> searchUserScore(@AuthUserInfo SystemUser systemUser){
        try{
            return vehicleService.searchUserScore(systemUser.getUserId());
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("判断积分是否充足error",ex);
            throw new CmsServiceException("判断积分是否充足失败");
        }
    }
}
