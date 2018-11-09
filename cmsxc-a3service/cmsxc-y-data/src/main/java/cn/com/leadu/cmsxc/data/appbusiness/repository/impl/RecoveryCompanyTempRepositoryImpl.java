package cn.com.leadu.cmsxc.data.appbusiness.repository.impl;

import cn.com.leadu.cmsxc.data.appbusiness.dao.RecoveryCompanyTempDao;
import cn.com.leadu.cmsxc.data.appbusiness.repository.RecoveryCompanyTempRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryCompanyTemp;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 收车公司
 */
@Component
public class RecoveryCompanyTempRepositoryImpl extends AbstractBaseRepository<RecoveryCompanyTempDao,RecoveryCompanyTemp> implements RecoveryCompanyTempRepository {

    /**
     * 通过主键获取收车公司
     * @return
     */
    public List<RecoveryCompanyTemp> selectAll(){
        return super.selectAll();
    }

}

