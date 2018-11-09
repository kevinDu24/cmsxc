package cn.com.leadu.cmsxc.data.appuser.dao;

import cn.com.leadu.cmsxc.data.base.config.BaseDao;
import cn.com.leadu.cmsxc.pojo.appuser.entity.GpsActiveHistory;
import cn.com.leadu.cmsxc.pojo.appuser.vo.GpsActiveCheckVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * GPS激活日志表Dao
 */
public interface GpsActiveHistoryDao extends BaseDao<GpsActiveHistory> {

    /**
     * 根据车架号后六位查询对应的车架号集合
     * @param vehicleIdentifyNum 车架后后六位
     * @return
     */
    List<GpsActiveCheckVo> selectByLastSixNum(@Param("vehicleIdentifyNum") String vehicleIdentifyNum);

    /**
     * 分页查询GPS激活历史列表
     *
     * @param userId 用户id
     * @param page 当前页
     * @param size 每页数目
     * @return
     */
    List<GpsActiveHistory> selectActiveList(@Param("userId") String userId,@Param("page") int page, @Param("size") int size);

}
