package cn.com.leadu.cmsxc.appuser.controller;

import cn.com.leadu.cmsxc.appuser.service.PlateUploadService;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 车牌扫描上传线索信息Controller
 */
@RestController
@RequestMapping("platescan")
public class PlateScanController {
    private static final Logger logger = LoggerFactory.getLogger(PlateScanController.class);
    @Autowired
    private PlateUploadService plateUploadService;
    /**
     * 车牌扫描上传线索信息
     *
     * @param systemUser 用户信息
     * @param paramterMap 参数集合（车牌号，线索地址，线索经纬度）
     * @param photoMsg 线索图片
     * @return
     */
    @RequestMapping(value = "/uploadPlateInfor", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> uploadPlateInfor(@AuthUserInfo SystemUser systemUser, @RequestParam Map<String,String> paramterMap, MultipartFile photoMsg) {
        try{
            return plateUploadService.uploadPlateInfor(systemUser , paramterMap ,photoMsg);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("车牌扫描上传线索error",ex);
            throw new CmsServiceException("线索上传失败");
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
    @RequestMapping(value="/download/{loadDate}/{fileName}", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response, @PathVariable("fileName") String fileName, @PathVariable("loadDate") String loadDate) {
        try{
             plateUploadService.downloadFile(response, fileName, loadDate);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("文件下载error",ex);
            throw new CmsServiceException("你访问的文件不存在");
        }
    }


}
