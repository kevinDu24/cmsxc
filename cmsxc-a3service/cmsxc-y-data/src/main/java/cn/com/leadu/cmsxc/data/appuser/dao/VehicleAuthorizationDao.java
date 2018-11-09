package cn.com.leadu.cmsxc.data.appuser.dao;


import cn.com.leadu.cmsxc.data.base.config.BaseDao;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.AuthorizationVo;
import cn.com.leadu.cmsxc.pojo.appuser.entity.VehicleAuthorization;
import cn.com.leadu.cmsxc.pojo.appuser.vo.AuthorizationListVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.ViewMaterialVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ApprovalListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 单车授权
 */
public interface VehicleAuthorizationDao extends BaseDao<VehicleAuthorization> {
    /**
     * 分页查询指定用户指定授权状态的授权列表
     *
     * @param userId 用户id
     * @param authorizationStatus 授权状态
     * @param page 当前页
     * @param size 每页个数
     * @return
     */
    List<AuthorizationListVo> selectByUserIdAndAuthorizationstatus(@Param("userId") String userId, @Param("authorizationStatus") String authorizationStatus,
                                                                   @Param("nowDate") String nowDate,  @Param("page") int page, @Param("size") int size);
    /**
     * 资料预览
     * @param authorizationId 授权id
     * @return
     */
    List<ViewMaterialVo> selectByAuthorizationId(@Param("authorizationId") String authorizationId);
    /**
     * 根据用户id和任务id获取用户最新申请信息
     * @param taskId 任务id
     * @param userId 用户id
     * @return
     */
    ViewMaterialVo selectByUserIdAndTaskId(@Param("userId") String userId, @Param("taskId") String taskId);

    /**
     * 根据授权状态获取授权列表数据
     *
     * @param authorizationVo 查询条件
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    List<cn.com.leadu.cmsxc.pojo.appbusiness.vo.AuthorizationListVo>  selectByAuthorizationstatus(@Param("authorizationVo") AuthorizationVo authorizationVo,
                                                                                                  @Param("flag") int flag,
                                                                                                  @Param("page") int page,
                                                                                                  @Param("size") int size,
                                                                                                  @Param("leaseId") String leaseId);

    /**
     * 委托公司id获取该委托公司下的待审批数量
     *
     * @param leaseId 委托公司id
     * @return
     */
    Long selectCount1(@Param("leaseId") String leaseId,@Param("userRole") String userRole,@Param("provinces") List<String> provinces);

    /**
     * 分页获取审批列表（app用）
     * @param status 授权
     * @param condition 检索条件
     * @param page
     * @param size
     * @return
     */
    List<ApprovalListVo> selectApprovalList(@Param("userRole") String userRole,@Param("provinces") List<String> provinces, @Param("status") String status,
                                             @Param("condition") String condition,
                                             @Param("leaseId") String leaseId,
                                             @Param("flag") int flag,
                                             @Param("page") int page,
                                             @Param("size") int size);
}
