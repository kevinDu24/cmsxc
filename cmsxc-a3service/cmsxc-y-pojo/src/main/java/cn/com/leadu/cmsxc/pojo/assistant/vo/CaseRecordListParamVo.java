package cn.com.leadu.cmsxc.pojo.assistant.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/5/8.
 *
 * 寻车助手--案件管理列表 传参用vo
 */
@Data
public class CaseRecordListParamVo {
    private String status;// 受理状态   0：未受理；1：已受理
    private String leaseId;// 委托公司id
    private String recoveryCompanyId;// 收车公司id
    private String leaseCompanyUserName;// 委托公司用户名
    private String plate;// 车牌号
    private int page;// 当前页
    private int size;// 每页数目
    private List<String> provinces;// 审核人员管辖省份
    private String userRole;// 用户角色
}
