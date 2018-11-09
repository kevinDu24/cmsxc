package cn.com.leadu.cmsxc.data.base.pojo;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.List;

/**
 * @author qiaomengnan
 * @ClassName: PageQueryInfo
 * @Description:
 * @date 2018/1/10
 */
@Data
public class PageInfoExtend<T> extends PageInfo<T> {

    private Integer draw;

    private Long recordsTotal;

    private Long recordsFiltered;

    private List<T> data;

}
