package cn.com.leadu.cmsxc.pojo.apprequestlog.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author qiaomengnan
 * @ClassName: AppRequestLog
 * @Description:
 * @date 2018/1/30
 */
@Data
public class AppRequestLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

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

    /**
     * @Fields  : 当前用户手机号
     */
    private String userPhone;

}
