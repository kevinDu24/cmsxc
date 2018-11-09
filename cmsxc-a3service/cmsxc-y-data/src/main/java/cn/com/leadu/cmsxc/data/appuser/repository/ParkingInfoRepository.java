package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appuser.entity.ParkingInfo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.ParkingInfoSubVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.*;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 停车场信息表Repository
 */
public interface ParkingInfoRepository {
    /**
     * 通过主键获取工单
     * @param id
     * @return
     */
    ParkingInfo selectByPrimaryKey(String id);
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<ParkingInfo> selectByExampleList(Example example);

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    ParkingInfo selectOneByExample(Example example);

    /**
     * 登录表
     * @param parkingInfo
     */
    public void insertOne(ParkingInfo parkingInfo);

    /**
     * 根据主键更新表
     * @param parkingInfo
     */
    void updateByPrimaryKey(ParkingInfo parkingInfo);

    /**
     * 批量插入数据
     * @param parkingInfos
     */
    void insertMore(List<ParkingInfo> parkingInfos);

    /**
     * 获取停车场公司列表
     * @return
     */
    List<ParkingCompanyListVo> selectCompanyList(String leaseId);

    /**
     * 获取停车场公司列表
     * @return
     */
    List<ParkingListVo> selectParkingList(String parkingAdminId);

    /**
     * 获取停车场信息
     * @return
     */
    EditParkingInfoVo selectParkingInfo(String id);

    /**
     * 筛选停车场
     * @return
     */
    List<ParkingListVo> selectParkingResult(String leaseId, String province,String state);

    /**
     * 根据委托公司id获取停车场
     * @return
     */
    List<ParkingDistanceVo> selectParkingbyLeaseId(String leaseId);

    /**
     * 根据停车场id获取停车场相关信息
     * @return
     */
    ParkingInfoSubVo selectParkingById(String leaseId);

}
