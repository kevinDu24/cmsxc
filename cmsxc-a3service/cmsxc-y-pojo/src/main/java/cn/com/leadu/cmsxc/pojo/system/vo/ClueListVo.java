package cn.com.leadu.cmsxc.pojo.system.vo;

import cn.com.leadu.cmsxc.common.constant.excel.ExcelTitle;
import cn.com.leadu.cmsxc.common.entity.PageQuery;
import lombok.Data;


/**
 * Created by yuanzhenxia on 2018/5/25.
 *
 * 扫描线索返回值用vo
 */
@Data
public class ClueListVo extends PageQuery {
    private String plate ;// 车牌号
    private String vehicleIdentifyNum;// 车架号
    private String targetFlag;// 命中flag，0：未命中，1：已命中
    private String appAddr;// 地址
    private String uploadDate;// 上传时间
    private String userId;// 用户id
    private String recoveryFullName;// 收车公司全称
    private String type;// 线索扫描上传类型

    @ExcelTitle(value = "扫描内容",sort = 1)
    public String getPlate(){
        if("0".equals(this.type)){
            return this.plate;
        }else if ("1".equals(this.type)){
            return this.vehicleIdentifyNum;
        }
        return this.plate;
    }
    @ExcelTitle(value = "命中状态",sort = 2)
    public String getTargetFlag(){
        if("0".equals(this.targetFlag)){
            return "未命中";
        }else if ("1".equals(this.targetFlag)){
            return "已命中";
        }
        return this.targetFlag;
    }
    @ExcelTitle(value = "地址",sort = 3)
    public String getAppAddr(){
        return this.appAddr;
    }
    @ExcelTitle(value = "时间",sort = 4)
    public String getUploadDate(){
        return this.uploadDate;
    }
    @ExcelTitle(value = "账号",sort = 5)
    public String getUserId(){
        return this.userId;
    }
    @ExcelTitle(value = "所属收车公司",sort = 6)
    public String getRecoveryFullName(){
        return this.recoveryFullName;
    }

}
