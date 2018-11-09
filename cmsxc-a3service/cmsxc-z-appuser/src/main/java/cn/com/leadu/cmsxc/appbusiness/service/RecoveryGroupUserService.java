package cn.com.leadu.cmsxc.appbusiness.service;

import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryGroupUser;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/14.
 *
 * 用户表
 */
public interface RecoveryGroupUserService {
    /**
     *  根据分组id查看组员信息
     *
     * @param groupId 分组id
     * @return
     */
    List<RecoveryGroupUser> selectRecoveryGroupUserByGroupId(String groupId);
}
