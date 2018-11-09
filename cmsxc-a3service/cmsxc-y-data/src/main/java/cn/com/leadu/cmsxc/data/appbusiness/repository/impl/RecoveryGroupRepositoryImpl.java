package cn.com.leadu.cmsxc.data.appbusiness.repository.impl;

import cn.com.leadu.cmsxc.data.appbusiness.dao.RecoveryGroupDao;
import cn.com.leadu.cmsxc.data.appbusiness.repository.RecoveryGroupRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryGroup;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.GroupListVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.SearchSalesmanVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.UserListVo;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/9.
 *
 * 分组表
 */
@Component
public class RecoveryGroupRepositoryImpl extends AbstractBaseRepository<RecoveryGroupDao,RecoveryGroup> implements RecoveryGroupRepository{
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    public List<RecoveryGroup> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }
    /**
     * 登录分组表信息
     *
     * @param recoveryGroup
     */
    public void insertOne(RecoveryGroup recoveryGroup){
        super.insert(recoveryGroup);
    }
    /**
     * 根据主键更新表
     * @param recoveryGroup
     */
    public void updateByPrimaryKey(RecoveryGroup recoveryGroup){
        super.updateByPrimaryKey(recoveryGroup);
    }
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    public RecoveryGroup selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }

    /**
     * 根据收车公司id取出所有小组及组员数量
     *
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    public List<GroupListVo> selectRecoveryGroupListByRecoveryCompanyId(String recoveryCompanyId){
        return baseDao.selectRecoveryGroupListByRecoveryCompanyId(recoveryCompanyId);
    }
    /**
     * 根据收车公司id和角色取出所有未分组成员信息
     *
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    public List<UserListVo> selectUserByRecoveryCompanyIdAndUserRole(String recoveryCompanyId){
        return baseDao.selectUserByRecoveryCompanyIdAndUserRole(recoveryCompanyId);
    }
    /**
     * 根据用户id或用户姓名查找业务员
     *
     * @param recoveryCompanyId 收车公司id
     * @param inputValue 用户输入的值
     * @return
     */
    public List<SearchSalesmanVo> selectSalesmanByUserIdOrUserName(String recoveryCompanyId, String inputValue){
        return baseDao.selectSalesmanByUserIdOrUserName(recoveryCompanyId, inputValue);
    }
    /**
     * 根据分组id获取组员信息
     *
     * @param recoveryGroupId 分组id
     * @return
     */
    public  List<SearchSalesmanVo> selectGroupUserByGroupId(String recoveryGroupId){
        return baseDao.selectGroupUserByGroupId(recoveryGroupId);
    }
    /**
     * 根据小组id删除小组
     *
     * @param groupId 分组id
     */
    public void deleteByRecoveryGroupId(String groupId){
        baseDao.deleteByRecoveryGroupId(groupId);
    }
    /**
     * @Title:
     * @Description:  根据id获取组信息
     * @param id
     * @return
     * @throws
     */
    @Override
    public RecoveryGroup selectByPrimaryKey(Object id){
        return super.selectByPrimaryKey(id);
    }
}
