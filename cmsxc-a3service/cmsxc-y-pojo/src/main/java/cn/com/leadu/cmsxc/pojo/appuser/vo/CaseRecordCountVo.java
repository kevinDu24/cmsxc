package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/5/2.
 *
 * 案件数量接口
 */
@Data
public class CaseRecordCountVo {

    private int followCount;// 跟进中数量

    private int finishCount;// 已完成数量
}
