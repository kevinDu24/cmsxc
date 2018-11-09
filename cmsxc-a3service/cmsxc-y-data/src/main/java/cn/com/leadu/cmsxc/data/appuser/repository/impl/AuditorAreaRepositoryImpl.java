package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.data.appuser.dao.AuditorAreaDao;
import cn.com.leadu.cmsxc.data.appuser.repository.AuditorAreaRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.AuditorArea;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 审核人员片区划分RepositoryImpl
 */
@Component
public class AuditorAreaRepositoryImpl extends AbstractBaseRepository<AuditorAreaDao,AuditorArea> implements AuditorAreaRepository {
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    @Override
    public List<AuditorArea> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    @Override
    public AuditorArea selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }

    /**
     * 登录表
     * @param auditorArea
     */
    public void insertOne(AuditorArea auditorArea){
        super.insert(auditorArea);
    }

    /**
     * 根据主键更新表
     * @param auditorArea
     */
    @Override
    public void updateByPrimaryKey(AuditorArea auditorArea) {
        super.updateByPrimaryKey(auditorArea);
    }

    /**
     * 批量插入数据
     * @param auditorAreas
     */
    public void insertMore(List<AuditorArea> auditorAreas){
        super.insertListByMapper(auditorAreas);
    }

    /**
     * 根据用户表主键删除数据
     *
     * @param id 分组id
     */
    public void deleteByUserKey(String id){
        baseDao.deleteByUserKey(id);
    }

    /**
     * 获取某个审核人员对应的所有省份
     * @param id
     * @return
     */
    public List<String> selectProvinces(String id){
        return baseDao.selectProvinces(id);
    }

}
