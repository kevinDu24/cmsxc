package cn.com.leadu.cmsxc.appuser.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appuser.vo.DataStatisticsParamVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.springframework.http.ResponseEntity;

/**
 * Created by yuanzhenxia on 2018/4/23.
 *
 * 数据统计service
 */
public interface DataStatisticsService {
    /**
     * 根据收车公司id获取合作委托公司信息
     *
     * @param systemUser 用户信息
     * @return
     */
    ResponseEntity<RestResponse> getLeaseCompanys(SystemUser systemUser);
    /**
     * 统计信息
     *
     * @param systemUser 用户信息
     * @return
     */
    ResponseEntity<RestResponse> getDataStatistics(SystemUser systemUser, DataStatisticsParamVo dataStatisticsParamVo);

}
