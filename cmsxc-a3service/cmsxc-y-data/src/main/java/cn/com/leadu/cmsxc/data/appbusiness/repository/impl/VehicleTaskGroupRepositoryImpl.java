package cn.com.leadu.cmsxc.data.appbusiness.repository.impl;

import cn.com.leadu.cmsxc.data.appbusiness.dao.VehicleTaskGroupDao;
import cn.com.leadu.cmsxc.data.appbusiness.repository.VehicleTaskGroupRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskGroup;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/9.
 *
 * 小组任务表RepositoryImpl
 */
@Component
public class VehicleTaskGroupRepositoryImpl extends AbstractBaseRepository<VehicleTaskGroupDao,VehicleTaskGroup> implements VehicleTaskGroupRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    public List<VehicleTaskGroup> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }
    /**
     * 登录分组表信息
     *
     * @param vehicleTaskGroup
     */
    public void insertOne(VehicleTaskGroup vehicleTaskGroup){
        super.insert(vehicleTaskGroup);
    }
    /**
     * 根据主键更新表
     * @param vehicleTaskGroup
     */
    public void updateByPrimaryKey(VehicleTaskGroup vehicleTaskGroup){
        super.updateByPrimaryKey(vehicleTaskGroup);
    }
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    public VehicleTaskGroup selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }
    /**
     * 批量插入数据
     * @param vehicleTaskGroups
     */
    public void insertMore(List<VehicleTaskGroup> vehicleTaskGroups){
        super.insertListByMapper(vehicleTaskGroups);
    }
    /**
     * 删除数据
     *
     * @param id
     */
    public void delete(Object id){
        super.delete(id);
    }
    /**
     * 根据分组id删除分组任务
     *
     * @param groupId 分组id
     */
    public void deleteByGroupId(String groupId){
        baseDao.deleteByGroupId(groupId);
    }
    /**
     * 根据分组id删和任務id除分组任务
     *
     * @param groupId 分组id
     * @param vehicleTaskRecoveryId 任務id
     */
    public void deleteByGroupIdAndVehicleTaskRecoveryId(String groupId, String vehicleTaskRecoveryId){
        baseDao.deleteByGroupIdAndVehicleTaskRecoveryId(groupId,vehicleTaskRecoveryId);
    }
    /**
     * 根据车牌号，获取任务分配小组id
     *
     * @param plate 车牌号
     */
    public List<String> selectGroupIdByPlate(String plate){
       return baseDao.selectGroupIdByPlate(plate);
    }
}
