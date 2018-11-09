package cn.com.leadu.cmsxc.data.appbusiness.repository.impl;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import cn.com.leadu.cmsxc.data.appbusiness.dao.LeaseCompanyDao;
import cn.com.leadu.cmsxc.data.appbusiness.repository.LeaseCompanyRepository;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.LeaseCompany;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryCompany;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.LeaseUserListVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.LeaseCompanysVo;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/2/2.
 *
 * 委托公司
 */
@Component
public class LeaseCompanyRepositoryImpl extends AbstractBaseRepository<LeaseCompanyDao,LeaseCompany> implements LeaseCompanyRepository {
    /**
     * 登录委托公司信息
     * @param leaseCompany
     */
    public LeaseCompany insertOne(LeaseCompany leaseCompany){
        return super.insert(leaseCompany);
    }

    /**
     * 根据委托公司简称或全称，分页获取委托公司信息
     *
     * @param leaseUserListVo 画面信息
     * @param pageQuery 当前页
     * @return
     */
   public PageInfoExtend<LeaseCompany> selectLeaseCompany(LeaseUserListVo leaseUserListVo, PageQuery pageQuery){
       PageInfo<LeaseCompany> pageInfo = PageHelper.startPage(pageQuery.getCurrenPage(),pageQuery.getPageSize())
               .doSelectPageInfo(new ISelect() {
                   @Override
                   public void doSelect() {
                       baseDao.selectLeaseCompany(leaseUserListVo.getLeaseShortName(),leaseUserListVo.getLeaseFullName());
                   }
               });
       PageInfoExtend<LeaseCompany> pageInfoExtend = new PageInfoExtend<>();
       pageInfoExtend.setDraw(pageQuery.getDraw());
       pageInfoExtend.setData(pageInfo.getList());
       pageInfoExtend.setRecordsTotal(pageInfo.getTotal());
       pageInfoExtend.setRecordsFiltered(pageInfo.getTotal());
       return pageInfoExtend;
    }

    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
   public List<LeaseCompany> selectByExampleList(Example example){
      return super.selectListByExample(example);
   }
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    public LeaseCompany selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }

    /**
     * 根据主键获取数据
     * @param id
     * @return
     */
    public LeaseCompany selectByPrimaryKey(String id){
        return super.selectByPrimaryKey(id);
    }

    /**
     * 根据主键删除
     * @param id
     */
    public void delete(Object id){
        super.delete(id);
    }
    /**
     * 根据收车公司id，获取所有合作的委托公司信息
     *
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    public List<LeaseCompanysVo> selectLeaseCompanysByRecoveryCompanyId(String recoveryCompanyId){
       return baseDao.selectLeaseCompanysByRecoveryCompanyId(recoveryCompanyId);
    }
}
