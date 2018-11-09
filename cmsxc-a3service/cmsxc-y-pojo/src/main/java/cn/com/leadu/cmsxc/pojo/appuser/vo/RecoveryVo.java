package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/2/24.
 *
 * 我的派单画面传参用
 */
@Data
public class RecoveryVo {
    private String recoveryCompanyId;// 收车公司id
    private String groupId;// 所属小组id
    private int status; // 任务状态
    private String plate; // 车牌号
    private int page; // 当前页
    private int size; // 每页条数
    private Date nowDate = new Date();// 当前时间

}
