package cn.com.leadu.cmsxc.system.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.LeaseCompany;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.LeaseCompanyVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.LeaseUserListVo;
import org.springframework.http.ResponseEntity;

/**
 * Created by yuanzhenxia on 2018/2/2.
 *
 * 委托公司用户管理Service
 */
public interface LeaseUserService {
    /**
     * 收车公司用户注册
     *
     * @param  LeaseCompanyVo 委托公司用户注册信息
     * @return
     */
    ResponseEntity<RestResponse> register(LeaseCompanyVo LeaseCompanyVo);
    /**
     * 获取委托公司信息
     *
     * @return
     */
    public  ResponseEntity<RestResponse> getLeaseUser();
    /**
     * 根据委托公司简称或全称，分页获取委托公司信息
     *
     * @param LeaseUserListVo 画面参数
     * @return
     */
    ResponseEntity<RestResponse> findLeaseUserByPage(LeaseUserListVo LeaseUserListVo);
    /**
     * 根据工单表中的金融公司用户名获取委托公司信息
     *
     * @pame 用户名
     * @return
     */
    public LeaseCompany findByUserName(String userName);

    /**
     * 根据委托公司id删除委托公司
     * @param leaseId
     * @return
     */
    ResponseEntity<RestResponse> deleteLeaseCompany(String leaseId);
}
