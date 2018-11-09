package cn.com.leadu.cmsxc.system.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.system.vo.information.NewPublishVo;
import org.springframework.http.ResponseEntity;

/**
 * Created by huzongcheng on 2018/2/5.
 */
public interface HomePageInfoService {

    /**
     * 发布新闻
     *
     * @param newPublishVo
     * @return
     */
    public ResponseEntity<RestResponse> newsPublish(NewPublishVo newPublishVo);
}
