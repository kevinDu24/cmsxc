package cn.com.leadu.cmsxc.assistant.service.impl;

import cn.com.leadu.cmsxc.assistant.config.FileUploadProperties;
import cn.com.leadu.cmsxc.assistant.service.FileService;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.common.util.DateUtil;
import cn.com.leadu.cmsxc.common.util.FileUtil;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuanzhenxia on 2018/1/30.
 *
 * 文件上传和下载
 */
@Service
public class FileServiceImpl implements FileService{
    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Autowired
    private FileUploadProperties fileUploadProperties;

    /**
     * 上传保存文件,返回文件访问路径
     * @param file
     * @param type 文件类型：上传头像-photo；
     * @return
     */
    public ResponseEntity<RestResponse> uploadFile(String type,MultipartFile file){
        String uploadDate = DateUtil.dateToStr(new Date(), DateUtil.formatStr_yyyyMMddS_);
        String nameName = DateUtil.dateToStr(new Date(), DateUtil.formatStr_yyyyMMddHHmmssS_) + "_"+ FileUtil.getFileName(file.getOriginalFilename());
        String fileUrl = "";
        if("photo".equals(type)){
            fileUrl = FileUtil.saveFile(nameName, fileUploadProperties.getPhotoPath() + uploadDate + "/", file, fileUploadProperties.getRequestPhotoPath() + uploadDate + "/" );
        }else{
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","文件类型不能为空！"),
                    HttpStatus.OK);
        }
        Map<String, String> fileNameMap = new HashMap<>();
        fileNameMap.put("fileUrl", fileUrl );
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,fileNameMap,""),
                HttpStatus.OK);
    }
    /**
     * 文件下载
     *
     * @param response 网页请求
     * @param fileName 文件名
     * @param loadDate 上传时间
     * @param type 文件类型：上传头像-photo；
     */
    public void downloadFile(HttpServletResponse response,String type,String fileName, String loadDate) {
        response.setContentType("text/html;charset=utf-8");
        String path = "";
        if("photo".equals(type)){
            path = fileUploadProperties.getPhotoPath() + loadDate + "/" + fileName;
        }
        File file = new File(path);
        try {
            if (file.exists()) {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName());
                if (mimeType == null) {
                    mimeType = "application/octet-stream";
                }
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }else{
                throw new CmsServiceException("你访问的文件不存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("你访问的文件不存在error",e);
            throw new CmsServiceException("你访问的文件不存在！");
        }
    }
}
