package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;
import java.util.Date;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 我的线索信息返回画面用
 */
@Data
public class ClueVo {
    private String plate;// 车牌号

    private String clueAddress;// 手机定位地址

    private Date clueTime; // 获取线索时间

    private String checkFlag;// 是否查看过此线索（0：未查看；1：已查看）

    private String effectiveFlag; // 工单状态是否有效、即是否为正常（0:失效，1：有效）

    private String vehicleIdentifyNum;// 车架号

    private String type;// 扫描方式，0:车牌号、1:车架号
}
