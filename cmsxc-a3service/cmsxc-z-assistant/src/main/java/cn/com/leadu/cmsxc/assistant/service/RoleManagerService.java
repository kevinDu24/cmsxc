package cn.com.leadu.cmsxc.assistant.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.assistant.vo.EditAuditorVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 角色管理Service
 */
public interface RoleManagerService {


    /**
     * 获取角色列表
     * @param leaseId 用户id
     * @return
     */
    ResponseEntity<RestResponse> getUserList(String leaseId);

    /**
     * 获取已选择的省份集合
     * @param id 用户表主键
     * @return
     */
    ResponseEntity<RestResponse> getProvinces(String id);

    /**
     * 新增审核人员
     * @param vo 审核人员信息参数
     * @return
     */
    ResponseEntity<RestResponse> addAuditor(EditAuditorVo vo, SystemUser loginUser);

    /**
     * 编辑审核人员
     * @param vo 审核人员信息参数
     * @return
     */
    ResponseEntity<RestResponse> editAuditor(EditAuditorVo vo, SystemUser loginUser);

    /**
     * 禁用接口
     * @param disableUserId 被禁用审核人员账号
     * @return
     */
    @Transactional
    ResponseEntity<RestResponse> disableAuditor( SystemUser loginUser,String disableUserId);

}
