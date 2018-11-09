package cn.com.leadu.cmsxc.appuser.service.impl;

import cn.com.leadu.cmsxc.appuser.service.RecoveryTaskServiceOld;
import cn.com.leadu.cmsxc.appuser.service.SystemUserService;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.appbusiness.repository.VehicleTaskRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryTaskListVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryTaskVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.RecoveryVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/2/12.
 *
 * 收车公司派单任务实现类_一期老接口
 */
@Service
public class RecoveryTaskServiceOldImpl implements RecoveryTaskServiceOld {
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private VehicleTaskRepository vehicleTaskRepository;
    /**
     * 获取各状态收车公司任务派单数量
     *
     * @param userId 用户id
     * @return
     */
    public ResponseEntity<RestResponse> getRecoveryTaskCountByStatus(String userId){
        RecoveryVo recoveryVo = new RecoveryVo();
        RecoveryTaskListVo recoveryTaskListVo = new RecoveryTaskListVo();
        // 获取当前登录用户信息
        SystemUser systemUser = systemUserService.selectSystemUserByUserId(userId);
        if(systemUser == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","当前用户信息不存在"),
                    HttpStatus.OK);
        }
        if(StringUtil.isNull(systemUser.getRecoveryCompanyId())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","数据加载完毕"),
                    HttpStatus.OK);
        }
        // 分页获取收车公司派单任务列表
        recoveryVo.setRecoveryCompanyId(systemUser.getRecoveryCompanyId());
        // 设置当前页为0
        recoveryVo.setPage(0);
        // 设置每页数量为0
        recoveryVo.setSize(0);
        // 设置状态为1：待收车
        recoveryVo.setStatus(1);
        // 获取待收车数量
        List<RecoveryTaskVo> waitList = vehicleTaskRepository.selectRecoveryTaskListOld(recoveryVo);
        if(waitList != null && !waitList.isEmpty()){
            recoveryTaskListVo.setWaitCount(waitList.size());
        }
        // 设置状态为2：已授权
        recoveryVo.setStatus(2);
        // 获取已授权数量
        List<RecoveryTaskVo> authorizationList = vehicleTaskRepository.selectRecoveryTaskListOld(recoveryVo);
        if(authorizationList != null && !authorizationList.isEmpty()){
            recoveryTaskListVo.setAuthorizationCount(authorizationList.size());
        }
        // 设置状态为3：已完成
        recoveryVo.setStatus(3);
        // 获取待收车数量
        List<RecoveryTaskVo> finishList = vehicleTaskRepository.selectRecoveryTaskListOld(recoveryVo);
        if(finishList != null && !finishList.isEmpty()){
            recoveryTaskListVo.setFinishCount(finishList.size());
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,recoveryTaskListVo,""),
                HttpStatus.OK);
    }

    /**
     * 分页获取收车公司任务派单列表
     *
     * @param recoveryVo 画面参数
     * @param userId 用户id
     * @return
     */
   public ResponseEntity<RestResponse> getRecoveryTaskList(String userId, RecoveryVo recoveryVo){
       // 获取当前登录用户信息
       SystemUser systemUser = systemUserService.selectSystemUserByUserId(userId);
       if(systemUser == null){
           return new ResponseEntity<RestResponse>(
                   RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","当前用户信息不存在"),
                   HttpStatus.OK);
       }
       if(StringUtil.isNull(systemUser.getRecoveryCompanyId())){
           return new ResponseEntity<RestResponse>(
                   RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","数据加载完毕"),
                   HttpStatus.OK);
       }
       // 分页获取收车公司派单任务列表
       recoveryVo.setRecoveryCompanyId(systemUser.getRecoveryCompanyId());
       List<RecoveryTaskVo> recoveryTaskVoList = vehicleTaskRepository.selectRecoveryTaskListOld(recoveryVo);
       if(recoveryTaskVoList != null && !recoveryTaskVoList.isEmpty()){
           return new ResponseEntity<RestResponse>(
                   RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,recoveryTaskVoList,""),
                   HttpStatus.OK);
       // 如果根据车牌号查询，查询不到数据，返回为查询到车辆信息
       }else if("0".equals(String.valueOf(recoveryVo.getStatus()))){
           return new ResponseEntity<RestResponse>(
                   RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未查询到车辆信息"),
                   HttpStatus.OK);
       }
       return new ResponseEntity<RestResponse>(
               RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","数据加载完毕"),
               HttpStatus.OK);
   }
}
