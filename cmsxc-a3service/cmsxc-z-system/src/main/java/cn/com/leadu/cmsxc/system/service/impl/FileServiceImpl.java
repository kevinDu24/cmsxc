package cn.com.leadu.cmsxc.system.service.impl;

import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.system.config.FileUploadProperties;
import cn.com.leadu.cmsxc.system.service.FileService;
import cn.com.leadu.cmsxc.system.util.constant.CommonUtils;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by yuanzhenxia on 2018/1/30.
 *
 * 文件上传和下载
 */
@Service
public class FileServiceImpl implements FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Autowired
    private FileUploadProperties fileUploadProperties;

    /**
     * 文件上传转发器
     * @param type
     * @param file
     * @return
     */
    public ResponseEntity<RestResponse> uploadFile(String type, MultipartFile file){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String uploadDate = sdf.format(new Date());
        if("newsImg".equals(type)){//新闻发布
            return saveFile(fileUploadProperties.getNewsImgPath() + uploadDate + "/", file, fileUploadProperties.getRequestNewsImgPath() + uploadDate + "/");
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","未指定上传类型"),
                HttpStatus.OK);
    }

    /**
     * 保存文件
     * @param savePath
     * @param file
     * @param serverPath
     * @return
     */
    public ResponseEntity<RestResponse> saveFile(String savePath, MultipartFile file, String serverPath){
        if (file != null && !file.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + CommonUtils.getFileSuffix(file.getOriginalFilename());
            try {
                System.out.println(serverPath + fileName);
                System.out.println(savePath + fileName);
                FileUtils.writeByteArrayToFile(new File(savePath + fileName), file.getBytes());
                Map map = Maps.newHashMap();
                map.put("url", serverPath + fileName);
                return new ResponseEntity<RestResponse>(
                        RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,map,""),
                        HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<RestResponse>(
                        RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","文件上传失败"),
                        HttpStatus.OK);
            }
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","文件为空,上传失败"),
                HttpStatus.OK);
    }



    /**
     * 文件下载
     * @param response
     * @param fileName 文件名
     * @return
     * @throws IOException
     */
    public byte[] downloadFile(HttpServletResponse response, String fileName, String loadDate, String type) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        String path = null;
        if("newsImg".equals(type)){
            path = fileUploadProperties.getNewsImgPath() +  loadDate + "/" + fileName;
        } else {
            String errorMessage = "抱歉. 你访问的文件不存在！";
            System.out.println(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return null;
        }

        File file = new File(path);
        if(!file.exists()){
            String errorMessage = "抱歉. 你访问的文件不存在！";
            System.out.println(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return null;
        }

        String mimeType= URLConnection.guessContentTypeFromName(file.getName());
        if(mimeType==null){
            mimeType = "application/octet-stream";
        }
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
        response.setContentLength((int)file.length());
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        FileCopyUtils.copy(inputStream, response.getOutputStream());
        return null;
    }
}
