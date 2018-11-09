package cn.com.leadu.cmsxc.data.appuser.dao;

import cn.com.leadu.cmsxc.data.base.config.BaseDao;
import cn.com.leadu.cmsxc.pojo.appuser.entity.StorageInfo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.CommissionSettlementListVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ParkingDdlVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.StorageListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 入库信息Dao
 */
public interface StorageInfoDao extends BaseDao<StorageInfo> {
    /**
     * 佣金结算列表数据
     *
     * @param recoveryCompanyId 收车公司id
     * @param status 申请状态 01：待申请，02：审核中，03:被退回，04:已通过
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    List<CommissionSettlementListVo> selectCommissionSettlementList( @Param("recoveryCompanyId") String recoveryCompanyId,
                                                            @Param("status") String status, @Param("plate") String plate, @Param("page") int page, @Param("size") int size);

    /**
     * 佣金结算列表数量
     *
     * @param recoveryCompanyId 收车公司id
     * @param status 状态
     * @param plate 车牌号
     * @return
     */
    Integer selectCommissionSettlementCount(@Param("recoveryCompanyId") String recoveryCompanyId,
                                            @Param("status") String status, @Param("plate") String plate);

    /**
     * 入库管理列表查询_库管/停车场公司管理员
     *
     * @param userId 用户名
     * @param status 状态
     * @param plate 车牌号
     * @return
     */
    List<StorageListVo> selectParkingStorageList(@Param("userId") String userId, @Param("userRole") String userRole, @Param("parkingId") String parkingId,
                                                 @Param("status") String status, @Param("plate") String plate, @Param("page") int page, @Param("size") int size);

    /**
     * 获取停车场管理员入库管理页面停车场下拉框数据
     *
     * @param userId 用户名
     * @return
     */
    List<ParkingDdlVo> selectParkingDdlAdmin(@Param("userId") String userId);
}
