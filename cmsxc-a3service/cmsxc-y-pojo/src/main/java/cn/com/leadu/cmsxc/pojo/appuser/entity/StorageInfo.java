package cn.com.leadu.cmsxc.pojo.appuser.entity;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.common.tkmapper.IdGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 入库信息表
 * @author qiaomengnan
 * @ClassName: StorageInfo
 * @Description:
 * @date 2018/1/7
 */
@Data
public class StorageInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = IdGenerator.ID_GENERATOR)
    private String id;

    private Long taskId;// 工单id

    private String recoveryUserId;// 送车人用户名

    private String vehicleUser;// 车辆使用人

    private String finishImages; //收车完成时的图片记录

    private String finishVideo; //收车完成时视频

    private String finishRemark; //收车完成时备注

    private String managerRefuseRemark; //内勤驳回备注

    private String leaseRefuseRemark; //驳回备注

    private String parkingId; //停车场id

    private String state; //状态 "01":收车完成、"02":抵达停车场、"03":内勤确认中、"04":内勤退回待修改、"05":资料待上传、
                            // "06":资料审核中、"07":退回待修改、"08":入库完成、"09":终止入库、"99":修改停车场
    private Date arriveTime; //抵达停车场时间

    private String vehicleRecoveryReport; //车辆回收报告

    private String vehicleNormalInfo; //车辆基本情况

    private String storageFinanceList; //入库财务交接单

    private String vehicleAppearance; //车辆外观照

    private String vehicleGood; //车上物品

    private String otherImages; //其他附件

    private String video; //视频

    private Date finishTime; //完成入库时间

    private Date stopTime; //终止入库时间

    private Date changeParkingTime; //更换停车场时间

    private String storageWay; //入库方式 "0":主系统推送,"1":app上完成

    private String recoveryCompanyId; //完成收车公司id

    private String groupId; //完成小组id
}
