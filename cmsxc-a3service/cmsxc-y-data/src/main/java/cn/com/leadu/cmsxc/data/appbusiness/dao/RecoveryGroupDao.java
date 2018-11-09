package cn.com.leadu.cmsxc.data.appbusiness.dao;

import cn.com.leadu.cmsxc.data.base.config.BaseDao;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryGroup;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.GroupListVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.OrganizationListVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.SearchSalesmanVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.UserListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/9.
 *
 * 分组表
 */
public interface RecoveryGroupDao extends BaseDao<RecoveryGroup> {
    /**
     * 根据收车公司id取出所有小组及组员数量
     *
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    List<GroupListVo> selectRecoveryGroupListByRecoveryCompanyId(@Param("recoveryCompanyId") String recoveryCompanyId);

    /**
     * 根据收车公司id和角色取出所有未分组成员信息
     *
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    List<UserListVo> selectUserByRecoveryCompanyIdAndUserRole(@Param("recoveryCompanyId") String recoveryCompanyId);
    /**
     * 根据用户id或用户姓名查找业务员
     *
     * @param recoveryCompanyId 收车公司id
     * @param inputValue 用户输入的值
     * @return
     */
    List<SearchSalesmanVo> selectSalesmanByUserIdOrUserName(@Param("recoveryCompanyId") String recoveryCompanyId , @Param("inputValue") String inputValue);
    /**
     * 根据分组id获取组员信息
     *
     * @param recoveryGroupId 分组id
     * @return
     */
    List<SearchSalesmanVo> selectGroupUserByGroupId(@Param("recoveryGroupId") String recoveryGroupId);

    /**
     * 根据小组id删除小组
     *
     * @param groupId 分组id
     * @return
     */
    void deleteByRecoveryGroupId(@Param("groupId") String groupId);


}
