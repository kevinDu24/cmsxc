package cn.com.leadu.cmsxc.appbusiness.service.impl;

import cn.com.leadu.cmsxc.appbusiness.service.RecoveryGroupService;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.appbusiness.repository.RecoveryGroupRepository;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryGroup;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryGroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/11.
 *
 * 分组impl
 */
@Service
public class RecoveryGroupServiceImpl implements RecoveryGroupService{
    @Autowired
    private RecoveryGroupRepository recoveryGroupRepository;
    /**
     *  根据分组名和收车公司id查看分组信息
     *
     * @param groupName 分组名称
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    public RecoveryGroup selectByGroupNameAndRecoveryCompanyId(String groupName, String recoveryCompanyId){
        if(StringUtil.isNotNull(groupName) && StringUtil.isNotNull(recoveryCompanyId)) {
            Example example = new Example(RecoveryGroup.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("groupName", groupName);
            criteria.andEqualTo("recoveryCompanyId", recoveryCompanyId);
            return recoveryGroupRepository.selectOneByExample(example);
        }
        return null;
    }
    /**
     * 根据收车公司id获取所有分组
     *
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    public List<RecoveryGroupVo> selectByRecoveryCompanyId(String recoveryCompanyId){
        List<RecoveryGroupVo> recoveryGroupVos = new ArrayList<>();
        if(StringUtil.isNotNull(recoveryCompanyId)) {
            Example example = new Example(RecoveryGroup.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("recoveryCompanyId", recoveryCompanyId);
            example.setOrderByClause(" create_time desc ");
            List<RecoveryGroup> groupsList = recoveryGroupRepository.selectByExampleList(example);
            for(RecoveryGroup recoveryGroup : groupsList){
                RecoveryGroupVo vo = new RecoveryGroupVo();
                vo.setGroupId(recoveryGroup.getId());
                vo.setGroupName(recoveryGroup.getGroupName());
                recoveryGroupVos.add(vo);
            }
            return recoveryGroupVos;
        }
        return null;
    }

    /**
     *  根据分组id查看分组信息
     *
     * @param groupId 分组id
     * @return
     */
    public RecoveryGroup selectByRecoveryGroupId(String groupId){
        if(StringUtil.isNotNull(groupId)) {
            Example example = new Example(RecoveryGroup.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id", groupId);
            return recoveryGroupRepository.selectOneByExample(example);
        }
        return null;
    }
}
