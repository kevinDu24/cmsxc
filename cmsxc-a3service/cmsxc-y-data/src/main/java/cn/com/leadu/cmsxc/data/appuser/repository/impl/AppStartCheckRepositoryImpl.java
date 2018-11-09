package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.data.appuser.dao.AppStartCheckDao;
import cn.com.leadu.cmsxc.data.appuser.repository.AppStartCheckRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.AppStartCheck;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenixa on 2018/1/19.
 *
 * apps审核用
 */
@Component
public class AppStartCheckRepositoryImpl extends AbstractBaseRepository<AppStartCheckDao,AppStartCheck> implements AppStartCheckRepository {

    /**
     * 获取数据所有数据
     *
     * @return
     */
    public List<AppStartCheck> selectAll(){
        return super.selectAll();
    }
}
