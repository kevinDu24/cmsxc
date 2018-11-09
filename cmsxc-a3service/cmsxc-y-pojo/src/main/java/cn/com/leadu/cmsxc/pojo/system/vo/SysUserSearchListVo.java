package cn.com.leadu.cmsxc.pojo.system.vo;

import cn.com.leadu.cmsxc.common.constant.enums.UserRoleEnums;
import cn.com.leadu.cmsxc.common.constant.excel.ExcelTitle;
import cn.com.leadu.cmsxc.common.entity.PageQuery;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/5/25.
 *
 * 账户注册查询返回用vo
 */
@Data
public class SysUserSearchListVo extends PageQuery {
    private String userId;// 用户id
    private String userName;// 用户姓名
    private String userRole;// 用户角色
    private String recoveryFullName;// 收车公司全称
    private String createTime; // 创建时间
    private Integer totalScore;// 总积分
    private Integer scanCount;// 扫描总量

    @ExcelTitle(value = "手机号",sort = 1)
    public String getUserId(){
        return this.userId;
    }
    @ExcelTitle(value = "姓名",sort = 2)
    public String getUserName(){
        return this.userName;
    }
    @ExcelTitle(value = "角色",sort = 3)
    public String getUserRole(){
        if(UserRoleEnums.RECOVERY_BOSS.getType().equals(this.userRole)){
            return "老板";
        }else if(UserRoleEnums.RECOVERY_MANAGER.getType().equals(this.userRole)){
            return "内勤人员";
        }else if(UserRoleEnums.RECOVERY_MEMBER.getType().equals(this.userRole)){
            return "业务员";
        }else if(UserRoleEnums.RECOVERY_OTHER.getType().equals(this.userRole)){
            return "散户";
        }
        return this.userRole;
    }
    @ExcelTitle(value = "所属收车公司",sort = 4)
    public String getRecoveryFullName(){
        return this.recoveryFullName;
    }
    @ExcelTitle(value = "注册时间",sort = 5)
    public String getCreateTime(){
        return this.createTime;
    }
    @ExcelTitle(value = "当前积分",sort = 6)
    public Integer getTotalScore(){
        return this.totalScore;
    }
    @ExcelTitle(value = "扫描总量",sort = 7)
    public Integer getScanCount(){
        return this.scanCount;
    }
}
