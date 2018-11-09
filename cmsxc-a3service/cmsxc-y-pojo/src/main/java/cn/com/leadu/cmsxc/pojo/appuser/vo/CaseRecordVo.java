package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/13.
 *
 * 创建案件记录传参用vo
 */
@Data
public class CaseRecordVo {
    private String taskId;// 任务id
    private String caseTitle;// 案件标题
    private String position;// 定位位置
    private String detailContent;// 详细内容
    private String voiceUrls;// 语音url
    private String videoUrls;// 视频url
    private List<String> photoUrls;// 图片urls
    private String photoUrl;
    private String lat;// 纬度
    private String lon;// 经度
    private String userId;// 记录人id
    private String groupFlag;// 是否为组长 0：是，1：不是
    private Date recordDate;// 添加记录时间
    private String voiceLength;// 语音时长
    // .............赏金寻车3期增加  2018/08/02.............//
    private String subCaseTitle;// 案件副标题
    private String resultFlag;// 确认结果   0：否，1：是
}
