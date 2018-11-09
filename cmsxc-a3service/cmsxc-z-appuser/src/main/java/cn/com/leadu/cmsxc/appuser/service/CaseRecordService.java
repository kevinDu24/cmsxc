package cn.com.leadu.cmsxc.appuser.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appuser.vo.CaseRecordParamVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.CaseRecordVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.springframework.http.ResponseEntity;

/**
 * Created by yuanzhenxia on 2018/4/13.
 *
 * 案件记录Service
 */
public interface CaseRecordService {
    /**
     * 添加催收记录
     *
     * @param systemUser 用户信息
     * @param vo 参数信息
     * @return
     */
    ResponseEntity<RestResponse> createCaseRecord(SystemUser systemUser, CaseRecordVo vo);

    /**
     * （业务员）根据价格区间，gps有无，线索有无，违章有无等条件，搜索案件信息
     *
     * @param vehicleTaskVo 画面参数信息
     * @return
     */
    ResponseEntity<RestResponse> getCaseRecordListBySalesMan(SystemUser systemUser, CaseRecordParamVo vehicleTaskVo);

    /**
     * （内勤人员）根据价格区间，gps有无，线索有无，违章有无等条件，搜索案件信息
     *
     * @param vehicleTaskVo 画面参数信息
     * @return
     */
    ResponseEntity<RestResponse> getCaseRecordListByManager(SystemUser systemUser, CaseRecordParamVo vehicleTaskVo);
    /**
     * 催单推送
     *
     * @param groupId 分组id
     * @param plate 车牌号
     * @param systemUser 用户id
     * @return
     */
    ResponseEntity<RestResponse> reminderPush(String groupId, String plate, SystemUser systemUser, String reminderContent, String authorizationUserId);
    /**
     * 查看案件记录详情
     *
     * @param taskId 任务id
     * @param systemUser 用户信息
     * @return
     */
    ResponseEntity<RestResponse> getCaseRecordDetail(String taskId, SystemUser systemUser);
    /**
     * 获取案件记录数量
     *
     * @param systemUser
     * @param caseRecordParamVo
     * @return
     */
    ResponseEntity<RestResponse> getCaseRecordCount(SystemUser systemUser, CaseRecordParamVo caseRecordParamVo);
    /**
     * 查看案件相关附件信息
     *
     * @param taskId 任务id
     * @return
     */
    ResponseEntity<RestResponse> getAttachmentFile(String taskId);

}
