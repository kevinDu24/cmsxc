package cn.com.leadu.cmsxc.data.appbusiness.dao;

import cn.com.leadu.cmsxc.data.base.config.BaseDao;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryCompany;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ApplyRecoveryInfoVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.RecoveryCompanysVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yuanzhnexia on 2018/1/16.
 *
 * 收车公司
 */
public interface RecoveryCompanyDao extends BaseDao<RecoveryCompany>{
    /**
     * 根据收车公司简称或全称，分页获取收车公司信息
     *
     * @param shortName 简称
     * @param fullName 全称
     * @param managerRegisterCode 主管注册码
     * @param salesmanRegisterCode 业务员注册码
     * @return
     */
    List<RecoveryCompany> selectRecoveryCompany(@Param("shortName") String shortName, @Param("fullName") String fullName,
                                                @Param("managerRegisterCode") String managerRegisterCode,@Param("salesmanRegisterCode") String salesmanRegisterCode);

    /**
     * 获得收车公司表所有注册码
     * @return
     */
    List<String> selectRegisterCode();

    /**
     * 根据工单id取收车公司全称
     * @return
     */
    public String selectFullNameByTaskId(@Param("taskId") String taskId);
    /**
     * 根据工单id取收车公司全称
     * @return
     */
    List<RecoveryCompanysVo> selectRecoveryCompanysByLeaseId(@Param("leaseId") String leaseId);

    /**
     * 根据用户id取收车公司全称、电话及用户角色
     * @return
     */
    ApplyRecoveryInfoVo selectRecoveryInfo(@Param("userId") String userId);
}
