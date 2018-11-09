package cn.com.leadu.cmsxc.assistant.controller;

import cn.com.leadu.cmsxc.assistant.service.ParkingManagerService;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.assistant.vo.EditParkingCompanyVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.EditParkingInfoVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 角色管理Controller
 */
@RestController
@RequestMapping("parkingManager")
public class ParkingManagerController {
    private static final Logger logger = LoggerFactory.getLogger(ParkingManagerController.class);
    @Autowired
    private ParkingManagerService parkingManagerService;

    /**
     * 获取停车场公司列表
     * @param systemUser 用户信息
     * @return
     */
    @RequestMapping(value = "/getParkingCompanyList", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getParkingCompanyList(@AuthUserInfo SystemUser systemUser) {
        try{
            return parkingManagerService.getParkingCompanyList(systemUser.getLeaseId());
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取停车场公司列表error",ex);
            throw new CmsServiceException("获取停车场公司列表失败");
        }
    }

    /**
     * 新增停车场公司
     * @param vo 停车场公司信息参数
     * @return
     */
    @RequestMapping(value = "/addParkingCompany", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> addParkingCompany(@AuthUserInfo SystemUser systemUser, @RequestBody EditParkingCompanyVo vo) {
        try{
            return parkingManagerService.addParkingCompany(vo,systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("新增停车场公司error",ex);
            throw new CmsServiceException("新增停车场公司失败");
        }
    }

    /**
     * 编辑停车场公司
     * @param vo 停车场公司信息参数
     * @return
     */
    @RequestMapping(value = "/editParkingCompany", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> editParkingCompany(@AuthUserInfo SystemUser systemUser, @RequestBody EditParkingCompanyVo vo) {
        try{
            return parkingManagerService.editParkingCompany(vo,systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("编辑停车场公司error",ex);
            throw new CmsServiceException("编辑停车场公司失败");
        }
    }

    /**
     * 通过停车场公司管理员用户名获取停车场列表
     * @param parkingAdminId 停车场公司管理员id
     * @return
     */
    @RequestMapping(value = "/getParkingList", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getParkingList(String parkingAdminId) {
        try{
            return parkingManagerService.getParkingList(parkingAdminId);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("通过停车场公司管理员用户名获取停车场列表error",ex);
            throw new CmsServiceException("通过停车场公司管理员用户名获取停车场列表失败");
        }
    }

    /**
     * 新增停车场
     * @param vo 停车场信息参数
     * @return
     */
    @RequestMapping(value = "/addParkingInfo", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> addParkingInfo(@AuthUserInfo SystemUser systemUser, @RequestBody EditParkingInfoVo vo) {
        try{
            return parkingManagerService.addParkingInfo(vo,systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("新增停车场error",ex);
            throw new CmsServiceException("新增停车场失败");
        }
    }

    /**
     * 通过停车场主键获取停车场信息
     * @param id 停车场信息表id
     * @return
     */
    @RequestMapping(value = "/getParkingInfo", method = RequestMethod.GET)
    ResponseEntity<RestResponse> getParkingInfo(String id){
        try{
            return parkingManagerService.getParkingInfo(id);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("通过停车场主键获取停车场信息error",ex);
            throw new CmsServiceException("通过停车场主键获取停车场信息失败");
        }
    }

    /**
     * 编辑停车场
     * @param vo 停车场信息参数
     * @return
     */
    @RequestMapping(value = "/editParkingInfo", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> editParkingInfo(@AuthUserInfo SystemUser systemUser, @RequestBody EditParkingInfoVo vo) {
        try{
            return parkingManagerService.editParkingInfo(vo,systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("编辑停车场error",ex);
            throw new CmsServiceException("编辑停车场失败");
        }
    }

    /**
     * 筛选停车场
     * @param province 停车场信息表id
     * @param state 状态
     * @return
     */
    @RequestMapping(value = "/searchParking", method = RequestMethod.GET)
    ResponseEntity<RestResponse> searchParking(@AuthUserInfo SystemUser systemUser,String province,String state){
        try{
            return parkingManagerService.searchParking(systemUser.getLeaseId(), province, state);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("筛选停车场error",ex);
            throw new CmsServiceException("筛选停车场失败");
        }
    }
}
