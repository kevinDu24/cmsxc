package cn.com.leadu.cmsxc.data.appbusiness.repository;

import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryCompanyTemp;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 收车公司
 */
public interface RecoveryCompanyTempRepository {
    /**
     * 通过主键获取收车公司
     * @return
     */
    public List<RecoveryCompanyTemp> selectAll();
}
