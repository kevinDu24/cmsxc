package cn.com.leadu.cmsxc.extend.vo;

import lombok.Data;

/**
 * @author qiaomengnan
 * @ClassName: AppRequestLog
 * @Description: 日志记录实体
 * @date 2018/1/30
 */
@Data
public class AppRequestLogTmpVo {

    /** 
     * @Fields  : 请求路径
     */ 
    private String requestUrl;

    /**
     * @Fields  : 请求参数
     */
    private String requestParams;

    /**
     * @Fields  : 客户端标识 0.安卓 1.ios
     */
    private Integer client;

    /**
     * @Fields  : 客户端版本
     */
    private String version;

}
