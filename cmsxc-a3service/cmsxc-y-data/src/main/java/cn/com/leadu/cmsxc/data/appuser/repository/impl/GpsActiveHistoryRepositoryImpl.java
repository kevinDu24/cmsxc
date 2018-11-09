package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.data.appuser.dao.GpsActiveHistoryDao;
import cn.com.leadu.cmsxc.data.appuser.repository.GpsActiveHistoryRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.GpsActiveHistory;
import cn.com.leadu.cmsxc.pojo.appuser.vo.GpsActiveCheckVo;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * GPS激活日志表RepositoryImpl
 */
@Component
public class GpsActiveHistoryRepositoryImpl extends AbstractBaseRepository<GpsActiveHistoryDao,GpsActiveHistory> implements GpsActiveHistoryRepository {
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    @Override
    public List<GpsActiveHistory> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    @Override
    public GpsActiveHistory selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }

    /**
     * 登录表
     * @param gpsActiveHistory
     */
    public void insertOne(GpsActiveHistory gpsActiveHistory){
        super.insert(gpsActiveHistory);
    }

    /**
     * 根据主键更新表
     * @param gpsActiveHistory
     */
    @Override
    public void updateByPrimaryKey(GpsActiveHistory gpsActiveHistory) {
        super.updateByPrimaryKey(gpsActiveHistory);
    }

    /**
     * 根据车架号后六位查询对应的车架号集合
     * @param vehicleIdentifyNum 车架后后六位
     * @return
     */
    public List<GpsActiveCheckVo> selectByLastSixNum(String vehicleIdentifyNum){
        return baseDao.selectByLastSixNum(vehicleIdentifyNum);
    }

    /**
     * 分页获取gps激活历史列表
     *
     * @param userId 用户id
     * @param page 当前页数
     * @param size 每页个数
     * @return
     */
    public List<GpsActiveHistory> selectActiveList(String userId, int page, int size){
        return baseDao.selectActiveList(userId,page,size);
    }

}
