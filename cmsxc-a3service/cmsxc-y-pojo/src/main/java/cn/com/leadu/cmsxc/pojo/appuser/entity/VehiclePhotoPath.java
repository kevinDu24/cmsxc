package cn.com.leadu.cmsxc.pojo.appuser.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 附件关联表（照片路径保存）
 */
@Data
public class VehiclePhotoPath extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private String photoUuid;// 汽车照片附件标识

    private String type; //0：视频，1：图片，2：正面照，3：外观照

    private String photoUrl;// 汽车照片保存路径

    private String remark;// 备注

}
