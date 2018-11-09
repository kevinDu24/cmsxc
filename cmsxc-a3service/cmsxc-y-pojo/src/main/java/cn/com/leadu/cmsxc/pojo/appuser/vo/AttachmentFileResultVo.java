package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by yuazhenxia on 2018/8/3.
 *
 * 获取车辆附件信息结果集
 */
@Data
public class AttachmentFileResultVo {
    private List<AttachmentFileVo> data; // 车辆附件信息
    private String message; // 消息
    private String code;// 代码
}
