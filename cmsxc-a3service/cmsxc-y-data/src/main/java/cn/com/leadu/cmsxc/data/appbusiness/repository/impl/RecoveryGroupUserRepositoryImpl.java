package cn.com.leadu.cmsxc.data.appbusiness.repository.impl;

import cn.com.leadu.cmsxc.data.appbusiness.dao.RecoveryGroupUserDao;
import cn.com.leadu.cmsxc.data.appbusiness.repository.RecoveryGroupUserRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryGroupUser;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.GroupUserClientVo;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/9.
 *
 * 组员表
 */
@Component
public class RecoveryGroupUserRepositoryImpl extends AbstractBaseRepository<RecoveryGroupUserDao,RecoveryGroupUser> implements RecoveryGroupUserRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    public List<RecoveryGroupUser> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }
    /**
     * 登录组员表信息
     *
     * @param recoveryGroupUser
     */
    public void insertOne(RecoveryGroupUser recoveryGroupUser){
        super.insert(recoveryGroupUser);
    }
    /**
     * 根据主键更新表
     * @param recoveryGroupUser
     */
    public void updateByPrimaryKey(RecoveryGroupUser recoveryGroupUser){
        super.updateByPrimaryKey(recoveryGroupUser);
    }
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    public RecoveryGroupUser selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }
    /**
     * 根据分组id和组员id删除组员
     *
     * @param recoveryGroupId 分组id
     * @param groupUserId 组员id
     */
    public void deleteByGroupIdAndUserId(String recoveryGroupId , String groupUserId){
        baseDao.deleteByGroupIdAndUserId(recoveryGroupId, groupUserId);
    }
    /**
     * 根据分组id删除组员
     *
     * @param recoveryGroupId 分组id
     */
    public void deleteByGroupId(String recoveryGroupId){
        baseDao.deleteByGroupId(recoveryGroupId);
    }
    /**
     * 通过组id获取小组成员并且按照客户端类型分类返回
     *
     * @param recoveryGroupId 分组id
     */
    public List<GroupUserClientVo> selectGroupUserClientByGroupId(String recoveryGroupId){
        return baseDao.selectGroupUserClientByGroupId(recoveryGroupId);
    }
    /**
     * 通过收车公司id与角色获取该收车公司内勤人员客户端类型
     *
     * @param recoveryCompanyId 收车公司id
     */
    public GroupUserClientVo selectUserClientByRecoveryCompanyId(String recoveryCompanyId){
        return baseDao.selectUserClientByRecoveryCompanyId(recoveryCompanyId);
    }
}
