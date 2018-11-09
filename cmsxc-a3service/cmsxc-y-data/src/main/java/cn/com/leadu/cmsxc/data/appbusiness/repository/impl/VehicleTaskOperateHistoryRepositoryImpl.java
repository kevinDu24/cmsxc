package cn.com.leadu.cmsxc.data.appbusiness.repository.impl;

import cn.com.leadu.cmsxc.data.appbusiness.dao.VehicleTaskOperateHistoryDao;
import cn.com.leadu.cmsxc.data.appbusiness.repository.VehicleTaskOperateHistoryRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskOperateHistory;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/17.
 *
 * 任务工单操作日志
 */
@Component
public class VehicleTaskOperateHistoryRepositoryImpl extends AbstractBaseRepository<VehicleTaskOperateHistoryDao,VehicleTaskOperateHistory> implements VehicleTaskOperateHistoryRepository {
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    @Override
    public List<VehicleTaskOperateHistory> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }
    /**
     * 登录车辆任务工单信息
     * @param vehicleTaskOperateHistory
     */
    public void insertOne(VehicleTaskOperateHistory vehicleTaskOperateHistory){
        super.insert(vehicleTaskOperateHistory);
    }
}
