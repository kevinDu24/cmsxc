package cn.com.leadu.cmsxc.external.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.AppInfoPushVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryCompanyPushVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryStatusPushVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.VehicleTaskPushVo;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 委托机构推送Controller
 */
public interface TaskPushService {

    /**
     * 委托机构获取token
     *
     * @return
     */
    public ResponseEntity<RestResponse> getToken(String userName, String password);

    /**
     * 委托机构推送工单
     *
     * @return
     */
    ResponseEntity<RestResponse> pushVehicleTask(String token, VehicleTaskPushVo vo);

    /**
     * 委托机构推送收车app信息
     *
     * @return
     */
    ResponseEntity<RestResponse> pushAppInfo(String token, AppInfoPushVo vo);

    /**
     * 委托机构推送新的收车公司
     *
     * @return
     */
    public ResponseEntity<RestResponse> pushRecoveryCompany(String token, RecoveryCompanyPushVo vo);
    /**
     * 收车任务取消时，主系统推送取消时间
     *
     * @param token
     * @param vo
     * @return
     */
    ResponseEntity<RestResponse> pushRecoveryCanal(String token, RecoveryStatusPushVo vo);
    /**
     * 收车任务完成时，主系统推送完成时间
     *
     * @param token
     * @param vo
     * @return
     */
    ResponseEntity<RestResponse> pushRecoveryFinish(String token, RecoveryStatusPushVo vo);

    /**
     * 主系统收车车辆gps在线变离线时的推送接口
     *
     * @param token
     * @param vehicleIdentifyNum 车架号
     * @return
     */
    ResponseEntity<RestResponse> pushGpsOnline(String token, String vehicleIdentifyNum);

    /**
     * 主系统收车车辆服务费变更推送
     *
     * @param token
     * @param vehicleIdentifyNum 车架号
     * @return
     */
    public ResponseEntity<RestResponse> pushServiceFee(String token, String vehicleIdentifyNum, String serviceFee);

    /**
     * 批量导入收车公司——测试用
     *
     * @return
     */
    public ResponseEntity<RestResponse> test();
    /**
     * 收车任务取消时，确认该收车任务是否授权给其他收车公司
     *
     * @param token
     * @param vo
     * @return
     */
    ResponseEntity<RestResponse> cancelComfirm(String token, RecoveryStatusPushVo vo);

    /**
     * 赏金寻车二期上线时，寄存数据价格区间重新修正
     *
     * @return
     */
    ResponseEntity<RestResponse> rebuildPriceRange();

    /**
     * 赏金寻车二期上线时，填充收车公司老板注册码
     *
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> buildBossCode();

    /**
     * 赏金三期更新工单表历史数据三址、fp所在省份
     *
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> buildHistoryData();

}
