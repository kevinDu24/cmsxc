package cn.com.leadu.cmsxc.appuser.validator.sysuser.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by yuanzhenxia on 2018/1/17.
 *
 * 传参用vo
 */
@Data
public class VehicleVo {
    private String plate;// 车牌号码
    private String authorizationId;// 授权id
    private String checkFlag;// 是否查看过标志 0：未查看，1：已查看
    private String clueCheckFlag;// 命中线索是否查看过标志 0：未查看，1：已查看
    private String taskId;// 任务id
    private String taskStatus;// 任务状态

}
