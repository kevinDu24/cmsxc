package cn.com.leadu.cmsxc.system.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryCompany;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryCompanyVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryUserListVo;
import org.springframework.http.ResponseEntity;

/**
 * Created by yuanzhenxia on 2018/2/1.
 *
 * 收车公司用户管理Service
 */
public interface RecoveryUserService {
    /**
     * 收车公司用户注册
     *
     * @param  recoveryCompanyVo 收车公司用户注册信息
     * @return
     */
    ResponseEntity<RestResponse> register(RecoveryCompanyVo recoveryCompanyVo);
    /**
     * 根据主管/业务员注册码获取收车公司信息，
     *
     * @param registerCode 注册码
     * @return
     */
    RecoveryCompany findByRegisterCode(String registerCode);
    /**
     * 根据收车公司简称或全称，分页获取收车公司信息
     *
     * @param recoveryUserListVo 画面参数
     * @return
     */
    ResponseEntity<RestResponse> findRecoveryUserByPage(RecoveryUserListVo recoveryUserListVo);
    /**
     * 生成收车公司注册码
     * @param newRecoveryCompany
     */
    void buildRegisterCode(RecoveryCompany newRecoveryCompany);

}
