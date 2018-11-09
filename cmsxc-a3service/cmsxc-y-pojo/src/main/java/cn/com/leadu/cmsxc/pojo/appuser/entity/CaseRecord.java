package cn.com.leadu.cmsxc.pojo.appuser.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by yuanzhenxia on 2018/4/13.
 *
 * 案件记录表
 */
@Data
public class CaseRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;
    private Long taskId;// 任务id
    private String recoveryCompanyId;// 收车公司id
    private String recoveryGroupId;// 分组id
    private String caseTitle;// 案件标题 01：核实地址，02：上门寻找客户，03：寻找案件车辆，04：使用板车-装车，05：使用板车-运输，06：使用板车-卸车，07：使用钥匙，08：其他
    private String recordUserId;// 记录人id
    private String position;// 定位位置
    private String detailContent;// 详细内容
    private String voiceUrls;// 语音url
    private String videoUrls;// 视频url
    private String photoUrls;// 图片url
    private String lat;// 纬度
    private String lon;// 经度
    private String voiceLength;// 语音时长
    // .............赏金寻车3期增加  2018/08/02.............//
    private String subCaseTitle;// 案件副标题
    private String resultFlag;// 确认结果   0：否，1：是

}
