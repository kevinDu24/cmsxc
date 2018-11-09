package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appuser.entity.GpsAppInfo;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 收车金融公司app信息
 */
public interface GpsAppInfoRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<GpsAppInfo> selectByExampleList(Example example);

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    GpsAppInfo selectOneByExample(Example example);

    /**
     * 登录收车金融公司app信息表
     * @param gpsAppInfo
     */
    public void insertOne(GpsAppInfo gpsAppInfo);

    /**
     * 根据主键更新表
     * @param gpsAppInfo
     */
    void updateByPrimaryKey(GpsAppInfo gpsAppInfo);
}
