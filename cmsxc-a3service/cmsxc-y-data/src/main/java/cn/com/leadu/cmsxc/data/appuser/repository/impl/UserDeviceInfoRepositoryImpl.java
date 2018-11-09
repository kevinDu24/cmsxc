package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.data.appuser.dao.UserDeviceInfoDao;
import cn.com.leadu.cmsxc.data.appuser.repository.UserDeviceInfoRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.UserDeviceInfo;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

/**
 * Created by yuanzhenixa on 2018/1/19.
 *
 * 用户设备信息
 */
@Component
public class UserDeviceInfoRepositoryImpl extends AbstractBaseRepository<UserDeviceInfoDao,UserDeviceInfo> implements UserDeviceInfoRepository {

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    @Override
    public UserDeviceInfo selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }

    /**
     * 根据主键更新表
     * @param userDeviceInfo
     */
    public void updateByPrimaryKey(UserDeviceInfo userDeviceInfo){
        super.updateByPrimaryKey(userDeviceInfo);
    }

    @Override
    public void delete(String id){
        super.delete(id);
    }
}
