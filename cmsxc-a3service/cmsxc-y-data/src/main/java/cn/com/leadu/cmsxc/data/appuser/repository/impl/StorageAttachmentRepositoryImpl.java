package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.data.appuser.dao.StorageAttachmentDao;
import cn.com.leadu.cmsxc.data.appuser.repository.StorageAttachmentRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.StorageAttachment;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 入库附件表(停车场)RepositoryImpl
 */
@Component
public class StorageAttachmentRepositoryImpl extends AbstractBaseRepository<StorageAttachmentDao,StorageAttachment> implements StorageAttachmentRepository {
    /**
     * 通过主键获取工单
     * @param id
     * @return
     */
    public StorageAttachment selectByPrimaryKey(String id){
        return super.selectByPrimaryKey(id);
    }

    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    public List<StorageAttachment> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    public StorageAttachment selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }

    /**
     * 登录表
     * @param storageAttachment
     */
    public void insertOne(StorageAttachment storageAttachment){
        super.insert(storageAttachment);
    }

    /**
     * 根据主键更新表
     * @param storageAttachment
     */
    public void updateByPrimaryKey(StorageAttachment storageAttachment) {
        super.updateByPrimaryKey(storageAttachment);
    }

    /**
     * 批量插入数据
     * @param storageAttachments
     */
    public void insertMore(List<StorageAttachment> storageAttachments){
        super.insertListByMapper(storageAttachments);
    }

}
