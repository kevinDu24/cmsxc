package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.data.appuser.dao.AuthorizationHistoryDao;
import cn.com.leadu.cmsxc.data.appuser.repository.AuthorizationHistoryRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.AuthorizationHistory;
import cn.com.leadu.cmsxc.pojo.appuser.entity.ClueInfo;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenixa on 2018/1/19.
 *
 * 授权操作日志
 */
@Component
public class AuthorizationHistoryRepositoryImpl extends AbstractBaseRepository<AuthorizationHistoryDao,AuthorizationHistory> implements AuthorizationHistoryRepository {
    /**
     * 登录单车授权操作日志
     * @param authorizationHistory
     */
    @Override
    public void insertOne(AuthorizationHistory authorizationHistory){
        super.insert(authorizationHistory);
    }

    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    @Override
    public List<AuthorizationHistory> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }
}
