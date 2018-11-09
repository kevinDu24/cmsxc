package cn.com.leadu.cmsxc.appbusiness.service.impl;

import cn.com.leadu.cmsxc.appbusiness.service.RecoveryGroupUserService;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.appbusiness.repository.RecoveryGroupUserRepository;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryGroup;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryGroupUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/14.
 *
 * 用户表
 */
@Service
public class RecoveryGroupUserServiceImpl implements RecoveryGroupUserService{
    @Autowired
    private RecoveryGroupUserRepository recoveryGroupUserRepository;
    /**
     *  根据分组id查看组员信息
     *
     * @param recoveryGroupId 分组id
     * @return
     */
    public List<RecoveryGroupUser> selectRecoveryGroupUserByGroupId(String recoveryGroupId){
        if(StringUtil.isNotNull(recoveryGroupId)) {
            Example example = new Example(RecoveryGroupUser.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("recoveryGroupId", recoveryGroupId);
            return recoveryGroupUserRepository.selectByExampleList(example);
        }
        return null;
    }
}
