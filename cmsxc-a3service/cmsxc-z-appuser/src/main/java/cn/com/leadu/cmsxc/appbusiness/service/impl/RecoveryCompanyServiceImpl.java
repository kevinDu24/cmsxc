package cn.com.leadu.cmsxc.appbusiness.service.impl;

import cn.com.leadu.cmsxc.appbusiness.service.RecoveryCompanyService;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.appbusiness.repository.RecoveryCompanyRepository;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryCompany;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 收车公司
 */
@Service
public class RecoveryCompanyServiceImpl implements RecoveryCompanyService {
    private static final Logger logger = LoggerFactory.getLogger(RecoveryCompanyServiceImpl.class);
    @Autowired
    private RecoveryCompanyRepository recoveryCompanyRepository;
    /**
     * 根据主管注册码获取收车公司信息
     * @param managerRegisterCode 主管注册码
     * @return
     */
   public RecoveryCompany selectRecoveryCompanyByManagerRegisterCode(String managerRegisterCode){
       if(StringUtil.isNotNull(managerRegisterCode)) {
           Example example = new Example(RecoveryCompany.class);
           Example.Criteria criteria = example.createCriteria();
           criteria.andEqualTo("managerRegisterCode", managerRegisterCode);
           return recoveryCompanyRepository.selectOneByExample(example);
       }
       return null;
   }
    /**
     * 根据业务员注册码获取收车公司信息
     *
     * @param salesmanRegisterCode 业务员注册码
     * @return
     */
  public RecoveryCompany selectRecoveryCompanyBySalasmanRegisterCode(String salesmanRegisterCode){
      if(StringUtil.isNotNull(salesmanRegisterCode)) {
          Example example = new Example(RecoveryCompany.class);
          Example.Criteria criteria = example.createCriteria();
          criteria.andEqualTo("salesmanRegisterCode", salesmanRegisterCode);
          return recoveryCompanyRepository.selectOneByExample(example);
      }
      return null;
  }
    /**
     * 根据老板注册码获取收车公司信息
     * @param bossRegisterCode 老板注册码
     * @return
     */
    public RecoveryCompany selectRecoveryCompanyByBossRegisterCode(String bossRegisterCode){
        if(StringUtil.isNotNull(bossRegisterCode)) {
            Example example = new Example(RecoveryCompany.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("bossRegisterCode", bossRegisterCode);
            return recoveryCompanyRepository.selectOneByExample(example);
        }
        return null;
    }

    /**
     * 根据收车公司id获取收车公司信息
     *
     * @param id 收车公司id
     * @return
     */
    public RecoveryCompany selectRecoveryCompanyById(String id){
        if(StringUtil.isNotNull(id)) {
            Example example = new Example(RecoveryCompany.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id", id);
            return recoveryCompanyRepository.selectOneByExample(example);
        }
        return null;
    }

    /**
     * 根据工单id取收车公司全称
     * @param taskId 工单id
     * @return
     */
    public String getFullNameByTaskId(Long taskId){
        return recoveryCompanyRepository.selectFullNameByTaskId(String.valueOf(taskId));
    }

}
