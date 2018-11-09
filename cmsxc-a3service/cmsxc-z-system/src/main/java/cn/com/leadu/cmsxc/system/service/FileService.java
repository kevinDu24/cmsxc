package cn.com.leadu.cmsxc.system.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yuanzhenxia on 2018/1/30.
 *
 * 文件操作
 */

public interface FileService {
    /**
     * 上传文件
     *
     * @param file 线索图片
     * @return
     */
    ResponseEntity<RestResponse> uploadFile(String type, MultipartFile file);

    /**
     * 文件下载（查看）
     *
     * @param response 页面请求
     * @param fileName 文件名
     * @param loadDate 上传时间
     * @return
     */
    byte[] downloadFile(HttpServletResponse response, String fileName, String loadDate, String type) throws IOException;
}
