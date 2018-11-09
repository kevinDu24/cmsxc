package cn.com.leadu.cmsxc.appbusiness.service;

import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryGroup;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryGroupVo;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/11.
 *
 * 分组Service
 */
public interface RecoveryGroupService {
    /**
     *  根据分组名和收车公司id查看分组信息
     *
     * @param groupName 分组名称
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    RecoveryGroup selectByGroupNameAndRecoveryCompanyId(String groupName, String recoveryCompanyId);

    /**
     * 根据收车公司id获取所有分组
     *
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    List<RecoveryGroupVo> selectByRecoveryCompanyId(String recoveryCompanyId);
    /**
     *  根据分组id查看分组信息
     *
     * @param recoveryGroupId 分组id
     * @return
     */
    RecoveryGroup selectByRecoveryGroupId(String recoveryGroupId);

}
