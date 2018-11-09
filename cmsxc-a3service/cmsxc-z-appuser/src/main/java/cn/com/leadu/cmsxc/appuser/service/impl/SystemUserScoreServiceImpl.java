package cn.com.leadu.cmsxc.appuser.service.impl;

import cn.com.leadu.cmsxc.appuser.service.SystemUserService;
import cn.com.leadu.cmsxc.appuser.service.SystemUserScoreService;
import cn.com.leadu.cmsxc.common.util.CommonUtil;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.appuser.util.constant.enums.ScoreCodeEnums;
import cn.com.leadu.cmsxc.appuser.validator.sysuser.vo.ScoreDetailVo;
import cn.com.leadu.cmsxc.appuser.validator.sysuser.vo.ScoreListVo;
import cn.com.leadu.cmsxc.common.entity.PageQuery;
import cn.com.leadu.cmsxc.common.util.ArrayUtil;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.system.repository.SystemUserRepository;
import cn.com.leadu.cmsxc.data.appuser.repository.SystemUserScoreRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import cn.com.leadu.cmsxc.pojo.appuser.entity.SystemUserScore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/23.
 *
 * 用户积分接口实现类
 */
@Service
public class SystemUserScoreServiceImpl implements SystemUserScoreService {
    private static final Logger logger = LoggerFactory.getLogger(SystemUserScoreServiceImpl.class);
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private SystemUserRepository systemUserRepository;
    @Autowired
    private SystemUserScoreRepository systemUserScoreRepository;
    /**
     * 赠送积分
     *
     * @param userId 用户id
     * @param scoreValue 积分值
     * @param scoreAcceptUserId 积分受领用户id
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> sendScore(String userId , String scoreValue, String scoreAcceptUserId){
        Date nowDate = new Date();
        if (StringUtil.isNull(userId)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","账户手机号为空"),
                    HttpStatus.OK);
        }
        if(Integer.parseInt(scoreValue) <= 0){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","赠送积分值不可小于等于0"),
                    HttpStatus.OK);
        }
        // 取得受领账户信息
        SystemUser acceptSysUser = systemUserService.selectSystemUserByUserId(scoreAcceptUserId);
        if(acceptSysUser == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","受领账户不存在"),
                    HttpStatus.OK);
        }
        // 取得当前账户信息
        SystemUser sysUser = systemUserService.selectSystemUserByUserId(userId);
        // 自己不可以向自己赠送积分
        if(userId.equals(scoreAcceptUserId)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","不可以向自己赠送积分"),
                    HttpStatus.OK);
        }
        if(sysUser != null && sysUser.getTotalScore() < Integer.parseInt(scoreValue)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","账户积分不足"),
                    HttpStatus.OK);
        }
        // 更新当前用户的总积分
        sysUser.setTotalScore(sysUser.getTotalScore()- Integer.parseInt(scoreValue));
        sysUser.setUpdateTime(nowDate);
        // 更新受领账户的总积分
        acceptSysUser.setTotalScore(acceptSysUser.getTotalScore() + Integer.parseInt(scoreValue));
        acceptSysUser.setUpdateTime(nowDate);
        systemUserRepository.updateByPrimaryKey(sysUser);
        systemUserRepository.updateByPrimaryKey(acceptSysUser);
        // 登录赠送人积分流水
        SystemUserScore systemUserScore = new SystemUserScore();
        // 用户id
        systemUserScore.setUserId(userId);
        // 积分code
        systemUserScore.setScoreCode(ScoreCodeEnums.SEND.getType());
        // 积分值
        systemUserScore.setScoreValue(-Integer.parseInt(scoreValue));
        // 备注
        systemUserScore.setRemark(ScoreCodeEnums.SEND.getValue());
        systemUserScore.setCreator(userId);
        systemUserScore.setUpdater(userId);
        systemUserScore.setUpdateTime(nowDate);
        systemUserScore.setCreateTime(nowDate);
        systemUserScore.setScoreTime(nowDate);
        systemUserScoreRepository.insertOne(systemUserScore);
        // 登录受领人积分流水
        SystemUserScore acceptSystemUserScore = new SystemUserScore();
        // 用户id
        acceptSystemUserScore.setUserId(scoreAcceptUserId);
        // 积分代码
        acceptSystemUserScore.setScoreCode(ScoreCodeEnums.ACCEPT.getType());
        // 积分值
        acceptSystemUserScore.setScoreValue(+Integer.parseInt(scoreValue));
        // 备注
        acceptSystemUserScore.setRemark(ScoreCodeEnums.ACCEPT.getValue());
        acceptSystemUserScore.setScoreTime(nowDate);
        acceptSystemUserScore.setCreator(scoreAcceptUserId);
        acceptSystemUserScore.setUpdater(scoreAcceptUserId);
        acceptSystemUserScore.setUpdateTime(nowDate);
        acceptSystemUserScore.setCreateTime(nowDate);
        systemUserScoreRepository.insertOne(acceptSystemUserScore);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","赠送积分成功"),
                HttpStatus.OK);
    }
    /**
     * 积分列表
     *
     * @param userId 用户id
     * @param searchTime 查询时间
     * @param page 当前页
     * @param size 每页数目
     * @return
     */
    public ResponseEntity<RestResponse> scoreList(String userId , String searchTime, int page, int size){
        // 返回数据用vo
        ScoreListVo scoreListVo = new ScoreListVo();
        // 支出积分数
        Integer expendScore = 0;
        // 收取积分数
        Integer incomeScore = 0;
        // 根据用户id取得当前用户总积分
        SystemUser sysUser = systemUserService.selectSystemUserByUserId(userId);
        if(sysUser == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","账户不存在"),
                    HttpStatus.OK);
        }
        // 当前用户总积分
        scoreListVo.setTotalScore(sysUser.getTotalScore());
        // 分页获取用户指定月份的积分流水信息
        List<SystemUserScore> systemAllUserScoreList = selectAllByUserIdAndSearchTime(userId, searchTime);
        if(systemAllUserScoreList != null){
            for(SystemUserScore systemUserScore : systemAllUserScoreList){
                // 如果积分值小于0，统计支出积分
                if(systemUserScore.getScoreValue() < 0){
                    expendScore = expendScore + systemUserScore.getScoreValue();
                    // 如果积分值大于0 ，统计收取积分
                }else{
                    incomeScore = incomeScore + systemUserScore.getScoreValue();
                }
            }
        }
        // 分页获取用户指定月份的积分流水信息
        List<SystemUserScore> systemUserScoreList = selectByUserIdAndSearchTime(userId, searchTime,page,size );
        for(SystemUserScore systemUserScore : systemUserScoreList){
            ScoreDetailVo scoreDetailVo = new ScoreDetailVo();
            // 积分时间
            scoreDetailVo.setScoreTime(systemUserScore.getScoreTime());
            // 积分值
            scoreDetailVo.setScoreValue(systemUserScore.getScoreValue());
            // 积分操作类型
            scoreDetailVo.setScoreOperateType(systemUserScore.getScoreCode());
            // 积分详情列表
            scoreListVo.getScoreDetails().add(scoreDetailVo);
        }
        // 总支出积分
        scoreListVo.setExpendScore(expendScore);
        // 总收入积分
        scoreListVo.setIncomeScore(incomeScore);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS, scoreListVo,""),
                HttpStatus.OK);
    }

    /**
     * 根据手机号和时间获取用户积分流水信息，分页
     *
     * @param userId 用户id
     * @param searchTime 查询时间
     * @param page 当前页
     * @param size 每页个数
     * @return
     */
    public List<SystemUserScore> selectByUserIdAndSearchTime(String userId, String searchTime, int page, int size){
        if(StringUtil.isNotNull(userId) && StringUtil.isNotNull(searchTime)) {
            Example example = new Example(SystemUserScore.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId", userId);
            criteria.andLike("scoreTime", CommonUtil.likePartten(searchTime));
            example.setOrderByClause("score_time desc");
            List<SystemUserScore> allList = systemUserScoreRepository.selectByExampleList(example);
            int count = allList.size();
            int totalPages = (int)Math.ceil((double)count / size);
            if(page > totalPages){
                return new ArrayList<>();
            }
            PageQuery pageQuery = new PageQuery(page, size);
            PageInfoExtend<SystemUserScore> systemUserScoreList = systemUserScoreRepository.selectListByExamplePageInfo(example,pageQuery);
            if (ArrayUtil.isNotNullAndLengthNotZero(systemUserScoreList.getData()))
                return systemUserScoreList.getData();
        }
        return null;
    }
    /**
     * 根据手机号和时间获取用户积分流水信息
     *
     * @param userId 用户id
     * @param searchTime 查询时间
     * @return
     */
    public List<SystemUserScore> selectAllByUserIdAndSearchTime(String userId, String searchTime){
        Example example = new Example(SystemUserScore.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtil.isNotNull(userId)){
            criteria.andEqualTo("userId", userId);
        }
        if(StringUtil.isNotNull(searchTime)) {
            criteria.andLike("scoreTime", CommonUtil.likePartten(searchTime));
        }
        example.setOrderByClause("score_time desc");
        List<SystemUserScore> allList = systemUserScoreRepository.selectByExampleList(example);
        if (ArrayUtil.isNotNullAndLengthNotZero(allList))
            return allList;
        return null;
    }

}
