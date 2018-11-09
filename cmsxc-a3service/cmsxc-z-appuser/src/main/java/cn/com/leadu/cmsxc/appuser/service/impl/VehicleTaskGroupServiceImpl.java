package cn.com.leadu.cmsxc.appuser.service.impl;

import cn.com.leadu.cmsxc.appuser.service.VehicleTaskGroupService;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.appbusiness.repository.VehicleTaskGroupRepository;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/12.
 *
 * 小组任务表
 */
@Service
public class VehicleTaskGroupServiceImpl implements VehicleTaskGroupService{
    @Autowired
    private VehicleTaskGroupRepository vehicleTaskGroupRepository;
    /**
     * 根据分组id获取所有小组任务
     *
     * @param groupId 分组id
     * @return
     */
    public List<VehicleTaskGroup> getVehicleTaskGroupByGroupId(String groupId){
        if(StringUtil.isNotNull(groupId)) {
            Example example = new Example(VehicleTaskGroup.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("groupId", groupId);
            return vehicleTaskGroupRepository.selectByExampleList(example);
        }
        return null;
    }
    /**
     * 根据任务id和分组id获取小组的任务
     *
     * @param vehicleTaskRecoveryId 收车公司任务表主键
     * @param groupId 分组id
     * @return
     */
    public VehicleTaskGroup getByGroupIdAndVehicleTaskRecoveryId(String groupId,String vehicleTaskRecoveryId){
        if(StringUtil.isNotNull(vehicleTaskRecoveryId) && StringUtil.isNotNull(groupId)) {
            Example example = new Example(VehicleTaskGroup.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("vehicleTaskRecoveryId", vehicleTaskRecoveryId);
            criteria.andEqualTo("groupId", groupId);
            return vehicleTaskGroupRepository.selectOneByExample(example);
        }
        return null;
    }

    /**
     * 根据收车公司任务表id获取任务分组情况
     *
     * @param vehicleTaskRecoveryId 收车公司任务表主键
     * @return
     */
    public List<VehicleTaskGroup> getByVehicleTaskRecoveryId(String vehicleTaskRecoveryId){
        if(StringUtil.isNotNull(vehicleTaskRecoveryId)) {
            Example example = new Example(VehicleTaskGroup.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("vehicleTaskRecoveryId", vehicleTaskRecoveryId);
            return vehicleTaskGroupRepository.selectByExampleList(example);
        }
        return null;
    }

}
