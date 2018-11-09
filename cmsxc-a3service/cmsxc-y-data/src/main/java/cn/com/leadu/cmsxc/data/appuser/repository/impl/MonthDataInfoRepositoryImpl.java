package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.data.appuser.dao.MonthDataInfoDao;
import cn.com.leadu.cmsxc.data.appuser.repository.MonthDataInfoRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.MonthDataInfo;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenixa on 2018/1/19.
 *
 * 月度数据统计Repository
 */
@Component
public class MonthDataInfoRepositoryImpl extends AbstractBaseRepository<MonthDataInfoDao,MonthDataInfo> implements MonthDataInfoRepository {

    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    public List<MonthDataInfo> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }
    /**
     * 登录分组表信息
     *
     * @param monthDataInfo
     */
    public void insertOne(MonthDataInfo monthDataInfo){
        super.insert(monthDataInfo);
    }
    /**
     * 根据主键更新表
     * @param monthDataInfo
     */
    public void updateByPrimaryKey(MonthDataInfo monthDataInfo){
        super.updateByPrimaryKey(monthDataInfo);
    }
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    public MonthDataInfo selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }
    /**
     * 批量插入数据
     * @param monthDataInfos
     */
    public void insertMore(List<MonthDataInfo> monthDataInfos){
        super.insertListByMapper(monthDataInfos);
    }

    public MonthDataInfo selectByPrimaryKey(String id){
        return super.selectByPrimaryKey(id);
    }

    /**
     * 每月1号查询上个月需要存放到月度数据表中的数据
     *
     * @return
     */
    public void insertMonthData(){
        baseDao.insertMonthData();
    }
}
