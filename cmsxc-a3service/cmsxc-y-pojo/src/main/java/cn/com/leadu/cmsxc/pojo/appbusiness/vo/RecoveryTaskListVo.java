package cn.com.leadu.cmsxc.pojo.appbusiness.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/3/6.
 *
 * 我的派单返回用vo
 */
@Data
public class RecoveryTaskListVo {

    private int waitCount;// 待收车数量

    private int authorizationCount;// 已授权数量

    private int finishCount;// 已完成数量
}
