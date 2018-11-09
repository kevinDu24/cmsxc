package cn.com.leadu.cmsxc.appuser.controller;

import cn.com.leadu.cmsxc.appuser.service.RecoveryTaskService;
import cn.com.leadu.cmsxc.appuser.service.RecoveryTaskServiceOld;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.AssignedTaskVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.CancelAssignTaskVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.RecoveryDetailVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.RecoveryVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by yuanzhenxia on 2018/2/12.
 *
 * 收车公司派单任务
 */
@RestController
@RequestMapping("recoverytask")
public class RecoveryTaskController {
    private static final Logger logger = LoggerFactory.getLogger(RecoveryTaskController.class);
    @Autowired
    private RecoveryTaskService recoveryTaskService;
    @Autowired
    private RecoveryTaskServiceOld recoveryTaskServiceOld;
    /**
     * 获取各状态收车公司任务派单数量_一期老接口
     *
     * @param systemUser 当前用户
     * @return
     */
    @RequestMapping(value = "/recoveryTaskCountByStatus", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getRecoveryTaskCountByStatus(@AuthUserInfo SystemUser systemUser) {
        try{
            return recoveryTaskServiceOld.getRecoveryTaskCountByStatus(systemUser.getUserId());
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取各状态收车公司任务派单数量error",ex);
            throw new CmsServiceException("获取各状态收车公司任务派单数量失败");
        }
    }

    /**
     * 分页获取收车公司任务派单列表_一期老接口
     *
     * @param systemUser 当前用户
     * @param recoveryVo 画面参数
     * @return
     */
    @RequestMapping(value = "/recoveryTaskList", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> getRecoveryTaskList(@AuthUserInfo SystemUser systemUser, @RequestBody RecoveryVo recoveryVo) {
        try{
            return recoveryTaskServiceOld.getRecoveryTaskList(systemUser.getUserId() , recoveryVo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("分页获取收车公司任务派单列表error",ex);
            throw new CmsServiceException("分页获取收车公司任务派单列表失败");
        }
    }

    /**
     * 获取各状态收车公司任务派单数量
     *
     * @param systemUser 当前用户
     * @return
     */
    @RequestMapping(value = "/recoveryTaskCountByStatusNew", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> recoveryTaskCountByStatusNew(@AuthUserInfo SystemUser systemUser) {
        try{
            return recoveryTaskService.getRecoveryTaskCountByStatus(systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取各状态收车公司任务派单数量error",ex);
            throw new CmsServiceException("获取各状态收车公司任务派单数量失败");
        }
    }

    /**
     * 分页获取收车公司任务派单列表
     *
     * @param systemUser 当前用户
     * @param recoveryVo 画面参数
     * @return
     */
    @RequestMapping(value = "/recoveryTaskListNew", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> recoveryTaskListNew(@AuthUserInfo SystemUser systemUser, @RequestBody RecoveryVo recoveryVo) {
        try{
            return recoveryTaskService.getRecoveryTaskList(systemUser , recoveryVo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("分页获取收车公司任务派单列表error",ex);
            throw new CmsServiceException("分页获取收车公司任务派单列表失败");
        }
    }

    /**
     * 派单详情
     *
     * @param systemUser 当前用户
     * @param recoveryDetailVo 画面参数
     * @return
     */
    @RequestMapping(value = "/recoveryTaskDetail", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> findRecoveryTaskDetail(@AuthUserInfo SystemUser systemUser, @RequestBody RecoveryDetailVo recoveryDetailVo) {
        try{
            return recoveryTaskService.findRecoveryTaskDetail(systemUser.getUserId(), recoveryDetailVo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("分页获取收车公司任务派单列表error",ex);
            throw new CmsServiceException("分页获取收车公司任务派单列表失败");
        }
    }

    /**
     * 派单管理_获取各个状态数量
     *
     * @param systemUser 用户信息
     * @return
     */
    @RequestMapping(value = "/getTaskManagerCount", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getTaskManagerCount(@AuthUserInfo SystemUser systemUser) {
        try{
            return recoveryTaskService.getTaskManagerCount(systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("派单管理_获取各个状态数量error",ex);
            throw new CmsServiceException("派单管理_获取各个状态数量失败");
        }
    }

    /**
     * 分页获取派单管理列表
     *
     * @param systemUser 用户信息
     * @return
     */
    @RequestMapping(value = "/getTaskManagerList", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> getTaskManagerList(@AuthUserInfo SystemUser systemUser, @RequestBody RecoveryVo recoveryVo) {
        try{
            return recoveryTaskService.getTaskManagerList(systemUser, recoveryVo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("分页获取派单管理列表error",ex);
            throw new CmsServiceException("分页获取派单管理列表失败");
        }
    }

    /**
     * 获取所有小组及该单的分配信息接口
     *
     * @param taskId
     * @param systemUser 用户信息
     * @return
     */
    @RequestMapping(value = "/getAssginInfo", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getAssginInfo(@AuthUserInfo SystemUser systemUser, String taskId) {
        try{
            return recoveryTaskService.getAssginInfo(systemUser, taskId);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取所有小组及该单的分配信息接口error",ex);
            throw new CmsServiceException("获取所有小组及该单的分配信息接口失败");
        }
    }

    /**
     * 分配任务
     *
     * @param vo 画面传参
     * @param systemUser 用户信息
     * @return
     */
    @RequestMapping(value = "/assginTask", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> assginTask(@AuthUserInfo SystemUser systemUser, @RequestBody AssignedTaskVo vo) {
        try{
            return recoveryTaskService.assginTask(systemUser, vo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取所有小组及该单的分配信息接口error",ex);
            throw new CmsServiceException("获取所有小组及该单的分配信息接口失败");
        }
    }
    /**
     * 取消分配任务
     *
     * @param vo 画面传参
     * @param systemUser 用户信息
     * @return
     */
    @RequestMapping(value = "/cancelAssginTask", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> cancelAssginTask(@AuthUserInfo SystemUser systemUser, @RequestBody CancelAssignTaskVo vo) {
        try{
            return recoveryTaskService.cancelAssginTask(systemUser, vo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("取消分配任务error",ex);
            throw new CmsServiceException("取消分配任务失败");
        }
    }

}
