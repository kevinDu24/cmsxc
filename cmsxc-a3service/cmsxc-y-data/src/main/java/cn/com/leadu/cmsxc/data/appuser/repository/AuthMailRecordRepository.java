package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appuser.entity.AuthMailRecord;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 审批授权邮件发送记录
 */
public interface AuthMailRecordRepository {
    /**
     * 登录审批授权邮件发送记录表信息
     *
     * @param authMailRecord
     */
    AuthMailRecord insertOne(AuthMailRecord authMailRecord);

    /**
     * 根据主键更新表
     * @param authMailRecord
     */
    void updateByPrimaryKey(AuthMailRecord authMailRecord);

    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<AuthMailRecord> selectByExampleList(Example example);
}
