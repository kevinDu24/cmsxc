package cn.com.leadu.cmsxc.data.appbusiness.repository.impl;

import cn.com.leadu.cmsxc.data.appbusiness.dao.VehicleTaskRecoveryDao;
import cn.com.leadu.cmsxc.data.appbusiness.repository.VehicleTaskRecoveryRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskRecovery;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 收车公司派单任务
 */
@Component
public class VehicleTaskRecoveryRepositoryImpl extends AbstractBaseRepository<VehicleTaskRecoveryDao,VehicleTaskRecovery> implements VehicleTaskRecoveryRepository {
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    @Override
    public List<VehicleTaskRecovery> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    @Override
    public VehicleTaskRecovery selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }
    /**
     * 登录收车公司派单任务表
     * @param vehicleTaskRecovery
     */
    public void insertOne(VehicleTaskRecovery vehicleTaskRecovery){
        super.insert(vehicleTaskRecovery);
    }
    /**
     * 根据主键更新表
     * @param vehicleTaskRecovery
     */
    @Override
    public void updateByPrimaryKey(VehicleTaskRecovery vehicleTaskRecovery) {
        super.updateByPrimaryKey(vehicleTaskRecovery);
    }
}
