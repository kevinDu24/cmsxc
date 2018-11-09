package cn.com.leadu.cmsxc.webclient.system.controller;

import cn.com.leadu.cmsxc.common.exception.CmsRpcException;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryCompanyVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryUserListVo;
import cn.com.leadu.cmsxc.webclient.system.rpc.RecoveryUserRpc;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * Created by yuanzhenxia on 2018/2/1.
 *
 * 收车公司用户管理
 */
@RestController
@RequestMapping("recovery_user")
public class RecoveryUserController {
    @Autowired
    private RecoveryUserRpc recoveryUserRpc;

    /**
     * 分页获取收车公司信息
     *
     * @param recoveryUserListVo 画面参数
     * @return
     * @throws CmsRpcException
     */
    @RequestMapping(value="findRecoveryUserByPage",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findRecoveryUserByPage(RecoveryUserListVo recoveryUserListVo) throws CmsRpcException {
        Map authorizationVoMap = recoveryUserListVo == null?null:(Map) JSON.toJSON(recoveryUserListVo);
        return recoveryUserRpc.findRecoveryUserByPage(authorizationVoMap);
    }
    /**
     * 添加新的收车公司
     *
     * @param recovery 画面参数
     * @return
     * @throws CmsRpcException
     */
    @RequestMapping(value="saveRecoveryUser",method = RequestMethod.POST)
    public ResponseEntity<RestResponse> saveRecoveryUser(@RequestBody RecoveryCompanyVo recovery) throws CmsRpcException {
        return recoveryUserRpc.register(recovery);
    }
}
