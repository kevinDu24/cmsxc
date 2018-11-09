package cn.com.leadu.cmsxc.data.appbusiness.repository;

import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryGroup;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.GroupListVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.SearchSalesmanVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.UserListVo;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/9.
 *
 * 分组表
 */
public interface RecoveryGroupRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<RecoveryGroup> selectByExampleList(Example example);
    /**
     * 登录分组表信息
     *
     * @param recoveryGroup
     */
    void insertOne(RecoveryGroup recoveryGroup);
    /**
     * 根据主键更新表
     * @param recoveryGroup
     */
    void updateByPrimaryKey(RecoveryGroup recoveryGroup);
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    RecoveryGroup selectOneByExample(Example example);

    /**
     * 根据收车公司id取出所有小组及组员数量
     *
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    List<GroupListVo> selectRecoveryGroupListByRecoveryCompanyId(String recoveryCompanyId);
    /**
     * 根据收车公司id和角色取出所有未分组成员信息
     *
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    List<UserListVo> selectUserByRecoveryCompanyIdAndUserRole(String recoveryCompanyId);
    /**
     * 根据用户id或用户姓名查找业务员
     *
     * @param recoveryCompanyId 收车公司id
     * @param inputValue 用户输入的值
     * @return
     */
    List<SearchSalesmanVo> selectSalesmanByUserIdOrUserName(String recoveryCompanyId, String inputValue);
    /**
     * 根据分组id获取组员信息
     *
     * @param recoveryGroupId 分组id
     * @return
     */
    List<SearchSalesmanVo> selectGroupUserByGroupId(String recoveryGroupId);

    /**
     * 根据小组id删除小组
     *
     * @param groupId 分组id
     */
    void deleteByRecoveryGroupId(String groupId);
    /**
     * @Title:
     * @Description:  根据id获取组信息
     * @param id
     * @return
     * @throws
     */
    RecoveryGroup selectByPrimaryKey(Object id);

}
