package cn.com.leadu.cmsxc.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qiaomengnan
 * @ClassName: BaseEntity
 * @Description: entity基类 所有的业务类 需要继承
 * @date 2018/1/7
 */
@Data
public abstract class BaseEntity<T> implements Entity,Serializable {

    private static final long serialVersionUID = 1L;

    private Date createTime;

    private String creator;

    private Date updateTime;

    private String updater;
}
