package cn.com.leadu.cmsxc.data.appbusiness.repository.impl;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import cn.com.leadu.cmsxc.data.appbusiness.dao.RecoveryCompanyDao;
import cn.com.leadu.cmsxc.data.appbusiness.repository.RecoveryCompanyRepository;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryCompany;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryUserListVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ApplyRecoveryInfoVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.RecoveryCompanysVo;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 收车公司
 */
@Component
public class RecoveryCompanyRepositoryImpl extends AbstractBaseRepository<RecoveryCompanyDao,RecoveryCompany> implements RecoveryCompanyRepository {
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    @Override
    public List<RecoveryCompany> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    @Override
    public RecoveryCompany selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }
    /**
     * 登录收车公司信息
     * @param recoveryCompany
     */
    public RecoveryCompany insertOne(RecoveryCompany recoveryCompany){
        return super.insert(recoveryCompany);
    }
    /**
     * 批量登录收车公司信息
     * @param recoveryCompanys
     */
    public List<RecoveryCompany> insertListByMapper(List<RecoveryCompany> recoveryCompanys){
        return super.insertListByMapper(recoveryCompanys);
    }
    /**
     * 根据收车公司简称或全称，分页获取收车公司信息
     *
     * @param recoveryUserListVo 画面参数
     * @param pageQuery 分页信息
     * @return
     */
    public PageInfoExtend<RecoveryCompany> selectRecoveryCompany(RecoveryUserListVo recoveryUserListVo, PageQuery pageQuery){
        PageInfo<RecoveryCompany> pageInfo = PageHelper.startPage(pageQuery.getCurrenPage(),pageQuery.getPageSize())
                .doSelectPageInfo(new ISelect() {
                    @Override
                    public void doSelect() {
                        baseDao.selectRecoveryCompany(recoveryUserListVo.getRecoveryShortName(), recoveryUserListVo.getRecoveryFullName(),
                                recoveryUserListVo.getManagerRegisterCode(),recoveryUserListVo.getSalesmanRegisterCode());
                    }
                });
        PageInfoExtend<RecoveryCompany> pageInfoExtend = new PageInfoExtend<>();
        pageInfoExtend.setDraw(pageQuery.getDraw());
        pageInfoExtend.setData(pageInfo.getList());
        pageInfoExtend.setRecordsTotal(pageInfo.getTotal());
        pageInfoExtend.setRecordsFiltered(pageInfo.getTotal());
        return pageInfoExtend;
    }

    /**
     * 通过主键获取收车公司
     * @param id
     * @return
     */
    public RecoveryCompany selectByPrimaryKey(String id){
        return super.selectByPrimaryKey(id);
    }

    /**
     * 获得收车公司表所有注册码
     * @return
     */
    public List<String> selectRegisterCode(){
        return baseDao.selectRegisterCode();
    }

    /**
     * 更新收车公司信息
     * @param recoveryCompany
     */
    public void updateByPrimaryKey(RecoveryCompany recoveryCompany){
        super.updateByPrimaryKey(recoveryCompany);
    }

    /**
     * @Title:
     * @Description:  批量更新收车公司信息
     * @param recoveryCompanies
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 06:04:42
     */
    public List<RecoveryCompany> updateListByMapper(List<RecoveryCompany> recoveryCompanies){
        return super.updateListByMapper(recoveryCompanies);
    }

    /**
     * 根据工单id取收车公司全称
     * @return
     */
    public String selectFullNameByTaskId(String taskId){
        return baseDao.selectFullNameByTaskId(taskId);
    }

    /**
     * 根据委托公司id获取委托公司合作的所有收车公司
     *
     * @param leaseId 委托公司id
     * @return
     */
    public List<RecoveryCompanysVo> selectRecoveryCompanysByLeaseId(String leaseId){
        return baseDao.selectRecoveryCompanysByLeaseId(leaseId);
    }

    /**
     * 根据用户id取收车公司全称、电话及用户角色
     * @return
     */
    public ApplyRecoveryInfoVo selectRecoveryInfo(String userId){
        return baseDao.selectRecoveryInfo(userId);
    }
}

