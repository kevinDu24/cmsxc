package cn.com.leadu.cmsxc.pojo.assistant.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/13.
 *
 * 角色管理中新增审核人员vo
 */
@Data
public class EditAuditorVo {
    private String userId;// 用户名id
    private String userName;// 用户姓名
    private String userRole;// 用户角色（目前都为‘12’）
    private String enableFlag;// 账号状态 0:禁用、1:正常
    private List<String> provinces;// 省份权限集合
}
