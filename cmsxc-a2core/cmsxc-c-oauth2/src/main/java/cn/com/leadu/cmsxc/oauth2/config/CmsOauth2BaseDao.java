package cn.com.leadu.cmsxc.oauth2.config;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author qiaomengnan
 * @ClassName: CmsOatuah2Dao
 * @Description:
 * @date 2018/1/7
 */
public interface CmsOauth2BaseDao<T> extends Mapper<T>, MySqlMapper<T> {

}
