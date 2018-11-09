package cn.com.leadu.cmsxc.webclient.system.controller;

import cn.com.leadu.cmsxc.common.exception.CmsRpcException;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.AuthorizationVo;
import cn.com.leadu.cmsxc.pojo.system.vo.AuthorizationOperateVo;
import cn.com.leadu.cmsxc.webclient.system.rpc.AuthorizationRpc;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by yuanzhenxia on 2018/2/5.
 *
 * 授权操作
 */
@RestController
@RequestMapping("authorization")
public class AuthorizationController {
    @Autowired
    private AuthorizationRpc authorizationRpc;
    /**
     * 分页获取授权信息信息
     *
     * @param authorizationVo 画面参数
     * @return
     * @throws CmsRpcException
     */
    @RequestMapping(value="findAuthorizationListByPage",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findAuthorizationListByPage(AuthorizationVo authorizationVo) throws CmsRpcException {
        Map authorizationVoMap = authorizationVo == null?null:(Map) JSON.toJSON(authorizationVo);
        return authorizationRpc.findAuthorizationListByPage(authorizationVoMap);
    }
    /**
     * 授权申请操作
     *
     * @param vo 画面参数
     * @return
     * @throws CmsRpcException
     */
    @RequestMapping(value="authorization",method = RequestMethod.POST)
    public ResponseEntity<RestResponse> authorization(@RequestBody AuthorizationOperateVo vo) throws CmsRpcException {
        return authorizationRpc.authorization(vo);
    }
    /**
     * 拒绝申请操作
     *
     * @param vo 画面参数
     * @return
     * @throws CmsRpcException
     */
    @RequestMapping(value="refuse",method = RequestMethod.POST)
    public ResponseEntity<RestResponse> refuse(@RequestBody AuthorizationOperateVo vo) throws CmsRpcException {
        return authorizationRpc.refuse(vo);
    }

    /**
     * 授权延期操作
     *
     * @param vo 画面参数
     * @return
     * @throws CmsRpcException
     */
    @RequestMapping(value="delay",method = RequestMethod.POST)
    public ResponseEntity<RestResponse> delay(@RequestBody AuthorizationOperateVo vo) throws CmsRpcException {
        return authorizationRpc.delay(vo);
    }

    /**
     * 根据photoUuid取得图片信息
     *
     * @param photoUuid 画面参数
     * @return
     * @throws CmsRpcException
     */
    @RequestMapping(value="getPhotoList",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getPhotoList(String photoUuid) throws CmsRpcException {
        return authorizationRpc.getPhotoList(photoUuid);
    }
}
