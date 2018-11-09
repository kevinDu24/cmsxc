package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/8/3.
 *
 * 案件相关附件信息
 */
@Data
public class AttachmentFileVo {

    private List<String> attachmentFileUrls = new ArrayList<>();// 附件url
    private String attachmentFileName;// 附件名称

}
