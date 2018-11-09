package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.data.appuser.dao.GpsAppInfoDao;
import cn.com.leadu.cmsxc.data.appuser.repository.GpsAppInfoRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.GpsAppInfo;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 委托公司收车app信息
 */
@Component
public class GpsAppInfoRepositoryImpl extends AbstractBaseRepository<GpsAppInfoDao,GpsAppInfo> implements GpsAppInfoRepository {
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    @Override
    public List<GpsAppInfo> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    @Override
    public GpsAppInfo selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }

    /**
     * 登录表
     * @param GpsAppInfo
     */
    public void insertOne(GpsAppInfo GpsAppInfo){
        super.insert(GpsAppInfo);
    }

    /**
     * 根据主键更新表
     * @param GpsAppInfo
     */
    @Override
    public void updateByPrimaryKey(GpsAppInfo GpsAppInfo) {
        super.updateByPrimaryKey(GpsAppInfo);
    }

}
