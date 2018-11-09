package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appbusiness.vo.AuthorizationVo;
import cn.com.leadu.cmsxc.pojo.appuser.entity.VehicleAuthorization;
import cn.com.leadu.cmsxc.pojo.appuser.vo.AuthorizationListVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.ViewMaterialVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ApprovalListVo;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 单车授权
 */
public interface VehicleAuthorizationRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<VehicleAuthorization> selectByExampleList(Example example);

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    VehicleAuthorization selectOneByExample(Example example);

    /**
     * 通过主键获取
     * @param id
     * @return
     */
    public VehicleAuthorization selectByPrimaryKey(String id);
    /**
     * 登录单车授权表信息
     *
     * @param vehicleAuthorization
     */
    void insertOne(VehicleAuthorization vehicleAuthorization);
    /**
     * 分页查询指定用户指定授权状态的授权列表
     *
     * @param userId 用户id
     * @param authorizationStatus 授权状态
     * @param page 当前页
     * @param size 每页个数
     * @return
     */
    List<AuthorizationListVo> selectByUserIdAndAuthorizationstatus(String userId, String authorizationStatus,String nowDate, int page, int size);
    /**
     * 资料预览
     *
     * @param authorizationId 授权id
     * @return
     */
    List<ViewMaterialVo> selectByAuthorizationId(String authorizationId);
    /**
     * 根据用户id和任务id获取用户最新申请信息
     *
     * @param taskId 任务id
     * @param userId 用户id
     * @return
     */
    ViewMaterialVo selectByUserIdAndTaskId( String userId, String taskId);
    /**
     * 根据主键更新表
     *
     * @param vehicleAuthorization
     */
    void updateByPrimaryKeySelective(VehicleAuthorization vehicleAuthorization);
    /**
     * 根据授权状态获取授权列表数据
     *
     * @param authorizationVo 查询条件
     * @return
     */
    List<cn.com.leadu.cmsxc.pojo.appbusiness.vo.AuthorizationListVo>  selectByAuthorizationstatus(AuthorizationVo authorizationVo, int flag, int page, int size, String leaseId);
    /**
     * 委托公司id获取该委托公司下的待审批数量
     *
     * @param leaseId 委托公司id
     * @return
     */
    Long selectCount(String leaseId,String userRole,List<String> provinces);
    /**
     * 分页获取审批列表（app用）
     * @param status 授权
     * @param condition 检索条件
     * @param leaseId 委托公司id
     * @param page
     * @param size
     * @return
     */
    List<ApprovalListVo> selectApprovalList(String userRole,List<String> provinces, String status,
                                                   String condition,
                                                   String leaseId,
                                                   int flag,
                                                   int page,
                                                   int size);
}
