package cn.com.leadu.cmsxc.webclient.system.controller;

import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.rpc.FileUploadRpc;
import cn.com.leadu.cmsxc.webclient.constant.FileUploadUrl;
import cn.com.leadu.cmsxc.webclient.system.rpc.FileRpc;
import org.apache.commons.collections.map.HashedMap;
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
import java.util.Map;

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
    private FileRpc fileRpc;

    @Autowired
    private FileUploadRpc fileUploadRpc;
    /**
     * 文件上传
     *
     * @param file 文件
     * @return
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> uploadFile(MultipartFile file, String type) {
        try{
            Map<String,Object> params = new HashedMap();
            params.put("file", file);
            params.put("type", type);
            return fileUploadRpc.fileUpload(FileUploadUrl.systemFileUploadUrl, params);
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
     * @return
     */
    @RequestMapping(value="/download/{type}/{loadDate}/{fileName}", method = RequestMethod.GET)
    public byte[] downloadFile(HttpServletResponse response, @PathVariable("type") String type, @PathVariable("fileName") String fileName, @PathVariable("loadDate") String loadDate) {
        try{
            return fileRpc.downloadFile(fileName, loadDate,type);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("文件下载error",ex);
            throw new CmsServiceException("你访问的文件不存在");
        }
    }
}
