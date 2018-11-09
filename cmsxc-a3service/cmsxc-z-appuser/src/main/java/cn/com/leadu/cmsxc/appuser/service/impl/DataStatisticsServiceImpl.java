package cn.com.leadu.cmsxc.appuser.service.impl;

import cn.com.leadu.cmsxc.appuser.service.DataStatisticsService;
import cn.com.leadu.cmsxc.appuser.service.SystemUserService;
import cn.com.leadu.cmsxc.common.util.ArrayUtil;
import cn.com.leadu.cmsxc.data.appbusiness.repository.LeaseCompanyRepository;
import cn.com.leadu.cmsxc.data.appbusiness.repository.VehicleTaskRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appuser.vo.DataStatisticsParamVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.DataStatisticsSubVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.DataStatisticsVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.LeaseCompanysVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/23.
 *
 * 数据统计impl
 */
@Service
public class DataStatisticsServiceImpl implements DataStatisticsService{
    private static final Logger logger = LoggerFactory.getLogger(DataStatisticsServiceImpl.class);
    @Autowired
    private LeaseCompanyRepository leaseCompanyRepository;
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private VehicleTaskRepository vehicleTaskRepository;
    /**
     * 根据收车公司id获取合作委托公司信息
     *
     * @param systemUser 用户信息
     * @return
     */
    public ResponseEntity<RestResponse> getLeaseCompanys(SystemUser systemUser) {
        List<LeaseCompanysVo> leaseCompanysVoList = new ArrayList();
        leaseCompanysVoList = leaseCompanyRepository.selectLeaseCompanysByRecoveryCompanyId(systemUser.getRecoveryCompanyId());
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, leaseCompanysVoList, ""),
                HttpStatus.OK);
    }

    /**
     * 统计信息
     *
     * @param systemUser 用户信息
     * @return
     */
    public ResponseEntity<RestResponse> getDataStatistics(SystemUser systemUser, DataStatisticsParamVo dataStatisticsParamVo){
        DataStatisticsVo dataStatisticsVo = new DataStatisticsVo();
        // 获取收车公司所有业务员和内勤人员(内勤人员也可以申请授权并获取授权)
        List<String> users = new ArrayList<>();
        // 获取公司所有人员
        List<SystemUser> sysUserList = systemUserService.selectByRecoveryCompanyId(systemUser.getRecoveryCompanyId());
        for(SystemUser sysUser : sysUserList){
            users.add(sysUser.getUserId());
        }
        dataStatisticsParamVo.setUsers(users);
        // 派单任务总量及总服务费
        DataStatisticsSubVo selfFinish = vehicleTaskRepository.selectSelfFinish(dataStatisticsParamVo, systemUser.getRecoveryCompanyId());
        if(selfFinish != null){
            // 派单完成总量
            dataStatisticsVo.setSelfSumCount(selfFinish.getSumCount());
            // 总服务费
            dataStatisticsVo.setSumFee(selfFinish.getSumFee());
            // 完成总量
            dataStatisticsVo.setSumCount(selfFinish.getSumCount());
        }
        // 取在授权表中不在派单表中的自己公司业务员完成的任务总量及总服务费
        DataStatisticsSubVo othersFinish = vehicleTaskRepository.selectOthersFinish(dataStatisticsParamVo, systemUser.getRecoveryCompanyId());
        if(othersFinish != null){
            // 悬赏列表完成总量
            dataStatisticsVo.setOthersSumCount(othersFinish.getSumCount());
            // 总服务费
            if(othersFinish.getSumFee() != null && dataStatisticsVo.getSumFee() != null){
                dataStatisticsVo.setSumFee(othersFinish.getSumFee() + dataStatisticsVo.getSumFee());
            }else if(othersFinish.getSumFee() != null){
                dataStatisticsVo.setSumFee(othersFinish.getSumFee());
            }
            // 完成总量
            if(othersFinish.getSumCount() != null && dataStatisticsVo.getSumCount() != null){
                dataStatisticsVo.setSumCount(othersFinish.getSumCount() + dataStatisticsVo.getSumCount());
            }else if(othersFinish.getSumCount() != null){
                dataStatisticsVo.setSumCount(othersFinish.getSumCount());
            }
        }
        List<DataStatisticsSubVo>  dataStatisticsSubVoList = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.##%");
        // 派单任务及悬赏任务总量及总服务费 -- 小组
        List<DataStatisticsSubVo> groupFinishList = vehicleTaskRepository.selectGroupFinish(dataStatisticsParamVo, systemUser.getRecoveryCompanyId());
        // 求出各小组完成总量占比
        if(ArrayUtil.isNotNullAndLengthNotZero(groupFinishList)){
            DataStatisticsSubVo dataStatisticsSubVo = null;
            for(DataStatisticsSubVo vo : groupFinishList){
                // 計算佔比，保留兩位小數
                double proportion = (double)vo.getSumCount()/(double)dataStatisticsVo.getSumCount();
                // 轉化為百分比
                vo.setProportion(df.format(proportion));
                // 取出“其他”分組的，排列在最後
                if("其他".equals(vo.getGroupName())){
                    dataStatisticsSubVo = vo;
                    continue;
                }
                dataStatisticsSubVoList.add(vo);
            }
            // 如果存在“其他”，添加在列表的最後
            if(dataStatisticsSubVo != null){
                dataStatisticsSubVoList.add(dataStatisticsSubVo);
            }
            dataStatisticsVo.setDataStatisticsSubVo(dataStatisticsSubVoList);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, dataStatisticsVo, ""),
                HttpStatus.OK);

    }


}
