package cn.com.leadu.cmsxc.data.appuser.dao;

import cn.com.leadu.cmsxc.data.base.config.BaseDao;
import cn.com.leadu.cmsxc.pojo.appuser.entity.AuditorArea;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 审核人员片区划分Dao
 */
public interface AuditorAreaDao extends BaseDao<AuditorArea> {

    /**
     * 根据用户表主键删除数据
     *
     * @param id 分组id
     */
    void deleteByUserKey(@Param("id") String id);

    /**
     * 获取某个审核人员对应的所有省份
     *
     * @param id 用户表主键
     * @return
     */
    List<String> selectProvinces(@Param("id") String id);

}
