package cn.com.leadu.cmsxc.appuser.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.springframework.http.ResponseEntity;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 首页新闻Service
 */
public interface HomePageInfoService {

    /**
     * 根据用户id和是否命中标识分页查看线索信息
     *
     * @return
     */
    ResponseEntity<RestResponse> getInformation(String type);
    /**
     * 首页获取数据统计
     *
     * @return
     */
    ResponseEntity<RestResponse> getHomePageDataStatistics(SystemUser systemUser);

    /**
     * 首页获取派单数量，消息中心数量
     *
     * @return
     */
    public ResponseEntity<RestResponse> getHomeCountInfo(SystemUser systemUser);

    /**
     * 首页获取滚播消息
     *
     * @return
     */
    public ResponseEntity<RestResponse> getHomeFinishInfo();

    /**
     * 首页获取今日推荐
     *
     * @return
     */
    public ResponseEntity<RestResponse> getTodayRecommend(String userId, String province);

}
