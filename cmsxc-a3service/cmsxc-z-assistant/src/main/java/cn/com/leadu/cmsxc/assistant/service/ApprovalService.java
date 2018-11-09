package cn.com.leadu.cmsxc.assistant.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ApprovalDetailSearchVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yuanzhenxia on 2018/1/15.
 *
 * 申请审批service
 */
public interface ApprovalService {

    /**
     * 获取待审批数量
     *
     * @param systemUser 用户信息
     * @return
     */
    ResponseEntity<RestResponse> getCount(SystemUser systemUser);

    /**
     * 分页获取审批列表（app用）
     * @param status 授权
     * @param condition 检索条件
     * @param flag "0":分页查询，"1"：搜索查询
     * @param page
     * @param size
     * @return
     */
    ResponseEntity<RestResponse> getApprovalList(SystemUser systemUser,
                                                        String status,
                                                        String condition,
                                                        int flag,
                                                        int page,
                                                        int size);

    /**
     * 审批详情
     *
     * @param param 入参
     * @return
     */
    ResponseEntity<RestResponse> getApprovalDetail(ApprovalDetailSearchVo param);

    /**
     * 授权申请操作
     *
     * @param  remark 备注
     * @param  authorizationId 授权id
     * @param  userId 用户id
     * @param  plate 车牌号
     * @return
     */
    ResponseEntity<RestResponse> authorization(String authorizationId, String remark,String userId, String plate, SystemUser sysUser);

    /**
     * 拒绝申请操作
     *
     * @param  remark 备注
     * @param  authorizationId 授权id
     * @return
     */
    ResponseEntity<RestResponse> refuse(String authorizationId, String remark, String loginUser);

    /**
     * 授权延期操作
     *
     * @param  date 授权截止日期
     * @param  authorizationId 授权id
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> delay(String authorizationId, String date, String remark, SystemUser systemUser);
}
