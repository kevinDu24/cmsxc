package cn.com.leadu.cmsxc.data.appbusiness.repository;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryCompany;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryUserListVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ApplyRecoveryInfoVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.RecoveryCompanysVo;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 收车公司
 */
public interface RecoveryCompanyRepository {
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    List<RecoveryCompany> selectByExampleList(Example example);
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    RecoveryCompany selectOneByExample(Example example);
    /**
     * 登录收车公司信息
     * @param recoveryCompany
     */
    RecoveryCompany insertOne(RecoveryCompany recoveryCompany);
    /**
     * 批量登录收车公司信息
     * @param recoveryCompanys
     */
    List<RecoveryCompany> insertListByMapper(List<RecoveryCompany> recoveryCompanys);
    /**
     * 根据收车公司简称或全称，分页获取收车公司信息
     *
     * @param recoveryUserListVo 画面参数
     * @param pageQuery 分页信息
     * @return
     */
    PageInfoExtend<RecoveryCompany> selectRecoveryCompany(RecoveryUserListVo recoveryUserListVo, PageQuery pageQuery);
    /**
     * 通过主键获取收车公司
     * @param id
     * @return
     */
    RecoveryCompany selectByPrimaryKey(String id);
    /**
     * 获得收车公司表所有注册码
     * @return
     */
    List<String> selectRegisterCode();
    /**
     * 更新收车公司信息
     * @param recoveryCompany
     */
    void updateByPrimaryKey(RecoveryCompany recoveryCompany);

    /**
     * @Title:
     * @Description:  批量更新收车公司信息
     * @param recoveryCompanies
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 06:04:42
     */
    public List<RecoveryCompany> updateListByMapper(List<RecoveryCompany> recoveryCompanies);

    /**
     * 根据工单id取收车公司全称
     * @return
     */
    String selectFullNameByTaskId(String taskId);
    /**
     * 根据委托公司id获取委托公司合作的所有收车公司
     *
     * @param leaseId 委托公司id
     * @return
     */
    List<RecoveryCompanysVo> selectRecoveryCompanysByLeaseId(String leaseId);

    /**
     * 根据用户id取收车公司全称、电话及用户角色
     * @return
     */
   ApplyRecoveryInfoVo selectRecoveryInfo(String userId);
}
