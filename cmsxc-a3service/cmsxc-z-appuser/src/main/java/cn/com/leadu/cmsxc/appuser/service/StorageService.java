package cn.com.leadu.cmsxc.appuser.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appuser.vo.RecoveryDataVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.RecoveryEvidenceVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.springframework.http.ResponseEntity;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 入库Service
 */
public interface StorageService {

    /**
     * 获取入库状态
     * @param taskId 工单id
     * @return
     */
    public ResponseEntity<RestResponse> getStorageState(Long taskId);

    /**
     * 推荐附近停车场
     * @param lat 纬度
     * @param lon 经度
     * @return
     */
    public ResponseEntity<RestResponse> getNearbyParking(Long taskId, Double lat, Double lon);

    /**
     * 提交收车完成资料
     * @param vo 提交信息
     * @return
     */
    public ResponseEntity<RestResponse> uploadEvidence(RecoveryEvidenceVo vo, SystemUser loginUser);

    /**
     * 获取已选停车场及二维码信息
     * @param taskId 工单id
     * @return
     */
     ResponseEntity<RestResponse> getSelectedParking(Long taskId, Double lat, Double lon);

    /**
     * 送车人上传资料
     * @param vo 提交信息
     * @return
     */
    ResponseEntity<RestResponse> uploadData(RecoveryDataVo vo, SystemUser loginUser);
}
