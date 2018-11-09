package cn.com.leadu.cmsxc.assistant.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by pengchao on 2016/09/19.
 */
@ConfigurationProperties(prefix = "file")
@Data
public class FileUploadProperties {
    // 头像路径
    private String photoPath;
    // 头像请求路径
    private String requestPhotoPath;
}
