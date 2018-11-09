package cn.com.leadu.cmsxc.assistant.controller;

import cn.com.leadu.cmsxc.assistant.service.FileService;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by yuanzhenxia on 2018/1/30.
 *
 * 文件操作
 */
@RestController
@RequestMapping("file")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    @Autowired
    private FileService fileService;
    /**
     * 文件上传
     *
     * @param file 文件
     * @param type 文件类型：申请授权-apply；上传头像-photo；新增记录-record
     * @return
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> uploadFile(String type, MultipartFile file) {
        try{
            return fileService.uploadFile(type, file);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("文件上传error",ex);
            throw new CmsServiceException("文件上传失败");
        }
    }

    /**
     * 文件下载(查看)
     *
     * @param response 网页请求
     * @param fileName 文件名
     * @param loadDate 上传时间
     * @param type 文件类型：申请授权-apply；上传头像-photo；新增记录-record
     * @return
     */
    @RequestMapping(value="/download/{type}/{loadDate}/{fileName}", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response,@PathVariable("type") String type, @PathVariable("fileName") String fileName, @PathVariable("loadDate") String loadDate) {
        try{
            fileService.downloadFile(response,type, fileName, loadDate);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("文件下载error",ex);
            throw new CmsServiceException("你访问的文件不存在");
        }
    }
}
