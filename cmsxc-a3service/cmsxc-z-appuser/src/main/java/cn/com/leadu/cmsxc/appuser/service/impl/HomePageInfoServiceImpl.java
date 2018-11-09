package cn.com.leadu.cmsxc.appuser.service.impl;

import cn.com.leadu.cmsxc.appuser.service.HomePageInfoService;
import cn.com.leadu.cmsxc.appuser.service.SystemUserService;
import cn.com.leadu.cmsxc.appuser.util.constant.enums.AssignmentEnums;
import cn.com.leadu.cmsxc.common.constant.enums.RecoveryTaskStatusEnums;
import cn.com.leadu.cmsxc.common.constant.enums.TaskStatusEnums;
import cn.com.leadu.cmsxc.common.constant.enums.DeleteFlagEnum;
import cn.com.leadu.cmsxc.common.constant.enums.InformationEnum;
import cn.com.leadu.cmsxc.common.constant.enums.MessageReadEnum;
import cn.com.leadu.cmsxc.common.constant.enums.UserRoleEnums;
import cn.com.leadu.cmsxc.common.util.CommonUtil;
import cn.com.leadu.cmsxc.common.util.ProvinceUtil;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.appbusiness.repository.RecoveryTaskRepository;
import cn.com.leadu.cmsxc.data.appbusiness.repository.VehicleTaskRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.HomePageInfoRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.MessageCenterRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.VehicleTaskRecovery;
import cn.com.leadu.cmsxc.pojo.appuser.entity.HomePageInfo;
import cn.com.leadu.cmsxc.pojo.appuser.entity.MessageCenter;
import cn.com.leadu.cmsxc.pojo.appuser.vo.*;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 首页新闻Service实现类
 */
@Service
public class HomePageInfoServiceImpl implements HomePageInfoService {
    private static final Logger logger = LoggerFactory.getLogger(HomePageInfoServiceImpl.class);
    @Autowired
    private HomePageInfoRepository homePageInfoRepository;
    @Autowired
    private VehicleTaskRepository vehicleTaskRepository;
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private MessageCenterRepository messageCenterRepository;
    @Autowired
    private RecoveryTaskRepository recoveryTaskRepository;

    /**
     * 首页获取新闻列表
     *
     * @return
     */
    public ResponseEntity<RestResponse> getInformation(String type){
        Example example = new Example(HomePageInfo.class);
        Example.Criteria criteria = example.createCriteria();
        //兼容老接口，若type没传，默认为bunner图（新闻）
        if(StringUtil.isNull(type)){
            type = InformationEnum.NEWS.getCode();
        }
        criteria.andEqualTo("type", type);
        example.setOrderByClause(" create_time desc ");
        // 获取新闻信息
        List<HomePageInfo> homePageInfoList =  homePageInfoRepository.selectByExampleList(example);
        if(homePageInfoList == null || homePageInfoList.isEmpty()){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","暂无资讯"),
                  HttpStatus.OK);
        }
        if(InformationEnum.NEWS.getCode().equals(type)){
            if(homePageInfoList.size() <= 4){
                return new ResponseEntity<RestResponse>(
                        RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, homePageInfoList,""),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<RestResponse>(
                        RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, homePageInfoList.subList(0,4),""),
                        HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, homePageInfoList.subList(0,1),""),
                    HttpStatus.OK);
        }
    }
    /**
     * 首页获取数据统计
     *
     * @param systemUser 用户信息
     * @return
     */
    public ResponseEntity<RestResponse> getHomePageDataStatistics(SystemUser systemUser){
        DataStatisticsParamVo dataStatisticsParamVo = new DataStatisticsParamVo();
        HomePageStatisticsVo homePageStatisticsVo = new HomePageStatisticsVo();
        // 未完成总量
        Long notFinishCount = 0L;
        // 派单总量
        Long totalCount = 0L;
        List<HomePageStatisticsSubVo> statisticsList = vehicleTaskRepository.selectCountByStatus(systemUser.getRecoveryCompanyId());
        for(int i = 0; i < statisticsList.size(); i++){
            if(RecoveryTaskStatusEnums.CANCEL.getType().equals(statisticsList.get(i).getStatus())){
                // 取消任务量
                homePageStatisticsVo.setCancelCount(statisticsList.get(i).getSumCount());
                totalCount = totalCount + statisticsList.get(i).getSumCount();
            }else if(RecoveryTaskStatusEnums.OTHERFINISH.getType().equals(statisticsList.get(i).getStatus())){
                // 他人完成任务量
                homePageStatisticsVo.setOthersFinishCount(statisticsList.get(i).getSumCount());
                totalCount = totalCount + statisticsList.get(i).getSumCount();
            }else if(RecoveryTaskStatusEnums.SELFFINISH.getType().equals(statisticsList.get(i).getStatus())){
                // 本公司完成任务量
                homePageStatisticsVo.setSelfFinishCount(statisticsList.get(i).getSumCount());
                totalCount = totalCount + statisticsList.get(i).getSumCount();
            }else{
                // 未完成任务量
                notFinishCount = notFinishCount + statisticsList.get(i).getSumCount();
                // 总派单任务量
                totalCount = totalCount + statisticsList.get(i).getSumCount();
            }
        }
        // 总派单任务量
        homePageStatisticsVo.setTotalCount(totalCount);
        // 未完成任务量
        homePageStatisticsVo.setNotFinishCount(notFinishCount);
        // 获取收车公司所有业务员和内勤人员(内勤人员也可以申请授权并获取授权)
        List<String> users = new ArrayList<>();
        // 获取公司所有人员
        List<SystemUser> sysUserList = systemUserService.selectByRecoveryCompanyId(systemUser.getRecoveryCompanyId());
        for(SystemUser sysUser : sysUserList){
            users.add(sysUser.getUserId());
        }
        dataStatisticsParamVo.setUsers(users);
        DecimalFormat df   = new DecimalFormat("######0.00");
        // 派单任务总量及总服务费
        DataStatisticsSubVo selfFinish = vehicleTaskRepository.selectSelfFinish(dataStatisticsParamVo, systemUser.getRecoveryCompanyId());
        if(selfFinish != null){
            // 悬赏任务完成总量
            homePageStatisticsVo.setSumFinishCount(selfFinish.getSumCount());
            // 总服务费
            homePageStatisticsVo.setSumServiceFee(Double.valueOf(df.format(selfFinish.getSumFee())));
        }
        // 取在授权表中不在派单表中的自己公司业务员完成的任务总量及总服务费
        DataStatisticsSubVo othersFinish = vehicleTaskRepository.selectOthersFinish(dataStatisticsParamVo, systemUser.getRecoveryCompanyId());
        if(othersFinish != null){
            // 悬赏列表完成总量
            homePageStatisticsVo.setRevardFinishCount(othersFinish.getSumCount());
            // 总服务费
            if(othersFinish.getSumFee() != null && homePageStatisticsVo.getSumServiceFee() != null){
                homePageStatisticsVo.setSumServiceFee(Double.valueOf(df.format(othersFinish.getSumFee() + homePageStatisticsVo.getSumServiceFee())));
            }else if(othersFinish.getSumFee() != null){
                homePageStatisticsVo.setSumServiceFee(Double.valueOf(df.format(othersFinish.getSumFee())));
            }
            // 完成总量
            if(othersFinish.getSumCount() != null && homePageStatisticsVo.getSumFinishCount() != null){
                homePageStatisticsVo.setSumFinishCount(othersFinish.getSumCount() + homePageStatisticsVo.getSumFinishCount());
            }else if(othersFinish.getSumCount() != null){
                homePageStatisticsVo.setSumFinishCount(othersFinish.getSumCount());
            }
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, homePageStatisticsVo, ""),
                HttpStatus.OK);
    }

    /**
     * 首页获取派单数量，消息中心数量
     *
     * @return
     */
    public ResponseEntity<RestResponse> getHomeCountInfo(SystemUser systemUser){
        //如果不是收车人员、内勤人员，提示角色有误
        if(!UserRoleEnums.RECOVERY_MANAGER.getType().equals(systemUser.getUserRole())
                && !UserRoleEnums.RECOVERY_MEMBER.getType().equals(systemUser.getUserRole())
                && !UserRoleEnums.RECOVERY_BOSS.getType().equals(systemUser.getUserRole())){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","当前用户角色有误"),
                    HttpStatus.OK);
        }
        HomeCountInfoVo vo = new HomeCountInfoVo(); //构建数据模型
        int taskCount = 0; //初始值为0
        int messageCuont = 0; //初始值为0
        //获取未读消息数量
        Example example = new Example(MessageCenter.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("receiver", systemUser.getUserId());
        criteria.andEqualTo("isReaded", MessageReadEnum.NO_READ.getCode());
        criteria.andEqualTo("deleteFlag", DeleteFlagEnum.ON.getCode());
        example.setOrderByClause(" push_date desc ");
        List<MessageCenter> messageCenterList = messageCenterRepository.selectByExampleList(example);
        if(messageCenterList != null){
            messageCuont = messageCenterList.size(); //设定未读消息数量
        }
        //如果是内勤人员，获取未派单数量
        if(UserRoleEnums.RECOVERY_MANAGER.getType().equals(systemUser.getUserRole())
                || UserRoleEnums.RECOVERY_BOSS.getType().equals(systemUser.getUserRole())){
            //查询未处理数量
            Example example1 = new Example(VehicleTaskRecovery.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("recoveryCompanyId", systemUser.getRecoveryCompanyId());
            criteria1.andEqualTo("assignmentFlag", AssignmentEnums.NO_ASSIGNED.getType()); //未处理
            criteria1.andNotEqualTo("status", RecoveryTaskStatusEnums.SELFFINISH.getType()); //不是自己完成
            criteria1.andNotEqualTo("status", RecoveryTaskStatusEnums.OTHERFINISH.getType()); //不是他人完成
            criteria1.andNotEqualTo("status", RecoveryTaskStatusEnums.CANCEL.getType()); //不是取消
            List<VehicleTaskRecovery> recoveryList = recoveryTaskRepository.selectByExampleList(example1); //未处理订单集合
            if(recoveryList != null){
                taskCount = recoveryList.size(); //未派单数量
            }
        }
        vo.setMessageCount(messageCuont); //设定未处理订单集合
        vo.setTaskCount(taskCount); //设定未派单数量
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, vo, ""),
                HttpStatus.OK);
    }

    /**
     * 首页获取滚播消息
     *
     * @return
     */
    public ResponseEntity<RestResponse> getHomeFinishInfo(){
        List<FinishTaskVo> resultList = vehicleTaskRepository.selectFinishInfo();
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, resultList, ""),
                HttpStatus.OK);
    }

    /**
     * 首页获取今日推荐
     *
     * @return
     */
    public ResponseEntity<RestResponse> getTodayRecommend(String userId, String province){
        VehicleTaskVo vehicleTaskVo = new VehicleTaskVo();
        vehicleTaskVo.setNowDate(new Date());
        vehicleTaskVo.setPage(1);
        vehicleTaskVo.setSize(3);
        if(StringUtil.isNotNull(province)){
            String shortName = ProvinceUtil.getProvinceShortByName(province);
            //获取省份简称
            if(StringUtil.isNotNull(shortName)){
                List<String> provinces = new ArrayList();
                provinces.add(CommonUtil.likePartten(shortName));
                vehicleTaskVo.setList(provinces);
            }
        }
        List<RewardForVehicleVo> vehicleTaskList = vehicleTaskRepository.selectByTaskStatusAndMore(vehicleTaskVo,TaskStatusEnums.NORMAL.getType(),userId,vehicleTaskVo.getPage(),vehicleTaskVo.getSize());
        if(vehicleTaskList == null){
            vehicleTaskList = new ArrayList();
        }
        //按照定位取得所在省的悬赏数据数量
        int count = vehicleTaskList.size();
        //如果本省数据小于3，则从悬赏列表中再取相应数据补充
        if(StringUtil.isNotNull(province) && count < 3){
            vehicleTaskVo.setList(null); //设定省份参数为空
            vehicleTaskVo.setSize(3-count); //设定取出的数据量为3-已取出数量
            List<RewardForVehicleVo> otherTaskList = vehicleTaskRepository.selectByTaskStatusAndMore(vehicleTaskVo,TaskStatusEnums.NORMAL.getType(),userId,vehicleTaskVo.getPage(),vehicleTaskVo.getSize());
            if(otherTaskList != null){
                vehicleTaskList.addAll(otherTaskList); //合并两者结果集
            }
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, vehicleTaskList, ""),
                HttpStatus.OK);
    }

}
