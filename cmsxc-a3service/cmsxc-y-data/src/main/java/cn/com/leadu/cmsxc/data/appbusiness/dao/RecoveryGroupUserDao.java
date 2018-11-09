package cn.com.leadu.cmsxc.data.appbusiness.dao;

import cn.com.leadu.cmsxc.data.base.config.BaseDao;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryGroupUser;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.GroupUserClientVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/9.
 *
 * 组员表
 */
public interface RecoveryGroupUserDao extends BaseDao<RecoveryGroupUser> {
    /**
     * 根据用户id和分组id删除组员
     *
     * @param recoveryGroupId 分组id
     * @param groupUserId 用户id
     */
    void deleteByGroupIdAndUserId(@Param("recoveryGroupId") String recoveryGroupId, @Param("groupUserId") String groupUserId);

    /**
     * 根据分组id删除组员
     *
     * @param recoveryGroupId 分组id
     */
    void deleteByGroupId(@Param("recoveryGroupId") String recoveryGroupId);
    /**
     * 通过组id获取小组成员并且按照客户端类型分类返回
     *
     * @param recoveryGroupId 分组id
     */
    List<GroupUserClientVo> selectGroupUserClientByGroupId(@Param("recoveryGroupId") String recoveryGroupId);
    /**
     * 通过收车公司id与角色获取该收车公司内勤人员客户端类型
     *
     * @param recoveryCompanyId 收车公司id
     */
    GroupUserClientVo selectUserClientByRecoveryCompanyId(@Param("recoveryCompanyId") String recoveryCompanyId);
}
