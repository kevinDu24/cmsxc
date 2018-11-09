package cn.com.leadu.cmsxc.webclient.oauth2.controller;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserVo;
import cn.com.leadu.cmsxc.webclient.config.CmsWebClientProperties;
import cn.com.leadu.cmsxc.webclient.oauth2.rpc.Oauth2Rpc;
import cn.com.leadu.cmsxc.webclient.util.EncodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

/**
 * @author qiaomengnan
 * @ClassName: Oauth2Controller
 * @Description: 获取token结构
 * @date 2018/1/9
 */
@RestController
@RequestMapping("oauth2")
public class Oauth2Controller {

    /**
     * @Fields : 鉴权rpc
     */
    @Autowired
    private Oauth2Rpc oauth2Rpc;

    /**
     * @Fields : 项目文件配置
     */
    @Autowired
    private CmsWebClientProperties cmsWebClientProperties;

    /**
     * @Title:
     * @Description:  用户通过账户密码登录获取token
     * @param sysUserVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 12:12:02
     */
    @RequestMapping(value = "get_token" , method = RequestMethod.POST)
    public ResponseEntity<RestResponse> getToken(@RequestBody SysUserVo sysUserVo) throws UnsupportedEncodingException{
        return oauth2Rpc.oauthToken(cmsWebClientProperties.getGrantType(),
                sysUserVo.getUserName(),
                EncodeUtils.MD5(EncodeUtils.getBase64(sysUserVo.getPassword())),
                cmsWebClientProperties.getBasic());

    }

}
