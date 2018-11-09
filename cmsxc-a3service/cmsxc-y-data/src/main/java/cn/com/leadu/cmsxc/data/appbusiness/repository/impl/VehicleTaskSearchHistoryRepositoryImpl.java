package cn.com.leadu.cmsxc.data.appbusiness.repository.impl;

import cn.com.leadu.cmsxc.data.appbusiness.dao.VehicleTaskSearchHistoryDao;
import cn.com.leadu.cmsxc.data.appbusiness.repository.VehicleTaskSearchHistoryRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTask;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskSearchHistory;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/17.
 *
 * 任务查询历史
 */
@Component
public class VehicleTaskSearchHistoryRepositoryImpl extends AbstractBaseRepository<VehicleTaskSearchHistoryDao,VehicleTaskSearchHistory> implements VehicleTaskSearchHistoryRepository {
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    @Override
    public List<VehicleTaskSearchHistory> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }
    /**
     * 登录车辆任务工单信息
     * @param vehicleTaskSearchHistory
     */
    public void insertOne(VehicleTaskSearchHistory vehicleTaskSearchHistory){
        super.insert(vehicleTaskSearchHistory);
    }
    /**
     * 根据主键更新表
     * @param vehicleTaskSearchHistory
     */
    @Override
    public void updateByPrimaryKey(VehicleTaskSearchHistory vehicleTaskSearchHistory) {
        super.updateByPrimaryKey(vehicleTaskSearchHistory);
    }
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    public VehicleTaskSearchHistory selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }
}
