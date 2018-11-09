package cn.com.leadu.cmsxc.data.appbusiness.repository.impl;

import cn.com.leadu.cmsxc.data.appbusiness.dao.RecoveryTaskDao;
import cn.com.leadu.cmsxc.data.appbusiness.repository.RecoveryTaskRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskRecovery;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/2/12.
 *
 * 收车公司派单任务
 */
@Component
public class RecoveryTaskRepositoryImpl extends AbstractBaseRepository<RecoveryTaskDao,VehicleTaskRecovery> implements RecoveryTaskRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
   public List<VehicleTaskRecovery> selectByExampleList(Example example){
       return super.selectListByExample(example);
   }
    /**
     * 登录车辆任务工单信息
     *
     * @param vehicleTaskRecovery
     */
    public void insertOne(VehicleTaskRecovery vehicleTaskRecovery){
        super.insert(vehicleTaskRecovery);
    }
    /**
     * 根据主键更新表
     * @param vehicleTaskRecovery
     */
    public void updateByPrimaryKey(VehicleTaskRecovery vehicleTaskRecovery){
        super.updateByPrimaryKey(vehicleTaskRecovery);
    }
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    public VehicleTaskRecovery selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }

}
