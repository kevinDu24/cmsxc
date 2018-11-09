package cn.com.leadu.cmsxc.webclient.system.controller;

import cn.com.leadu.cmsxc.common.exception.CmsRpcException;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.LeaseCompanyVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.LeaseUserListVo;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import cn.com.leadu.cmsxc.webclient.system.rpc.LeaseUserRpc;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by yuanzhenxia on 2018/2/2.
 *
 * 委托公司用户管理
 */
@RestController
@RequestMapping("lease_user")
public class LeaseUserController {
    @Autowired
    private LeaseUserRpc LeaseUserRpc;
    /**
     * 分页获取委托公司信息
     *
     * @param LeaseUserListVo 画面参数
     * @return
     * @throws CmsRpcException
     */
    @RequestMapping(value="findLeaseUserByPage",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findLeaseUserByPage(LeaseUserListVo LeaseUserListVo) throws CmsRpcException {
        Map authorizationVoMap = LeaseUserListVo == null?null:(Map) JSON.toJSON(LeaseUserListVo);
        return LeaseUserRpc.findLeaseUserByPage(authorizationVoMap);
    }
    /**
     * 获取委托公司信息
     *
     * @return
     * @throws CmsRpcException
     */
    @RequestMapping(value="getLeaseUser",method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getLeaseUser() throws CmsRpcException {
        return LeaseUserRpc.getLeaseUser();
    }
    /**
     * 添加新的委托公司
     *
     * @param Lease 画面参数
     * @return
     * @throws CmsRpcException
     */
    @RequestMapping(value="saveLeaseUser",method = RequestMethod.POST)
    public ResponseEntity<RestResponse> saveLeaseUser(@RequestBody LeaseCompanyVo Lease) throws CmsRpcException {
        return LeaseUserRpc.register(Lease);
    }
}
