package cn.com.leadu.cmsxc.appuser.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by pengchao on 2016/09/19.
 */
@ConfigurationProperties(prefix = "file")
@Data
public class FileUploadProperties {
    // 线索图片路径
    private String cluePath;
    // 申请授权图片路径
    private String authorizationPath;
    // 线索图片请求路径
    private String requestCluePath;
    // 申请授权图片请求路径
    private String requestAuthorizationPath;
    // 头像路径
    private String photoPath;
    // 头像请求路径
    private String requestPhotoPath;
    // 催收记录路径
    private String recordPath;
    // 催收记录请求路径
    private String requestRecordPath;
    // 今日资讯路径
    private String newsImgPath;
    // 今日资讯请求路径
    private String requestNewsImgPath;
    // 收车完成相关路径
    private String recoveryfinishPath;
    // 收车完成相关路径
    private String requestrecoveryFinishPath;
}
