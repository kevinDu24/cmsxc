package cn.com.leadu.cmsxc.system.service.impl;

import cn.com.leadu.cmsxc.data.appuser.repository.HomePageInfoRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appuser.entity.HomePageInfo;
import cn.com.leadu.cmsxc.pojo.system.vo.information.NewPublishVo;
import cn.com.leadu.cmsxc.system.service.HomePageInfoService;
import cn.com.leadu.cmsxc.system.util.constant.HtmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by LEO on 16/9/29.
 */
@Service
public class HomePageInfoServiceImpl implements HomePageInfoService {

    @Autowired
    private HomePageInfoRepository homePageInfoRepository;

    /**
     * 发布新闻
     *
     * @param newPublishVo
     * @return
     */
    public ResponseEntity<RestResponse> newsPublish(NewPublishVo newPublishVo) {
        HomePageInfo homePageInfo = new HomePageInfo();
        homePageInfo.setTitle(newPublishVo.getTitle());
        homePageInfo.setImgUrl(newPublishVo.getCoverUrl());
        addImgStyle(newPublishVo);
        homePageInfo.setContent(newPublishVo.getContent());
        homePageInfo.setReadCount(0);
        homePageInfo.setType(newPublishVo.getType());
        homePageInfoRepository.insertOne(homePageInfo);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","发布成功"),
                HttpStatus.OK);
    }

    /**
     * 添加图片样式
     *
     * @param newPublishVo
     * @return
     */
    public NewPublishVo addImgStyle(NewPublishVo newPublishVo) {
        String imgStyle = "style=\"max-height: 100%;max-width: 100%;\"";
        String temp[] = newPublishVo.getContent().split("<img");
        if (temp.length > 1) {
            String content = "";
            for (int i = 0; i < temp.length; i++) {
                if (i < temp.length - 1) {
                    content = content + temp[i] + "<img " + imgStyle;
                } else {
                    content = content + temp[i];
                }
            }
            newPublishVo.setContent(content);
        }
        newPublishVo.setContent(HtmlUtils.produceHtml(newPublishVo.getContent()));
        return newPublishVo;
    }
}
