package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.data.appuser.dao.RecoveryLeaseRelationDao;
import cn.com.leadu.cmsxc.data.appuser.repository.RecoveryLeaseRelationRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.GpsAppInfo;
import cn.com.leadu.cmsxc.pojo.appuser.entity.RecoveryLeaseRelation;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 委托公司收车app信息
 */
@Component
public class RecoveryLeaseRelationRepositoryImpl extends AbstractBaseRepository<RecoveryLeaseRelationDao,RecoveryLeaseRelation> implements RecoveryLeaseRelationRepository {
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    @Override
    public List<RecoveryLeaseRelation> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    @Override
    public RecoveryLeaseRelation selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }

    /**
     * 登录表
     * @param recoveryLeaseRelation
     */
    public void insertOne(RecoveryLeaseRelation recoveryLeaseRelation){
        super.insert(recoveryLeaseRelation);
    }

    /**
     * 根据主键更新表
     * @param recoveryLeaseRelation
     */
    @Override
    public void updateByPrimaryKey(RecoveryLeaseRelation recoveryLeaseRelation) {
        super.updateByPrimaryKey(recoveryLeaseRelation);
    }

}
