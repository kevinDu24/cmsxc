package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.pojo.appuser.entity.SystemUserScore;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/15.
 *
 * 用户积分流水
 */
public interface SystemUserScoreRepository {
    /**
     * 登录用户积分流水信息
     * @param systemUserScore
     */
    void insertOne(SystemUserScore systemUserScore);
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    List<SystemUserScore> selectByExampleList(Example example);

    PageInfoExtend selectListByExamplePageInfo(Example example, PageQuery pageQuery);
}
