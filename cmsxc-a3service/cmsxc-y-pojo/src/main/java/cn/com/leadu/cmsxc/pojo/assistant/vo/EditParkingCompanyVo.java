package cn.com.leadu.cmsxc.pojo.assistant.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/4/13.
 *
 * 新增、编辑停车场公司vo
 */
@Data
public class EditParkingCompanyVo {
    private String userName;// 公司名称
    private String userId;// 用户名
    private String enableFlag;// 账号状态 0:禁用、1:正常
}
