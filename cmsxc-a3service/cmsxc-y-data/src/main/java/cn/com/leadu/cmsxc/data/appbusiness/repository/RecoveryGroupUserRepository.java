package cn.com.leadu.cmsxc.data.appbusiness.repository;

import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryGroupUser;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.GroupUserClientVo;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/9.
 *
 * 组员表
 */
public interface RecoveryGroupUserRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<RecoveryGroupUser> selectByExampleList(Example example);
    /**
     * 登录组员表信息
     *
     * @param recoveryGroupUser
     */
    void insertOne(RecoveryGroupUser recoveryGroupUser);
    /**
     * 根据主键更新表
     * @param recoveryGroupUser
     */
    void updateByPrimaryKey(RecoveryGroupUser recoveryGroupUser);
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    RecoveryGroupUser selectOneByExample(Example example);

    /**
     * 根据分组id和组员id删除组员
     *
     * @param recoveryGroupId 分组id
     * @param groupUserId 组员id
     */
    void deleteByGroupIdAndUserId(String recoveryGroupId , String groupUserId);

    /**
     * 根据分组id删除组员
     *
     * @param recoveryGroupId 分组id
     */
    void deleteByGroupId(String recoveryGroupId);

    /**
     * 通过组id获取小组成员并且按照客户端类型分类返回
     *
     * @param recoveryGroupId 分组id
     */
    List<GroupUserClientVo> selectGroupUserClientByGroupId(String recoveryGroupId);
    /**
     * 通过收车公司id与角色获取该收车公司内勤人员客户端类型
     *
     * @param recoveryCompanyId 收车公司id
     */
    GroupUserClientVo selectUserClientByRecoveryCompanyId(String recoveryCompanyId);
}
