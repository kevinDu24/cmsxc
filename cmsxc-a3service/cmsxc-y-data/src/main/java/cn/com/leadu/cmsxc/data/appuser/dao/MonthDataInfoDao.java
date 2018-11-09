package cn.com.leadu.cmsxc.data.appuser.dao;

import cn.com.leadu.cmsxc.data.base.config.BaseDao;
import cn.com.leadu.cmsxc.pojo.appuser.entity.MonthDataInfo;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 月度数据统计Dao
 */
public interface MonthDataInfoDao extends BaseDao<MonthDataInfo> {

    /**
     * 每月1号查询上个月需要存放到月度数据表中的数据
     *
     * @return
     */
    void insertMonthData();
}
