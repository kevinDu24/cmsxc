package cn.com.leadu.cmsxc.pojo.appuser.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 入库日志表
 * @author qiaomengnan
 * @ClassName: StorageInfo
 * @Description:
 * @date 2018/1/7
 */
@Data
public class StorageHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private String storageId;// 入库信息表id

    private String operateContent;// 操作内容   "01":收车完成、"02":抵达停车场、"03":送车人资料上传、"06":停车场资料上传、
                                    // "07":资料驳回、"08":入库完成、"09":终止入库、"99":修改停车场
    private Date operateTime; //操作时间

    private String operator; //操作人

    private String operatorName; //操作人姓名

    private String remark; //备注
}
