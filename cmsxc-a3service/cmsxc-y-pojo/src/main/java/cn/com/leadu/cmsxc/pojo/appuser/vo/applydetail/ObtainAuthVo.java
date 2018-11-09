package cn.com.leadu.cmsxc.pojo.appuser.vo.applydetail;

import lombok.Data;

import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 申请详情——获得授权Vo
 */
@Data
public class ObtainAuthVo {

    private Date passTime; // 审核时间

    private String approveRemark;// 审核备注

    private Date authStartDate;// 授权有效开始时间

    private Date authOutTimeDate; // 授权失效时间

}
