package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import cn.com.leadu.cmsxc.data.appuser.dao.SystemUserScoreDao;
import cn.com.leadu.cmsxc.data.appuser.repository.SystemUserScoreRepository;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.SystemUserScore;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/15.
 *
 * 用户积分流水
 */
@Component
public class SystemUserScoreRepositoryImpl extends AbstractBaseRepository<SystemUserScoreDao,SystemUserScore> implements SystemUserScoreRepository {
    /**
     * 登录用户积分流水
     * @param systemUserScore
     */
    public void insertOne(SystemUserScore systemUserScore){
        super.insert(systemUserScore);
    }

    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    public List<SystemUserScore> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }

    /**
     * 分页获取积分信息
     *
     * @param example,pageQuery
     * @param pageQuery
     * @return
     */
    public PageInfoExtend<SystemUserScore> selectListByExamplePageInfo(Example example, PageQuery pageQuery){
        return super.selectListByExamplePageInfo(example,pageQuery);
    }
}
