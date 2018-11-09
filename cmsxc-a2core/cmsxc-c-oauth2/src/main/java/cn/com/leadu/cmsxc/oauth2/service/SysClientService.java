package cn.com.leadu.cmsxc.oauth2.service;

import cn.com.leadu.cmsxc.oauth2.common.utils.ArrayUtil;
import cn.com.leadu.cmsxc.oauth2.dao.SysClientDao;
import cn.com.leadu.cmsxc.oauth2.entity.SysClient;
import cn.com.leadu.cmsxc.oauth2.vo.SysClientVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SysClientService implements ClientDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(SysClientService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SysClientDao sysClientDao;

    /**
     * @Title:
     * @Description:
     * @param clientId
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/07 08:12:53
     */
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        try {
            SysClient sysClient = null;
            Example example = new Example(SysClient.class);
            example.createCriteria()
                    .andEqualTo("clientId",clientId);
            List<SysClient> sysClients = sysClientDao.selectByExample(example);
            if(ArrayUtil.isNotNullAndLengthNotZero(sysClients))
                sysClient = sysClients.get(0);

            if (sysClient == null) {
                throw new ClientRegistrationException(String.format("%s客户端id不存在", clientId));
            }

            SysClientVo sysClientVo = objectMapper.convertValue(sysClient, SysClientVo.class);
            if (sysClientVo == null) {
                throw new ClientRegistrationException(String.format("%s客户端id不存在", clientId));
            }

            if(sysClient.getScopes() != null){
                Set<String> scopes = new HashSet<>();
                for(String scope : sysClient.getScopes().split(",")){
                    scopes.add(scope);
                }
                sysClientVo.setScope(scopes);
            }

            if (sysClient.getRoles() != null) {
                List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
                for (String role : sysClient.getRoles().split(",")) {
                    authorities.add(new SimpleGrantedAuthority(role));
                }
                sysClientVo.setAuthorities(authorities);
            }

            return sysClientVo;

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw e;
        }
    }

}
