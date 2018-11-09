package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appuser.entity.StorageAttachment;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 入库附件表(停车场)Repository
 */
public interface StorageAttachmentRepository {
    /**
     * 通过主键获取
     * @param id
     * @return
     */
    StorageAttachment selectByPrimaryKey(String id);
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<StorageAttachment> selectByExampleList(Example example);

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    StorageAttachment selectOneByExample(Example example);

    /**
     * 登录表
     * @param storageAttachment
     */
    public void insertOne(StorageAttachment storageAttachment);

    /**
     * 根据主键更新表
     * @param storageAttachment
     */
    void updateByPrimaryKey(StorageAttachment storageAttachment);

    /**
     * 批量插入数据
     * @param storageAttachments
     */
    void insertMore(List<StorageAttachment> storageAttachments);

}
