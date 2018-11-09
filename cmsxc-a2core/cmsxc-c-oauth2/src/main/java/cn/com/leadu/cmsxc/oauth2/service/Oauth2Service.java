package cn.com.leadu.cmsxc.oauth2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class Oauth2Service {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ConsumerTokenServices tokenServices;

    public ResponseEntity<String> revokeToken(HttpServletRequest request)
            throws Exception {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return new ResponseEntity<String>("未携带token",HttpStatus.BAD_REQUEST);
        }

        String tokenValue = authHeader.replace("Bearer", "").trim();
        if (tokenServices.revokeToken(tokenValue)) {
            return new ResponseEntity<String>("success",HttpStatus.OK);
        }

        return new ResponseEntity<String>("error",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Void> revokeToken(@PathVariable String user, @PathVariable String token, Principal principal)
            throws Exception {
        checkResourceOwner(user, principal);
        if (tokenServices.revokeToken(token)) {
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
    }

    public Collection<OAuth2AccessToken> listTokensForClient(@PathVariable String client) throws Exception {
        return enhance(tokenStore.findTokensByClientId(client));
    }

    public Collection<OAuth2AccessToken> listTokensForUser(@PathVariable String client, @PathVariable String user,
                                                           Principal principal) throws Exception {
        checkResourceOwner(user, principal);
        return enhance(tokenStore.findTokensByClientIdAndUserName(client, user));
    }

    private void checkResourceOwner(String user, Principal principal) {
        if (principal instanceof OAuth2Authentication) {
            OAuth2Authentication authentication = (OAuth2Authentication) principal;
            if (!authentication.isClientOnly() && !user.equals(principal.getName())) {
                throw new AccessDeniedException(String.format("User '%s' cannot obtain tokens for system '%s'",
                        principal.getName(), user));
            }
        }
    }

    private Collection<OAuth2AccessToken> enhance(Collection<OAuth2AccessToken> tokens) {
        Collection<OAuth2AccessToken> result = new ArrayList<OAuth2AccessToken>();
        for (OAuth2AccessToken prototype : tokens) {
            DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(prototype);
            OAuth2Authentication authentication = tokenStore.readAuthentication(token);
            if (authentication == null) {
                continue;
            }
            String clientId = authentication.getOAuth2Request().getClientId();
            if (clientId != null) {
                Map<String, Object> map = new HashMap<String, Object>(token.getAdditionalInformation());
                map.put("client_id", clientId);
                token.setAdditionalInformation(map);
                result.add(token);
            }
        }
        return result;
    }

    public Integer getTokenExpiresIn(Principal principal){
        if (principal instanceof OAuth2Authentication) {
            OAuth2Authentication authentication = (OAuth2Authentication) principal;
            return tokenStore.getAccessToken(authentication).getExpiresIn();
        }else{
            return null;
        }
    }

}
