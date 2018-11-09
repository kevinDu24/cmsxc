package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appuser.entity.StorageInfo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.CommissionSettlementListVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ParkingDdlVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.StorageListVo;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 入库信息表Repository
 */
public interface StorageInfoRepository {
    /**
     * 通过主键获取
     * @param id
     * @return
     */
    StorageInfo selectByPrimaryKey(String id);
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<StorageInfo> selectByExampleList(Example example);

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    StorageInfo selectOneByExample(Example example);

    /**
     * 登录表
     * @param storageInfos
     */
    public StorageInfo insertOne(StorageInfo storageInfos);

    /**
     * 根据主键更新表
     * @param storageInfo
     */
    void updateByPrimaryKey(StorageInfo storageInfo);

    /**
     * 批量插入数据
     * @param storageInfos
     */
    void insertMore(List<StorageInfo> storageInfos);

    /**
     * 佣金结算列表数据
     *
     * @param recoveryCompanyId 收车公司id
     * @param status 申请状态 01：待申请，02：审核中，03:被退回，04:已通过
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    List<CommissionSettlementListVo> selectCommissionSettlementList(String recoveryCompanyId, String status, String plate, int page, int size);

    /**
     * 佣金结算列表数量
     *
     * @param recoveryCompanyId 收车公司id
     * @param status 状态
     * @param plate 车牌号
     * @return
     */
    Integer selectCommissionSettlementCount(String recoveryCompanyId, String status, String plate);

    /**
     * 入库管理列表查询_库管/停车场公司管理员
     *
     * @param userId 用户名
     * @param status 状态 01：入库中，02：已结束
     * @param userRole 角色code
     * @param parkingId 所选停车场id
     * @param plate 车牌号
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    List<StorageListVo> selectParkingStorageList(String userId, String userRole, String parkingId, String status, String plate, int page, int size);

    /**
     * 入库管理列表查询_库管/停车场公司管理员
     *
     * @param userId 用户名
     * @return
     */
    List<ParkingDdlVo> selectParkingDdlAdmin(String userId);
}
