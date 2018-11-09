package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appuser.entity.UserDeviceInfo;
import tk.mybatis.mapper.entity.Example;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 用户设备信息
 */
public interface UserDeviceInfoRepository {
    /**
     * 获取单个数据
     *
     * @return
     */
    public UserDeviceInfo selectOneByExample(Example example);

    /**
     * 根据主键更新表
     * @param userDeviceInfo
     */
    public void updateByPrimaryKey(UserDeviceInfo userDeviceInfo);

    /**
     * 根据主键删除
     *
     */
    public void delete(String id);
}
