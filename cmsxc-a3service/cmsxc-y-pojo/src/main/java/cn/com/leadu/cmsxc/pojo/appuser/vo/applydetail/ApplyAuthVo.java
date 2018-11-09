package cn.com.leadu.cmsxc.pojo.appuser.vo.applydetail;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 申请详情——申请授权Vo
 */
@Data
public class ApplyAuthVo {

    private Date applyTime; // 申请时间

    private String name;// 申请人姓名

    private String phoneNum;// 手机号

    private String identityNum; // 身份证号

    private String remark;// 备注

    private String address; // 定位

    private String video; // 视频

    private String frontPhoto; // 车辆正面照

    private String exteriorPhoto; // 车辆外观照

    private List<String> otherPhoto; // 其他照片集合
}
