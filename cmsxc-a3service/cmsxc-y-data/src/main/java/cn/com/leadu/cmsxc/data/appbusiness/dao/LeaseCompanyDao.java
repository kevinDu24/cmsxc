package cn.com.leadu.cmsxc.data.appbusiness.dao;

import cn.com.leadu.cmsxc.data.base.config.BaseDao;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.LeaseCompany;
import cn.com.leadu.cmsxc.pojo.appuser.vo.LeaseCompanysVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/2/2.
 *
 * 委托公司
 */
public interface LeaseCompanyDao extends BaseDao<LeaseCompany> {
    /**
     * 根据收车公司简称或全称，分页获取收车公司信息
     *
     * @param shortName 简称
     * @param fullName 全称
     * @return
     */
    List<LeaseCompany> selectLeaseCompany(@Param("shortName") String shortName, @Param("fullName") String fullName);
    /**
     * 根据收车公司id，获取所有合作的委托公司信息
     *
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    List<LeaseCompanysVo> selectLeaseCompanysByRecoveryCompanyId(@Param("recoveryCompanyId") String recoveryCompanyId);
}
