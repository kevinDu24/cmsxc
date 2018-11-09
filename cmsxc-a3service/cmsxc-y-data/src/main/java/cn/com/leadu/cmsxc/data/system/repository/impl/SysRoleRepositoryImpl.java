package cn.com.leadu.cmsxc.data.system.repository.impl;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.data.system.dao.SysRoleDao;
import cn.com.leadu.cmsxc.data.system.repository.SysRoleRepository;
import cn.com.leadu.cmsxc.pojo.system.entity.SysRole;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author qiaomengnan
 * @ClassName: SysRoleRepositoryImpl
 * @Description: 角色repository
 * @date 2018/1/12
 */
@Component
public class SysRoleRepositoryImpl extends AbstractBaseRepository<SysRoleDao,SysRole> implements SysRoleRepository {

    /**
     * @Title:
     * @Description: 保存角色信息
     * @param sysRole
     * @return String
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 10:26:22
     */
    public String insertOne(SysRole sysRole){
        super.insert(sysRole);
        return sysRole.getId();
    }

    /**
     * @Title:
     * @Description:  通过条件批量查询
     * @param example
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 04:23:21
     */
    public List<SysRole> selectByExampleList(Example example){
        return super.selectListByExample(example);
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
    public PageInfoExtend<SysRole> selectListByExamplePageInfo(Example example, PageQuery pageQuery){
        return super.selectListByExamplePageInfo(example,pageQuery);
    }

    /**
     * @Title:
     * @Description:  更新角色
     * @param sysRole
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:53:16
     */
    public void updateByPrimaryKeySelective(SysRole sysRole){
        super.updateByPrimaryKeySelective(sysRole);
    }

    /**
     * @Title:
     * @Description:  删除角色
     * @param id
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:53:28
     */
    public void delete(Object id){
        super.delete(id);
    }

    /**
     * @Title:
     * @Description:  根据id获取角色
     * @param id
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 06:31:34
     */
    public SysRole selectByPrimaryKey(Object id){
        return super.selectByPrimaryKey(id);
    }


    /**
     * @Title:
     * @Description:  根据用户id获取对应的角色id集合
     * @param userId
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 01:00:27
     */
    public List<String> selectSysRoleIdsBySysUserId(String userId){
        return baseDao.selectSysRoleIdsBySysUserId(userId);
    }



}
