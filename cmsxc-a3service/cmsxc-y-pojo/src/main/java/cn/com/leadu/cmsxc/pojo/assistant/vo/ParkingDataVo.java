package cn.com.leadu.cmsxc.pojo.assistant.vo;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2018/4/13.
 *
 * 停车场人员上传资料vo
 */
@Data
public class ParkingDataVo {
    private String storageId;// 入库id
    private String haveKeyFlag;// 有无钥匙 "0":无，则只有1-4四张图片;"1":有，则有27张完整图片
    private String vehicleRecoveryReport; //车辆回收报告
    private String vehicleNormalInfo; //车辆基本情况
    private String storageFinanceList; //入库财务交接单
    private String vehicleGood; //车上物品
    private String otherImages; //其他附件
    private String video; //视频
    private String image1; //行驶证
    private String image2; //驾驶位座椅
    private String image3; //主驾座椅
    private String image4; //左前门铰链
    private String image5; //右后门铰链
    private String image6; //右前门铰链
    private String image7; //左后门铰链
    private String image8; //左前45度
    private String image9; //右后45度
    private String image10; //仪表盘
    private String image11; //安全带根部
    private String image12; //左后叶内侧导水槽
    private String image13; //右后叶内侧导水槽
    private String image14; //后盖铰链螺丝
    private String image15; //后备箱底板
    private String image16; //后备箱左侧梁
    private String image17; //后备箱右侧梁
    private String image18; //车内顶棚
    private String image19; //铭牌
    private String image20; //左前叶螺丝侧面
    private String image21; //右前叶螺丝侧面
    private String image22; //机盖铰链
    private String image23; //左前水箱框架
    private String image24; //右前水箱框架
    private String image25; //左前纵梁
    private String image26; //右前纵梁
    private String image27; //钥匙
}
