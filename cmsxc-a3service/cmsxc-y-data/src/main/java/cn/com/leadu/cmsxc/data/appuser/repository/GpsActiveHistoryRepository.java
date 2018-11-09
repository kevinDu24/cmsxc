package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appuser.entity.GpsActiveHistory;
import cn.com.leadu.cmsxc.pojo.appuser.vo.GpsActiveCheckVo;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * GPS激活日志表Repository
 */
public interface GpsActiveHistoryRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<GpsActiveHistory> selectByExampleList(Example example);

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    GpsActiveHistory selectOneByExample(Example example);

    /**
     * 登录收车金融公司app信息表
     * @param gpsActiveHistory
     */
    public void insertOne(GpsActiveHistory gpsActiveHistory);

    /**
     * 根据主键更新表
     * @param gpsActiveHistory
     */
    void updateByPrimaryKey(GpsActiveHistory gpsActiveHistory);

    /**
     * 根据车架号后六位查询对应的车架号集合
     * @param vehicleIdentifyNum
     * @return
     */
    List<GpsActiveCheckVo> selectByLastSixNum(String vehicleIdentifyNum);

    /**
     * 分页获取gps激活历史列表
     *
     * @param userId 用户id
     * @param page 当前页数
     * @param size 每页个数
     * @return
     */
    public List<GpsActiveHistory> selectActiveList(String userId, int page, int size);
}
