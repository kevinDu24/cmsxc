package cn.com.leadu.cmsxc.oauth2.dao;

import cn.com.leadu.cmsxc.oauth2.config.CmsOauth2BaseDao;
import cn.com.leadu.cmsxc.oauth2.entity.SysRole;
import cn.com.leadu.cmsxc.oauth2.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author qiaomengnan
 * @ClassName: SysUserDao
 * @Description:
 * @date 2018/1/7
 */
public interface SysUserDao extends CmsOauth2BaseDao<SysUser> {

    List<SysRole> selectByUserIdRole(String userId);

    List<String> selectBySysUserRoleId(String userId);

    List<String> selectBySysUserRes(@Param("roleIds") List<String> roleIds);


}
