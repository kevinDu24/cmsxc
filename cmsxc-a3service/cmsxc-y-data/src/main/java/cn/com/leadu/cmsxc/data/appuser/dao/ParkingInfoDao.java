package cn.com.leadu.cmsxc.data.appuser.dao;

import cn.com.leadu.cmsxc.data.base.config.BaseDao;
import cn.com.leadu.cmsxc.pojo.appuser.entity.ParkingInfo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.ParkingInfoSubVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 停车场信息Dao
 */
public interface ParkingInfoDao extends BaseDao<ParkingInfo> {

    /**
     * 获取停车场公司列表
     *
     * @param leaseId 委托公司id
     * @return
     */
    List<ParkingCompanyListVo> selectCompanyList(@Param("leaseId") String leaseId);

    /**
     * 获取停车场列表
     *
     * @param parkingAdminId 停车场公司管理员id
     * @return
     */
    List<ParkingListVo> selectParkingList(@Param("parkingAdminId") String parkingAdminId);

    /**
     * 获取停车场信息
     *
     * @param id 停车场id
     * @return
     */
    EditParkingInfoVo selectParkingInfo(@Param("id") String id);

    /**
     * 筛选停车场列表
     *
     * @param leaseId 委托公司id
     * @param province 省份
     * @param state 状态
     * @return
     */
    List<ParkingListVo> selectParkingResult(@Param("leaseId") String leaseId, @Param("province") String province,
                                                 @Param("state") String state);

    /**
     * 根据委托公司id获取停车场
     *
     * @param leaseId 委托公司id
     * @return
     */
    List<ParkingDistanceVo> selectParkingByLeaseId(@Param("leaseId") String leaseId);

    /**
     * 根据停车场id获取停车场相关信息
     *
     * @param id 停车场id
     * @return
     */
    ParkingInfoSubVo selectParkingById(@Param("id") String id);
}
