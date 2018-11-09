package cn.com.leadu.cmsxc.appbusiness.service.impl;

import cn.com.leadu.cmsxc.appbusiness.service.VehicleTaskSearchHistoryService;
import cn.com.leadu.cmsxc.common.util.ArrayUtil;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.appbusiness.repository.VehicleTaskSearchHistoryRepository;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskSearchHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/17.
 *
 * 工单查询历史
 */
@Service
public class VehicleTaskSearchHistoryServiceImpl implements VehicleTaskSearchHistoryService {
    @Autowired
    private VehicleTaskSearchHistoryRepository vehicleTaskSearchHistoryRepository;
    /**
     * 根据任务id和用户id查询工单查询历史表
     * @param taskId 工单id
     * @param userId 用户id
     * @return
     */
    public VehicleTaskSearchHistory selectByTaskIdAndUserId(String taskId, String userId){
        if(StringUtil.isNotNull(taskId) && StringUtil.isNotNull(userId)) {
            Example example = new Example(VehicleTaskSearchHistory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("taskId", taskId);
            criteria.andEqualTo("userId", userId);
            example.setOrderByClause("search_time desc");
            return vehicleTaskSearchHistoryRepository.selectOneByExample(example);
        }
        return null;
    }
    /**
     * 根据车牌号和用户id查询工单查询历史表
     * @param taskId 车牌号
     * @param userId 用户id
     * @return
     */
    public VehicleTaskSearchHistory selectByPlateAndUserId(String taskId, String userId){
        if(StringUtil.isNotNull(taskId) && StringUtil.isNotNull(userId)) {
            Example example = new Example(VehicleTaskSearchHistory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("taskId", taskId);
            criteria.andEqualTo("userId", userId);
            example.setOrderByClause("search_time desc");
            return vehicleTaskSearchHistoryRepository.selectOneByExample(example);
        }
        return null;
    }
}
