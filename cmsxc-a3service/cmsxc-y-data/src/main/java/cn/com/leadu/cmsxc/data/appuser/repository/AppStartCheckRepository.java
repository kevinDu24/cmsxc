package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appuser.entity.AppStartCheck;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * apps审核用
 */
public interface AppStartCheckRepository {
    /**
     * 获取数据所有数据
     *
     * @return
     */
    public List<AppStartCheck> selectAll();
}
