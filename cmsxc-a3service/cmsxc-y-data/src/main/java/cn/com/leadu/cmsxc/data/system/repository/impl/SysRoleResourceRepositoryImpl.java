package cn.com.leadu.cmsxc.data.system.repository.impl;

import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.data.system.dao.SysRoleResourceDao;
import cn.com.leadu.cmsxc.data.system.repository.SysRoleResourceRepository;
import cn.com.leadu.cmsxc.pojo.system.entity.SysRoleResource;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author qiaomengnan
 * @ClassName: SysRoleResourceRepositoryImpl
 * @Description:
 * @date 2018/1/14
 */
@Component
public class SysRoleResourceRepositoryImpl extends AbstractBaseRepository<SysRoleResourceDao,SysRoleResource> implements SysRoleResourceRepository {

    /**
     * @Title:
     * @Description:  批量保存角色资源
     * @param sysRoleResources
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 06:04:42
     */
    public List<SysRoleResource> insertListByMapper(List<SysRoleResource> sysRoleResources){
        return super.insertListByMapper(sysRoleResources);
    }

    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    @Override
    public List<SysRoleResource> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }

    /**
     * 根据角色id批量删除数据
     * @param roleId
     * @return
     */
    @Override
    public void deleteByRoleId(String roleId){
        baseDao.deleteByRoleId(roleId);
    }

}
