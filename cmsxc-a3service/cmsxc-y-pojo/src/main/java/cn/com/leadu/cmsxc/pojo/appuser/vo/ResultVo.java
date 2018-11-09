package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/3/2.
 *
 * 获取授权书结果集
 */
@Data
public class ResultVo {
    private String pdfUrl; // 授权书url
    private String message; // 消息
    private String code;// 代码
}
