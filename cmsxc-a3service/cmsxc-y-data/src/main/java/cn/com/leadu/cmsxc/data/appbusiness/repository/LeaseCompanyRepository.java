package cn.com.leadu.cmsxc.data.appbusiness.repository;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.LeaseCompany;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.LeaseUserListVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.LeaseCompanysVo;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/2/2.
 *
 * 委托公司
 */
public interface LeaseCompanyRepository {
    /**
     * 登录委托公司信息
     * @param leaseCompany
     */
    LeaseCompany insertOne(LeaseCompany leaseCompany);

    /**
     * 根据委托公司简称或全称，分页获取委托公司信息
     *
     * @param leaseUserListVo 画面信息
     * @param pageQuery 当前页
     * @return
     */
    PageInfoExtend<LeaseCompany> selectLeaseCompany(LeaseUserListVo leaseUserListVo, PageQuery pageQuery);
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    List<LeaseCompany> selectByExampleList(Example example);
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    LeaseCompany selectOneByExample(Example example);
    /**
     * 根据主键获取数据
     * @param id
     * @return
     */
    LeaseCompany selectByPrimaryKey(String id);

    /**
     * 根据主键删除
     * @param id
     */
    void delete(Object id);
    /**
     * 根据收车公司id，获取所有合作的委托公司信息
     *
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    List<LeaseCompanysVo> selectLeaseCompanysByRecoveryCompanyId(String recoveryCompanyId);
}
