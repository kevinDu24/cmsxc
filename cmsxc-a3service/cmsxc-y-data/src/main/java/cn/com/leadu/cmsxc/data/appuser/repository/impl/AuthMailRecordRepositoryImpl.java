package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.data.appuser.dao.AuthMailRecordDao;
import cn.com.leadu.cmsxc.data.appuser.repository.AuthMailRecordRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.AuthMailRecord;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenixa on 2018/1/19.
 *
 * 审批授权邮件发送记录
 */
@Component
public class AuthMailRecordRepositoryImpl extends AbstractBaseRepository<AuthMailRecordDao,AuthMailRecord> implements AuthMailRecordRepository {
    /**
     * 登录审批授权邮件发送记录
     * @param authMailRecord
     */
    @Override
    public AuthMailRecord insertOne(AuthMailRecord authMailRecord){
        return super.insert(authMailRecord);
    }

    /**
     * 根据主键更新表
     * @param authMailRecord
     */
    public void updateByPrimaryKey(AuthMailRecord authMailRecord){
        super.updateByPrimaryKey(authMailRecord);
    }

    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    @Override
    public List<AuthMailRecord> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }
}
