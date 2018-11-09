package cn.com.leadu.cmsxc.pojo.system.vo;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import lombok.Data;


/**
 * Created by yuanzhenxia on 2018/5/25.
 *
 * 车牌扫描传参用vo
 */
@Data
public class ClueListParamVo extends PageQuery {

    private String plate;// 车牌号
    private String targetFlag;// 命中状态
    private String startDate;// 开始时间
    private String endDate;// 结束时间
    private String recoveryCompanyId;// 收车公司id
}
