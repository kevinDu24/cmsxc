package cn.com.leadu.cmsxc.data.system.repository.impl;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.data.system.dao.SystemUserDao;
import cn.com.leadu.cmsxc.data.system.repository.SystemUserRepository;
import cn.com.leadu.cmsxc.pojo.appuser.vo.ScanInfoVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.UserRoleListVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserSearchListVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserSearchVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SystemUserVo;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/15.
 *
 * 用户信息
 */
@Component
public class SystemUserRepositoryImpl extends AbstractBaseRepository<SystemUserDao,SystemUser> implements SystemUserRepository {
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    @Override
    public List<SystemUser> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    @Override
    public SystemUser selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }
    /**
     * 登录用户信息
     * @param sysUser
     */
    @Override
    public SystemUser insertOne(SystemUser sysUser){
        return super.insert(sysUser);
    }

    /**
     * 根据主键更新表
     * @param sysUser
     */
    @Override
    public void updateByPrimaryKey(SystemUser sysUser) {
        super.updateByPrimaryKey(sysUser);
    }

    /**
     * @Title:
     * @Description: 分页查询
     * @param example,pageQuery
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 04:49:17
     */
    public PageInfoExtend<SystemUser> selectListByExamplePageInfo(Example example, PageQuery pageQuery){
        return super.selectListByExamplePageInfo(example,pageQuery);
    }

    /**
     * @Title:
     * @Description:  更新用户
     * @param sysUser
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:53:16
     */
    @Override
    public void updateByPrimaryKeySelective(SystemUser sysUser) {
        super.updateByPrimaryKeySelective(sysUser);
    }

    /**
     * @Title:
     * @Description:  删除用户
     * @param id
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:53:28
     */
    @Override
    public void delete(Object id) {
        super.delete(id);
    }

    /**
     * @Title:
     * @Description:  根据id获取用户
     * @param id
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 06:31:34
     */
    @Override
    public SystemUser selectByPrimaryKey(Object id){
        return super.selectByPrimaryKey(id);
    }


    /**
     * @Title:
     * @Description:   分页查询数据字典vo
     * @param systemUserVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/02/03 05:37:57
     */
    public PageInfoExtend<SystemUserVo> findSysUserByPage(SystemUserVo systemUserVo, PageQuery pageQuery, SystemUser sysUser){
        PageInfo<SystemUserVo> pageInfo = PageHelper.startPage(pageQuery.getCurrenPage(),pageQuery.getPageSize())
                .doSelectPageInfo(new ISelect() {
                    @Override
                    public void doSelect() {
                        baseDao.findSysUserByPage(systemUserVo, sysUser.getUserRole(), sysUser.getRecoveryCompanyId(), sysUser.getLeaseId());
                    }
                });
        PageInfoExtend<SystemUserVo> pageInfoExtend = new PageInfoExtend<>();
        pageInfoExtend.setDraw(pageQuery.getDraw());
        pageInfoExtend.setData(pageInfo.getList());
        pageInfoExtend.setRecordsTotal(pageInfo.getTotal());
        pageInfoExtend.setRecordsFiltered(pageInfo.getTotal());
        return pageInfoExtend;
    }

    /**
     *  分页获取注册账户信息---画面显示
     *
     * @param sysUserSearchVo 画面信息
     * @param role 用户角色
     * @param leaseId 委托公司id
     * @param pageQuery 分页信息
     * @return
     */
    public PageInfoExtend<SysUserSearchListVo> findSysUserListByPage(SysUserSearchVo sysUserSearchVo, String role, String leaseId ,PageQuery pageQuery){
        PageInfo<SysUserSearchListVo> pageInfo = PageHelper.startPage(pageQuery.getCurrenPage(),pageQuery.getPageSize())
                .doSelectPageInfo(new ISelect() {
                    @Override
                    public void doSelect() {
                        baseDao.findSysUserListByPage(sysUserSearchVo, role, leaseId);
                    }
                });
        PageInfoExtend<SysUserSearchListVo> pageInfoExtend = new PageInfoExtend<>();
        pageInfoExtend.setDraw(pageQuery.getDraw());
        pageInfoExtend.setData(pageInfo.getList());
        pageInfoExtend.setRecordsTotal(pageInfo.getTotal());
        pageInfoExtend.setRecordsFiltered(pageInfo.getTotal());
        return pageInfoExtend;
    }

    /**
     * 获取所有注册账户信息--- 导出报表用
     *
     * @param sysUserSearchVo  画面信息
     * @param role   用户角色
     * @param leaseId  委托公司id
     * @return
     */
    public List<SysUserSearchListVo> getSysUserListAll(SysUserSearchVo sysUserSearchVo, String role, String leaseId){
        return baseDao.findSysUserListByPage(sysUserSearchVo, role, leaseId);
    }

    /**
     * 获取角色列表
     *
     * @param leaseId 委托公司id
     * @return
     */
    public List<UserRoleListVo> selectUserList(String leaseId){
        return baseDao.selectUserList(leaseId);
    }
    /**
     * 根据委托公司id及工单所在省份，获取审核人员信息
     *
     * @param leaseId 委托公司id
     * @param taskId 任务id
     * @return
     */
    public List<SystemUser> selectLeaseAdminUserList(String leaseId, String taskId){
        return baseDao.selectLeaseAdminUserList(leaseId,taskId);
    }

    /**
     * 根据业务员用户名获取其手机号、姓名、所在收车公司
     *
     * @param userId 用户名
     * @return
     */
    public ScanInfoVo selectInfoByUserId(String userId){
        return baseDao.selectInfoByUserId(userId);
    }


}
