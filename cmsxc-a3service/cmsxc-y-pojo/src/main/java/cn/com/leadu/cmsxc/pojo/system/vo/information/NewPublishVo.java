package cn.com.leadu.cmsxc.pojo.system.vo.information;

import lombok.Data;

/**
 * Created by LEO on 16/9/29.
 */
@Data
public class NewPublishVo {
    private String title; //标题
    private String content; //内容
    private String coverUrl; //封面
    private String type; //类型
}
