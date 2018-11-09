package cn.com.leadu.cmsxc.system.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.AuthorizationVo;
import cn.com.leadu.cmsxc.pojo.appuser.entity.VehicleAuthorization;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/2/5.
 *
 * 授权操作service
 */
public interface AuthorizationService {
    /**
     * 根据授权状态，分页获取授权信息
     *
     * @param authorizationVo 画面参数
     * @return
     */
    ResponseEntity<RestResponse> findAuthorizationListByPage(AuthorizationVo authorizationVo, SystemUser sysUser);
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
     * 根据授权ID获取授权信息
     *
     * @param authorizationId 授权id
     * @return
     */
    VehicleAuthorization selectByAuthorizationId(String authorizationId);

    /**
     * 根据photoUuid取得全部的图片
     *
     * @param photoUuid 画面参数
     * @return
     */
    ResponseEntity<RestResponse> getPhotoList(String photoUuid);
    /**
     * 授权延期操作
     *
     * @param  delayDate 延期日期
     * @param  authorizationId 授权id
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> delay(String authorizationId, Date delayDate, String remark, SystemUser systemUser);

}
