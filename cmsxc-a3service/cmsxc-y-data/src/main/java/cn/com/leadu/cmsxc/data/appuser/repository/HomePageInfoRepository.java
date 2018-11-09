package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appuser.entity.HomePageInfo;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 我的线索信息
 */
public interface HomePageInfoRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<HomePageInfo> selectByExampleList(Example example);

    /**
     * 登录新闻表
     * @param HomePageInfo
     */
    public void insertOne(HomePageInfo HomePageInfo);

    /**
     * 根据主键更新表
     * @param homePageInfo
     */
    void updateByPrimaryKey(HomePageInfo homePageInfo);
}
