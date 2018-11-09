package cn.com.leadu.cmsxc.data.system.repository.impl;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.data.system.dao.SysUserDao;
import cn.com.leadu.cmsxc.data.system.repository.SysUserRepository;
import cn.com.leadu.cmsxc.pojo.system.entity.SysUser;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: qiaohao
 * Date: 2018/1/4
 * Time: 下午4:13
 * Description:
 */
@Component
public class SysUserRepositoryImpl extends AbstractBaseRepository<SysUserDao,SysUser> implements SysUserRepository {

    /**
     * @Title:
     * @Description: 保存用户信息
     * @param sysUser
     * @return String
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 10:26:22
     */
    @Override
    public String insertOne(SysUser sysUser){
        super.insert(sysUser);
        return sysUser.getId();
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
    @Override
    public List<SysUser> selectByExampleList(Example example){
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
    public PageInfoExtend<SysUser> selectListByExamplePageInfo(Example example, PageQuery pageQuery){
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
    public void updateByPrimaryKeySelective(SysUser sysUser) {
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
    public SysUser selectByPrimaryKey(Object id){
        return super.selectByPrimaryKey(id);
    }

}
