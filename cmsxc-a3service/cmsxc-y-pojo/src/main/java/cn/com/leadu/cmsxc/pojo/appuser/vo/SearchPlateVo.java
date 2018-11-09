package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/2/24.
 *
 * 查询车牌号传参用vo
 */
@Data
public class SearchPlateVo {
    private String plate;// 车牌号
    private String taskStatus;// 任务状态
    private Date nowDate;// 当前时间
 }
