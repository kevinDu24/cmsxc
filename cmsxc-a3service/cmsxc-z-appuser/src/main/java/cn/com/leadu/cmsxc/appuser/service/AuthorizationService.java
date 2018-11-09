package cn.com.leadu.cmsxc.appuser.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appuser.entity.VehicleAuthorization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 授权Service
 */
public interface AuthorizationService {
    /**
     * 申请授权
     *
     * @param userId 用戶id
     * @param paramterMap 参数集合（车牌号，申请人姓名，申请人电话，申请人证件号）
     * @return
     */
    ResponseEntity<RestResponse> applyAuthorization(String userId, Map<String,String> paramterMap);
    /**
     * 分页查询指定用户指定授权状态的授权列表
     *
     * @param userId 用户id
     * @param authorizationStatus 授权状态
     * @param page 当前页
     * @param size 每页个数
     * @return
     */
    ResponseEntity<RestResponse> getApplyAuthorizationList(String userId, String authorizationStatus, int page, int size);
    /**
     * 申请资料预览
     *
     * @param authorizationId 授权状态
     * @return
     */
    ResponseEntity<RestResponse> viewMaterial( String authorizationId);
    /**
     * 取消申请授权
     *
     * @param userId 用户id
     * @param authorizationId 授权id
     * @return
     */
    ResponseEntity<RestResponse> cancelApplyAuthorization(String userId, String authorizationId);
    /**
     * 根据授权ID获取授权信息
     *
     * @param authorizationId 授权id
     * @return
     */
    VehicleAuthorization selectByAuthorizationId(String authorizationId);
    /**
     * 根据任务工单id查询该车辆有效的授权信息
     * @param taskId 任务工单id
     * @return
     */
    VehicleAuthorization selectByTaskId(Long taskId);
    /**
     * 根据任务工单id和用户id获取该用户最新授权信息
     * @param taskId 任务工单id
     * @return
     */
    VehicleAuthorization selectByTaskIdAndUserId(Long taskId, String userId);
    /**
     * 编辑资料
     *
     * @param userId 用户id
     * @param paramterMap 参数集合（授权id，车牌号，申请人姓名，申请人电话，申请人证件号）
     * @param photoMsg1 图片1
     * @param photoMsg2 图片2
     * @param photoMsg3 图片3
     * @param photoMsg4 图片4
     * @param photoMsg5 图片5
     * @return
     */
    ResponseEntity<RestResponse> editAuthorization(String userId, Map<String,String> paramterMap, MultipartFile photoMsg1, MultipartFile photoMsg2,
                                                          MultipartFile photoMsg3, MultipartFile photoMsg4, MultipartFile photoMsg5);

    /**
     * 文件下载
     *
     * @param response 网页请求
     * @param fileName 文件名
     * @param loadDate 上传时间
     */
    void downloadFile(HttpServletResponse response, String fileName, String loadDate);

    /**
     * 上传文件
     *
     * @param file 线索图片
     * @return
     */
    ResponseEntity<RestResponse> uploadFile( MultipartFile file);

    /**
     * 申请授权前，获取此用户针对此车辆之前的最新申请信息
     *
     * @param userId 用户id
     * @param plate 车牌号
     * @return
     */
    ResponseEntity<RestResponse> beforeApplyAuthorization(String userId, String plate);

}
