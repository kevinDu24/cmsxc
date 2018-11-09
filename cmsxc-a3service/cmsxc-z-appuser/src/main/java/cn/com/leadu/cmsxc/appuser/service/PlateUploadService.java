package cn.com.leadu.cmsxc.appuser.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 上传扫描车牌照Service
 */
public interface PlateUploadService {
    /**
     * 上传扫描车牌照
     *
     * @param systemUser 用户信息
     * @param photoMsg 线索图片
     * @param paramterMap 参数集合（车牌号，线索地址，线索经纬度）
     * @return
     */
    ResponseEntity<RestResponse> uploadPlateInfor(SystemUser systemUser, Map<String,String> paramterMap, MultipartFile photoMsg);

    /**
     * 文件下载（查看）
     *
     * @param response 页面请求
     * @param fileName 文件名
     * @param loadDate 上传时间
     * @return
     */
    void downloadFile(HttpServletResponse response, String fileName, String loadDate);
}
