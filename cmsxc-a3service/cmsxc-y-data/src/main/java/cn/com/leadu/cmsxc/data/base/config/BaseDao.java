package cn.com.leadu.cmsxc.data.base.config;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Created by qiaohao on 2017/8/23.
 */
public interface BaseDao<T> extends Mapper<T> , MySqlMapper<T> {

}
