package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appuser.entity.MonthDataInfo;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 月度数据统计Repository
 */
public interface MonthDataInfoRepository {

    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<MonthDataInfo> selectByExampleList(Example example);
    /**
     * 登录分组表信息
     *
     * @param monthDataInfo
     */
    void insertOne(MonthDataInfo monthDataInfo);
    /**
     * 根据主键更新表
     * @param monthDataInfo
     */
    void updateByPrimaryKey(MonthDataInfo monthDataInfo);
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    MonthDataInfo selectOneByExample(Example example);

    /**
     * 批量插入数据
     * @param monthDataInfos
     */
    void insertMore(List<MonthDataInfo> monthDataInfos);


    /**
     * 每月1号查询上个月需要存放到月度数据表中的数据
     *
     * @return
     */
    public void insertMonthData();
}
