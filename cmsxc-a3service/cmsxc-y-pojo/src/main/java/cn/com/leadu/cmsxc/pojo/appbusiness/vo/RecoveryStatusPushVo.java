package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/2/27.
 *
 * 主系统推送收车任务状态用vo
 */
@Data
public class RecoveryStatusPushVo {
    private String recoveryCompanyId;// 收车公司id
    private String plate;// 车牌号
    private String vehicleIdentifyNum;// 车架号
    private String canalDate;// 取消时间
    private String finishDate;// 完成时间
    private String cancelPaperUrl;// 取消证书url
    private String cancelReason;// 取消原因
}
