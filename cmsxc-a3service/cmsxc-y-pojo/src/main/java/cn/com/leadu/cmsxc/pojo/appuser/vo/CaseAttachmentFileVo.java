package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by yunzhenxia on 2018/8/3.
 *
 * 案件附件信息
 */
@Data
public class CaseAttachmentFileVo {
    private String name;// 车主姓名

    private String idCard; //身份证号码

    private String phoneNum; //车主手机号码

    private String workAddress; //家庭地址

    private String homeAddress; //家庭地址

    private String lifeAddress; //常住地址

    private List<AttachmentFileVo> attachmentFileVoList;// 相关附件信息
}
