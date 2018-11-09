package cn.com.leadu.cmsxc.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qiaohao
 * @ClassName: PageQuery
 * @Description: 分页查询对象抽象类
 * @date: 2017/8/30 19:55
 */
@Data
public class PageQuery implements Page,Serializable {

    private static final long serialVersionUID = 1L;

    public PageQuery() {

    }

    public PageQuery(Page page){
        this.currenPage = page.getCurrenPage();
        this.pageSize = page.getPageSize();
    }

    public PageQuery(Integer currentPage, Integer pageSize) {
        this.currenPage = currentPage;
        this.pageSize = pageSize;
    }

    public PageQuery(Integer currentPage) {
        this.currenPage = currentPage;
    }

    /**
     * @Fields : 当前页数
     */
    private Integer currenPage = 1;

    public Integer getCurrenPage(){
        if(start != null && length != null)
            //计算页码
            this.currenPage = (start / length) + 1;
        return currenPage;
    }

    /**
     * @Fields : 每页显示条数
     */
    private Integer pageSize = 10;

    public Integer getPageSize(){
        if(length != null)
            pageSize = length;
        return pageSize;
    }

    /**
     * @Fields  : datatable插件参数
     */
    private Integer start ;

    /**
     * @Fields  : datatable插件参数
     */
    private Integer length ;

    /**
     * @Fields  : datatable参数
     */
    private Integer draw ;

    @JsonIgnore
    @JSONField(serialize = false)
    public  PageQuery getPageQuery(){
        return this;
    }
}