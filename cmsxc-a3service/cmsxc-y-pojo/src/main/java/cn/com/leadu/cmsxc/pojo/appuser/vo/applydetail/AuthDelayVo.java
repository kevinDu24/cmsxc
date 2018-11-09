package cn.com.leadu.cmsxc.pojo.appuser.vo.applydetail;

import lombok.Data;

import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 申请详情——授权延期Vo
 */
@Data
public class AuthDelayVo {

    private Date delayTime; // 延期时间

    private String delayRemark;// 延期备注

    private Date authStartDate;// 授权有效开始时间

    private Date authOutTimeDate; // 授权失效时间

}
