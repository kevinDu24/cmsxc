package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.data.appuser.dao.StorageInfoDao;
import cn.com.leadu.cmsxc.data.appuser.repository.StorageInfoRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.StorageInfo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.CommissionSettlementListVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ParkingDdlVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.StorageListVo;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 停车场信息RepositoryImpl
 */
@Component
public class StorageInfoRepositoryImpl extends AbstractBaseRepository<StorageInfoDao,StorageInfo> implements StorageInfoRepository {
    /**
     * 通过主键获取工单
     * @param id
     * @return
     */
    public StorageInfo selectByPrimaryKey(String id){
        return super.selectByPrimaryKey(id);
    }

    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    public List<StorageInfo> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    public StorageInfo selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }

    /**
     * 登录表
     * @param storageInfo
     */
    public StorageInfo insertOne(StorageInfo storageInfo){
        return super.insert(storageInfo);
    }

    /**
     * 根据主键更新表
     * @param storageInfo
     */
    public void updateByPrimaryKey(StorageInfo storageInfo) {
        super.updateByPrimaryKey(storageInfo);
    }

    /**
     * 批量插入数据
     * @param storageInfos
     */
    public void insertMore(List<StorageInfo> storageInfos){
        super.insertListByMapper(storageInfos);
    }
    /**
     * 佣金结算列表数据
     *
     * @param recoveryCompanyId 收车公司id
     * @param status 申请状态 01：待申请，02：审核中，03:被退回，04:已通过
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    public List<CommissionSettlementListVo> selectCommissionSettlementList(String recoveryCompanyId, String status, String plate, int page, int size){
        return baseDao.selectCommissionSettlementList(recoveryCompanyId,status,plate,page,size);
    }
    /**
     * 佣金结算列表数量
     *
     * @param recoveryCompanyId 收车公司id
     * @param status 状态
     * @param plate 车牌号
     * @return
     */
    public Integer selectCommissionSettlementCount(String recoveryCompanyId, String status, String plate){
        return baseDao.selectCommissionSettlementCount(recoveryCompanyId,status,plate);
    }

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
    public List<StorageListVo> selectParkingStorageList(String userId, String userRole, String parkingId, String status, String plate, int page, int size){
        return baseDao.selectParkingStorageList(userId,userRole,parkingId,status,plate,page,size);
    }

    /**
     * 入库管理列表查询_库管/停车场公司管理员
     *
     * @param userId 用户名
     * @return
     */
    public List<ParkingDdlVo> selectParkingDdlAdmin(String userId){
        return baseDao.selectParkingDdlAdmin(userId);
    }
}
