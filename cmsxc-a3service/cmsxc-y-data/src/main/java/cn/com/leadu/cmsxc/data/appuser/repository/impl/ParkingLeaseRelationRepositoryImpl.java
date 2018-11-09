package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.data.appuser.dao.ParkingLeaseRelationDao;
import cn.com.leadu.cmsxc.data.appuser.repository.ParkingLeaseRelationRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.ParkingLeaseRelation;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 停车场信息RepositoryImpl
 */
@Component
public class ParkingLeaseRelationRepositoryImpl extends AbstractBaseRepository<ParkingLeaseRelationDao,ParkingLeaseRelation> implements ParkingLeaseRelationRepository {
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    @Override
    public List<ParkingLeaseRelation> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    @Override
    public ParkingLeaseRelation selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }

    /**
     * 登录表
     * @param parkingLeaseRelation
     */
    public void insertOne(ParkingLeaseRelation parkingLeaseRelation){
        super.insert(parkingLeaseRelation);
    }

    /**
     * 根据主键更新表
     * @param parkingLeaseRelation
     */
    @Override
    public void updateByPrimaryKey(ParkingLeaseRelation parkingLeaseRelation) {
        super.updateByPrimaryKey(parkingLeaseRelation);
    }

    /**
     * 批量插入数据
     * @param parkingLeaseRelations
     */
    public void insertMore(List<ParkingLeaseRelation> parkingLeaseRelations){
        super.insertListByMapper(parkingLeaseRelations);
    }

}
