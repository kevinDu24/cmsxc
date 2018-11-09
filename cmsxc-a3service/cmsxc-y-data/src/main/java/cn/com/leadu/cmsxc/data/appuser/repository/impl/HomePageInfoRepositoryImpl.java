package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.data.appuser.dao.HomePageInfoDao;
import cn.com.leadu.cmsxc.data.appuser.repository.HomePageInfoRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.HomePageInfo;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 新闻信息
 */
@Component
public class HomePageInfoRepositoryImpl extends AbstractBaseRepository<HomePageInfoDao,HomePageInfo> implements HomePageInfoRepository {
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    @Override
    public List<HomePageInfo> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }

    /**
     * 登录新闻表
     * @param HomePageInfo
     */
    public void insertOne(HomePageInfo HomePageInfo){
        super.insert(HomePageInfo);
    }

    /**
     * 根据主键更新表
     * @param homePageInfo
     */
    @Override
    public void updateByPrimaryKey(HomePageInfo homePageInfo) {
        super.updateByPrimaryKey(homePageInfo);
    }

}
