package cn.com.leadu.cmsxc.assistant.service.impl;

import cn.com.leadu.cmsxc.assistant.service.ParkingManagerService;
import cn.com.leadu.cmsxc.assistant.service.SystemUserService;
import cn.com.leadu.cmsxc.assistant.util.constant.EncodeUtils;
import cn.com.leadu.cmsxc.common.constant.Constants;
import cn.com.leadu.cmsxc.common.constant.enums.EnableFlagEnum;
import cn.com.leadu.cmsxc.common.constant.enums.UserRoleEnums;
import cn.com.leadu.cmsxc.common.util.ArrayUtil;
import cn.com.leadu.cmsxc.data.appuser.repository.ParkingInfoRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.ParkingLeaseRelationRepository;
import cn.com.leadu.cmsxc.data.system.repository.SysUserRoleRepository;
import cn.com.leadu.cmsxc.data.system.repository.SystemUserRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appuser.entity.ParkingInfo;
import cn.com.leadu.cmsxc.pojo.appuser.entity.ParkingLeaseRelation;
import cn.com.leadu.cmsxc.pojo.assistant.vo.EditParkingCompanyVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.EditParkingInfoVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ParkingCompanyListVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ParkingListVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SysUserRole;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 角色管理Service实现类
 */
@Service
public class ParkingManagerServiceImpl implements ParkingManagerService {
    private static final Logger logger = LoggerFactory.getLogger(ParkingManagerServiceImpl.class);

    @Autowired
    private SystemUserRepository systemUserRepository;
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;
    @Autowired
    private ParkingInfoRepository parkingInfoRepository;
    @Autowired
    private ParkingLeaseRelationRepository parkingLeaseRelationRepository;


    /**
     * 获取停车场公司列表
     * @param leaseId 用户id
     * @return
     */
    public ResponseEntity<RestResponse> getParkingCompanyList(String leaseId){
        List<ParkingCompanyListVo> resultList =  parkingInfoRepository.selectCompanyList(leaseId);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, resultList,""),
                HttpStatus.OK);
    }


    /**
     * 新增停车场公司
     * @param vo 停车场公司信息参数
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> addParkingCompany(EditParkingCompanyVo vo, SystemUser loginUser){
        SystemUser systemUser = systemUserService.selectSystemUserByUserId(vo.getUserId());
        //验证用户名是否存在
        if(systemUser != null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该用户已存在"),
                    HttpStatus.OK);
        }
        //验证公司名称是否存在
        Example example = new Example(SystemUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userName", vo.getUserName());
        criteria.andEqualTo("userRole", UserRoleEnums.PARKING_ADMIN.getType());
        if(ArrayUtil.isNotNullAndLengthNotZero(systemUserRepository.selectByExampleList(example))){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该名称已存在"),
                    HttpStatus.OK);
        }
        SystemUser user = new SystemUser();
        Date now = new Date();
        //登录用户表
        user.setUserId(vo.getUserId()); //用户名
        user.setUserPassword(EncodeUtils.MD5(EncodeUtils.getBase64(Constants.ORIGIN_PASSWORD))); //初始密码进行加密
        user.setUserRole(UserRoleEnums.PARKING_ADMIN.getType()); //角色code
        user.setUserName(vo.getUserName()); //用户姓名
        user.setUserPhone(vo.getUserId()); //用户手机号
        user.setEnableFlag(vo.getEnableFlag()); //禁用/启用状态
        user.setLeaseId(loginUser.getLeaseId()); //委托公司id
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setCreator(loginUser.getUserId());
        user.setUpdater(loginUser.getUserId());
        //登录用户角色表
        user = systemUserRepository.insertOne(user);
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(user.getId());
        sysUserRole.setRoleId(user.getUserRole());
        sysUserRoleRepository.insertOne(sysUserRole);
        //登录停车场公司与委托公司关系表
        ParkingLeaseRelation parkingLeaseRelation = new ParkingLeaseRelation();
        parkingLeaseRelation.setLeaseId(loginUser.getLeaseId());
        parkingLeaseRelation.setParkingAdminId(user.getId());
        parkingLeaseRelationRepository.insertOne(parkingLeaseRelation);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, "","添加成功"),
                HttpStatus.OK);
    }

    /**
     * 编辑停车场公司
     * @param vo 停车场公司信息参数
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> editParkingCompany(EditParkingCompanyVo vo, SystemUser loginUser){
        SystemUser user = systemUserService.selectSystemUserByUserId(vo.getUserId());
        if(user == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该用户不存在"),
                    HttpStatus.OK);
        }
        Date now = new Date();
        //更新用户表
        user.setUserName(vo.getUserName()); //公司名称
        user.setEnableFlag(vo.getEnableFlag()); //禁用/启用状态
        // TODO: 2018/8/3  如果启用变禁用，要判断该停车场公司下是否有待交接的单子；如果有，则提示他联系委托公司更改停车场
        user.setUpdateTime(now);
        user.setUpdater(loginUser.getUserId());
        systemUserRepository.updateByPrimaryKey(user);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, "","编辑成功"),
                HttpStatus.OK);
    }

    /**
     * 通过停车场公司管理员用户名获取停车场列表
     * @param parkingAdminId 停车场公司管理员用户名
     * @return
     */
    public ResponseEntity<RestResponse> getParkingList(String parkingAdminId){
        //通过停车场id获取停车场信息
        List<ParkingListVo> resultList = parkingInfoRepository.selectParkingList(parkingAdminId);

        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, resultList,""),
                HttpStatus.OK);
    }

    /**
     * 新增停车场
     * @param vo 停车场信息参数
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> addParkingInfo(EditParkingInfoVo vo, SystemUser loginUser){
        SystemUser parkingAdmin = systemUserService.selectSystemUserByUserId(vo.getParkingAdminId());
        if(parkingAdmin == null || EnableFlagEnum.OFF.getCode().equals(parkingAdmin.getEnableFlag())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该停车场所属停车场公司已被禁用，不可添加停车场"),
                    HttpStatus.OK);
        }
        //判断该库管用户名是否被注册
        SystemUser parkingManager = systemUserService.selectSystemUserByUserId(vo.getParkingManager());
        if(parkingManager != null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该停车场账号已被注册"),
                    HttpStatus.OK);
        }
        //没有被注册，注册
        Date now = new Date();
        parkingManager = new SystemUser();
        //登录用户表
        parkingManager.setUserId(vo.getParkingManager()); //用户名
        parkingManager.setUserPassword(EncodeUtils.MD5(EncodeUtils.getBase64(Constants.ORIGIN_PASSWORD))); //初始密码进行加密
        parkingManager.setUserRole(UserRoleEnums.PARKING_MANAGER.getType()); //角色code
        parkingManager.setUserName(vo.getParkingManagerName()); //用户姓名
        parkingManager.setUserPhone(vo.getParkingManager()); //用户手机号
        parkingManager.setEnableFlag(vo.getState()); //禁用/启用状态
        parkingManager.setLeaseId(loginUser.getLeaseId()); //委托公司id
        parkingManager.setCreateTime(now);
        parkingManager.setUpdateTime(now);
        parkingManager.setCreator(loginUser.getUserId());
        parkingManager.setUpdater(loginUser.getUserId());
        //登录用户角色表
        parkingManager = systemUserRepository.insertOne(parkingManager);
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(parkingManager.getId());
        sysUserRole.setRoleId(parkingManager.getUserRole());
        sysUserRoleRepository.insertOne(sysUserRole);
        //登录停车场信息表
        ParkingInfo parkingInfo = buildParking(null, vo, loginUser);
        parkingInfoRepository.insertOne(parkingInfo);

        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, "","添加成功"),
                HttpStatus.OK);
    }

    /**
     * 通过停车场主键获取停车场信息
     * @param id 停车场信息表id
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> getParkingInfo(String id){
        //通过停车场id获取停车场信息
        EditParkingInfoVo vo = parkingInfoRepository.selectParkingInfo(id);

        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, vo,""),
                HttpStatus.OK);
    }

    /**
     * 编辑停车场
     * @param vo 停车场信息参数
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> editParkingInfo(EditParkingInfoVo vo, SystemUser loginUser){
        SystemUser parkingAdmin = systemUserService.selectSystemUserByUserId(vo.getParkingAdminId());
        if(parkingAdmin == null || EnableFlagEnum.OFF.getCode().equals(parkingAdmin.getEnableFlag())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该停车场所属停车场公司已被禁用，不可编辑停车场"),
                    HttpStatus.OK);
        }
        //// TODO: 2018/8/3  如果启用变禁用，要判断该停车场下是否有待交接的单子；如果有，则提示他联系委托公司更改停车场
        //通过停车场id获取停车场信息
        ParkingInfo parkingInfo = parkingInfoRepository.selectByPrimaryKey(vo.getId());
        //更新停车场信息表
        parkingInfo = buildParking(parkingInfo, vo, loginUser);
        parkingInfoRepository.updateByPrimaryKey(parkingInfo);
        //更新用户表的库管姓名
        SystemUser parkingManager = systemUserService.selectSystemUserByUserId(vo.getParkingManager());
        // 如果库管姓名被改动，则更新用户表
        if(!parkingManager.getUserName().equals(vo.getParkingManagerName())){
            parkingManager.setUserName(vo.getParkingManagerName()); //库管姓名
            systemUserRepository.updateByPrimaryKey(parkingManager);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, "","编辑成功"),
                HttpStatus.OK);
    }

    /**
     * 构建停车场信息
     * @param parkingInfo
     * @param vo
     * @param loginUser
     * @return
     */
    private ParkingInfo buildParking(ParkingInfo parkingInfo, EditParkingInfoVo vo, SystemUser loginUser) {
        Date now = new Date();
        //新增停车场
        if(parkingInfo == null){
            parkingInfo = new ParkingInfo();
            parkingInfo.setParkingManager(vo.getParkingManager()); //库管用户名
            parkingInfo.setCreateTime(now); //创建时间
            parkingInfo.setCreator(loginUser.getUserId()); //创建人
            parkingInfo.setParkingManager(vo.getParkingManager()); //库管用户名
            parkingInfo.setParkingAdminId(vo.getParkingAdminId()); //停车场公司管理员用户名
        }
        parkingInfo.setParkingName(vo.getParkingName()); //停车场名称
        parkingInfo.setState(vo.getState()); //启用/禁用
        parkingInfo.setAddress(vo.getAddress()); //停车场地址
        parkingInfo.setProvince(vo.getProvince()); //省
        parkingInfo.setCity(vo.getCity()); //市
        parkingInfo.setLat(vo.getLat()); //纬度
        parkingInfo.setLon(vo.getLon()); //经度
        parkingInfo.setOpenTime(vo.getOpenTime()); //营业开始时间
        parkingInfo.setCloseTime(vo.getCloseTime()); //营业结束时间
        parkingInfo.setSize(vo.getSize()); //长宽高
        parkingInfo.setType(vo.getType()); //停车场类型
        parkingInfo.setPlateCarFlag(vo.getPlateCarFlag()); //板车是否可入
        parkingInfo.setTruckFlag(vo.getTruckFlag()); //货车是否可入
        parkingInfo.setMaxPlaceNum(vo.getMaxPlaceNum()); //最大容纳车辆数量
        parkingInfo.setUpdater(loginUser.getUserId());
        parkingInfo.setUpdateTime(now);
        return parkingInfo;
    }

    /**
     * 筛选停车场
     * @param leaseId 委托公司id
     * @return
     */
    public ResponseEntity<RestResponse> searchParking(String leaseId, String province,String state){
        List<ParkingListVo> resultList =  parkingInfoRepository.selectParkingResult(leaseId,province,state);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, resultList,""),
                HttpStatus.OK);
    }

}
