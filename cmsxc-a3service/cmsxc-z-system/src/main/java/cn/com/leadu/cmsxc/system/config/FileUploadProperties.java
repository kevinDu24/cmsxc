package cn.com.leadu.cmsxc.system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by pengchao on 2016/09/19.
 */
@ConfigurationProperties(prefix = "file")
@Data
public class FileUploadProperties {
    // 今日资讯路径
    private String newsImgPath;
    // 今日资讯请求路径
    private String requestNewsImgPath;
}
