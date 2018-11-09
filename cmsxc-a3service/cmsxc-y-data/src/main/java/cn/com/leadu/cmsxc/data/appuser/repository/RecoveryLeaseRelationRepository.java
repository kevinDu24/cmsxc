package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appuser.entity.RecoveryLeaseRelation;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 收车公司与委托公司关系Repository
 */
public interface RecoveryLeaseRelationRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<RecoveryLeaseRelation> selectByExampleList(Example example);

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    RecoveryLeaseRelation selectOneByExample(Example example);

    /**
     * 登录收车公司与委托公司关系表
     * @param recoveryLeaseRelation
     */
    public void insertOne(RecoveryLeaseRelation recoveryLeaseRelation);

    /**
     * 根据主键更新表
     * @param recoveryLeaseRelation
     */
    void updateByPrimaryKey(RecoveryLeaseRelation recoveryLeaseRelation);
}
