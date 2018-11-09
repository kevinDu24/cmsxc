package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appuser.entity.AuditorArea;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 审核人员片区划分Repository
 */
public interface AuditorAreaRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<AuditorArea> selectByExampleList(Example example);

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    AuditorArea selectOneByExample(Example example);

    /**
     * 登录表
     * @param auditorArea
     */
    public void insertOne(AuditorArea auditorArea);

    /**
     * 根据主键更新表
     * @param auditorArea
     */
    void updateByPrimaryKey(AuditorArea auditorArea);

    /**
     * 批量插入数据
     * @param auditorAreas
     */
    void insertMore(List<AuditorArea> auditorAreas);

    /**
     * 根据用户表主键删除数据
     *
     * @param id 分组id
     */
    void deleteByUserKey(String id);

    /**
     * 获取某个审核人员对应的所有省份
     * @param id
     * @return
     */
    List<String> selectProvinces(String id);

}
