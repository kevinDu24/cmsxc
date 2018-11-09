package cn.com.leadu.cmsxc.appbusiness.service;

import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryCompany;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 收车公司Service
 */

public interface RecoveryCompanyService {
    /**
     * 根据主管注册码获取收车公司信息
     * @param managerRegisterCode 主管注册码
     * @return
     */
    RecoveryCompany selectRecoveryCompanyByManagerRegisterCode(String managerRegisterCode);
    /**
     * 根据业务员注册码获取收车公司信息
     * @param salesmanRegisterCode 业务员注册码
     * @return
     */
    RecoveryCompany selectRecoveryCompanyBySalasmanRegisterCode(String salesmanRegisterCode);
    /**
     * 根据老板注册码获取收车公司信息
     * @param bossRegisterCode 老板注册码
     * @return
     */
    RecoveryCompany selectRecoveryCompanyByBossRegisterCode(String bossRegisterCode);
    /**
     * 根据收车公司id获取收车公司信息
     *
     * @param id 收车公司id
     * @return
     */
    RecoveryCompany selectRecoveryCompanyById(String id);
    /**
     * 根据工单id取收车公司全称
     * @param taskId 工单id
     * @return
     */
    public String getFullNameByTaskId(Long taskId);
}
