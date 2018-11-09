package cn.com.leadu.cmsxc.oauth2.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qiaomengnan
 * @ClassName: BaseEntity
 * @Description: entity基类
 * @date 2018/1/7
 */
@Data
public abstract class BaseEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date createTime;

    private String creator;

    private Date updateTime;

    private String updater;
}
