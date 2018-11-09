package cn.com.leadu.cmsxc.oauth2.service;

import cn.com.leadu.cmsxc.oauth2.common.utils.ArrayUtil;
import cn.com.leadu.cmsxc.oauth2.dao.SysUserDao;
import cn.com.leadu.cmsxc.oauth2.entity.SysUser;
import cn.com.leadu.cmsxc.oauth2.vo.SysUserVo;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class SysUserService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysUserService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SysUserDao sysUserDao;

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

            SysUser sysUser = null;
            Example example = new Example(SysUser.class);
//            example.setOrderByClause(" sort asc ");
            example.createCriteria()
                    .andEqualTo("username",username);
            List<SysUser> sysUsers = sysUserDao.selectByExample(example);
            if(ArrayUtil.isNotNullAndLengthNotZero(sysUsers))
                sysUser = sysUsers.get(0);

            if (sysUser == null) {
                throw new UsernameNotFoundException(String.format("%s用户名不存在", username));
            }

            SysUserVo sysUserVo = objectMapper.convertValue(sysUser, SysUserVo.class);
            if (sysUserVo == null) {
                throw new UsernameNotFoundException(String.format("%s用户名不存在", username));
            }
//
//            if (sysUser.getRoles() != null) {
//                List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//                for (SysRole sysRole : sysUser.getRoles()) {
//                        authorities.add(new SimpleGrantedAuthority(sysRole.getName()));
//                }
//                sysUserVo.setAuthorities(authorities);
//            }
            return sysUserVo;

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw ex;
        }

    }
}
