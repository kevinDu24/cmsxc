package cn.com.leadu.cmsxc.data.system.repository.impl;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.data.system.dao.SysResourceDao;
import cn.com.leadu.cmsxc.data.system.repository.SysResourceRepository;
import cn.com.leadu.cmsxc.pojo.system.entity.SysResource;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author qiaomengnan
 * @ClassName: SysResourceRepositoryImpl
 * @Description:
 * @date 2018/1/14
 */
@Component
public class SysResourceRepositoryImpl extends AbstractBaseRepository<SysResourceDao,SysResource> implements SysResourceRepository {

    /**
     * @Title:
     * @Description: 根据角色id以及资源类型查询菜单资源
     * @param roleIds
     * @param type
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 12:39:33
     */
    public List<SysResource> selectSysResMenuBySysRoleId(List<String> roleIds,Integer type){
        return baseDao.selectSysResMenuBySysRoleId(roleIds,type);
    }


    /**
     * @Title:
     * @Description:  根据条件查询资源
     * @param example
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 03:55:41
     */
    public List<SysResource> selectListByExample(Example example){
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
    public PageInfoExtend<SysResource> selectListByExamplePageInfo(Example example, PageQuery pageQuery){
        return super.selectListByExamplePageInfo(example,pageQuery);
    }


}
