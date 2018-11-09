package cn.com.leadu.cmsxc.oauth2.service;

import cn.com.leadu.cmsxc.oauth2.common.utils.ArrayUtil;
import cn.com.leadu.cmsxc.oauth2.dao.SystemUserDao;
import cn.com.leadu.cmsxc.oauth2.entity.SystemUser;
import cn.com.leadu.cmsxc.oauth2.vo.SysUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author qiaomengnan
 * @ClassName: SysUserService
 * @Description: 用户service
 * @date 2018/1/7
 */
public class SystemUserService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemUserService.class);

    @Autowired
    private SystemUserDao systemUserDao;

    /**
     * @Title:
     * @Description:
     * @param username
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/07 08:12:30
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {

            SystemUser systemUser = null;
            Example example = new Example(SystemUser.class);
            example.createCriteria()
                    .andEqualTo("userId",username);
            List<SystemUser> systemUsers = systemUserDao.selectByExample(example);
            if(ArrayUtil.isNotNullAndLengthNotZero(systemUsers))
                systemUser = systemUsers.get(0);
            if (systemUser == null) {
                throw new UsernameNotFoundException(String.format("%s用户名不存在", username));
            }

            SysUserVo sysUserVo = new SysUserVo(systemUser);
            if (sysUserVo == null) {
                throw new UsernameNotFoundException(String.format("%s用户名不存在", username));
            }
            return sysUserVo;

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw ex;
        }
    }
}
