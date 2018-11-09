package cn.com.leadu.cmsxc.pojo.appuser.vo.applydetail;

import lombok.Data;

import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 申请详情——拒绝授权Vo
 */
@Data
public class RefuseAuthVo {

    private Date refuseTime; // 审核时间

    private String approveRemark;// 审核备注

}
