package cn.com.leadu.cmsxc.appuser.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskRecovery;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.AssignedTaskVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.CancelAssignTaskVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.RecoveryDetailVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.RecoveryVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/2/12.
 *
 * 收车公司派单任务service
 */
public interface RecoveryTaskService {

    /**
     * 获取收车公司任务派单列表
     *
     * @param recoveryVo 画面参数
     * @param systemUser 用户id
     * @return
     */
    ResponseEntity<RestResponse> getRecoveryTaskList(SystemUser systemUser, RecoveryVo recoveryVo);
    /**
     * 获取各状态收车公司任务派单数量
     *
     * @param systemUser 用户id
     * @return
     */
    ResponseEntity<RestResponse> getRecoveryTaskCountByStatus(SystemUser systemUser);
    /**
     * 根据任务id和收车公司id获取收车公司派单
     *
     * @param taskId 任务id
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    VehicleTaskRecovery findRecoveryTaskByTaskIdAndRecoveryCompanyId(String taskId, String recoveryCompanyId);

    /**
     * 根据任务id获取该任务的详情信息
     *
     * @param recoveryDetailVo 参数信息
     * @param userId 用户id
     * @return
     */
    ResponseEntity<RestResponse> findRecoveryTaskDetail( String userId,RecoveryDetailVo recoveryDetailVo);

    /**
     * 根据用户名获取该用户所属小组id
     *
     * @return
     */
    public String getGroupIdByUserId(String userId);

    /**
     * 派单管理_获取各个状态数量
     *
     * @param systemUser 用户信息
     * @return
     */
    public ResponseEntity<RestResponse> getTaskManagerCount(SystemUser systemUser);

    /**
     * 分页获取派单管理列表_未处理
     *
     * @param recoveryVo 画面参数
     * @param systemUser 用户信息
     * @return
     */
    public ResponseEntity<RestResponse> getTaskManagerList(SystemUser systemUser, RecoveryVo recoveryVo);

    /**
     * 获取所有小组及该单的分配信息接口
     *
     * @param taskId 工单id
     * @param systemUser 用户信息
     * @return
     */
    public ResponseEntity<RestResponse> getAssginInfo(SystemUser systemUser, String taskId);
    /**
     * 根据id获取收车公司派单
     *
     * @param idList 收车任务主键
     * @return
     */
    List<VehicleTaskRecovery> getRecoveryTaskByIds(List<String> idList);
    /**
     * 分配任务
     *
     * @param vo 任务分配参数
     * @return
     */
    public ResponseEntity<RestResponse> assginTask(SystemUser systemUser, AssignedTaskVo vo);
    /**
     * 取消分配任务
     *
     * @param vo 任务分配参数
     * @return
     */
    @Transactional
    ResponseEntity<RestResponse> cancelAssginTask(SystemUser systemUser, CancelAssignTaskVo vo);

}
