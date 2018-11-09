package cn.com.leadu.cmsxc.assistant.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

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
     * @param type 文件类型：申请授权-apply；上传头像-photo；新增记录-record
     * @return
     */
    ResponseEntity<RestResponse> uploadFile(String type, MultipartFile file);

    /**
     * 文件下载（查看）
     *
     * @param response 页面请求
     * @param fileName 文件名
     * @param loadDate 上传时间
     * @param type 文件类型：申请授权-apply；上传头像-photo；新增记录-record
     * @return
     */
    void downloadFile(HttpServletResponse response, String type, String fileName, String loadDate);
}
