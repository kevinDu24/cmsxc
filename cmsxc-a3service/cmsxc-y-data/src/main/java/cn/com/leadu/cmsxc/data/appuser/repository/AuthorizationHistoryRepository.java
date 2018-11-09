package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appuser.entity.AuthorizationHistory;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 授权操作日志
 */
public interface AuthorizationHistoryRepository {
    /**
     * 登录单车授权表信息
     *
     * @param authorizationHistory
     */
    void insertOne(AuthorizationHistory authorizationHistory);

    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    List<AuthorizationHistory> selectByExampleList(Example example);
}
