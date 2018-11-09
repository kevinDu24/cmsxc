package cn.com.leadu.cmsxc.assistant.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.assistant.vo.EditParkingCompanyVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.EditParkingInfoVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.springframework.http.ResponseEntity;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 停车场管理管理Service
 */
public interface ParkingManagerService {


    /**
     * 获取停车场公司列表
     * @param leaseId 用户id
     * @return
     */
    ResponseEntity<RestResponse> getParkingCompanyList(String leaseId);

    /**
     * 新增停车场公司
     * @param vo 停车场公司信息参数
     * @return
     */
    ResponseEntity<RestResponse> addParkingCompany(EditParkingCompanyVo vo, SystemUser loginUser);

    /**
     * 编辑停车场公司
     * @param vo 停车场公司信息参数
     * @return
     */
    ResponseEntity<RestResponse> editParkingCompany(EditParkingCompanyVo vo, SystemUser loginUser);

    /**
     * 通过停车场公司管理员用户名获取停车场列表
     * @param parkingAdminId 停车场公司管理员用户名
     * @return
     */
    ResponseEntity<RestResponse> getParkingList(String parkingAdminId);

    /**
     * 新增停车场
     * @param vo 停车场信息参数
     * @return
     */
    ResponseEntity<RestResponse> addParkingInfo(EditParkingInfoVo vo, SystemUser loginUser);

    /**
     * 通过停车场主键获取停车场信息
     * @param id 停车场信息表id
     * @return
     */
    ResponseEntity<RestResponse> getParkingInfo(String id);

    /**
     * 编辑停车场
     * @param vo 停车场信息参数
     * @return
     */
    ResponseEntity<RestResponse> editParkingInfo(EditParkingInfoVo vo, SystemUser loginUser);

    /**
     * 筛选停车场
     * @param leaseId 委托公司id
     * @return
     */
    ResponseEntity<RestResponse> searchParking(String leaseId, String province,String state);

}
