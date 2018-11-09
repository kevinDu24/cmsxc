package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appuser.entity.ParkingLeaseRelation;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 停车场公司与委托公司关系Repository
 */
public interface ParkingLeaseRelationRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<ParkingLeaseRelation> selectByExampleList(Example example);

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    ParkingLeaseRelation selectOneByExample(Example example);

    /**
     * 登录表
     * @param parkingLeaseRelation
     */
    public void insertOne(ParkingLeaseRelation parkingLeaseRelation);

    /**
     * 根据主键更新表
     * @param parkingLeaseRelation
     */
    void updateByPrimaryKey(ParkingLeaseRelation parkingLeaseRelation);

    /**
     * 批量插入数据
     * @param parkingLeaseRelations
     */
    void insertMore(List<ParkingLeaseRelation> parkingLeaseRelations);

}
