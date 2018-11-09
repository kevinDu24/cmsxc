package cn.com.leadu.cmsxc.assistant.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.assistant.vo.CaseRecordListParamVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yuanzhenxia on 2018/5/7.
 *
 * 寻车助手---案件管理service
 */
public interface CaseRecordService {
    /**
     * 根据委托公司id获取合作收车公司信息
     *
     * @param systemUser 用户信息
     * @return
     */
    ResponseEntity<RestResponse> getRecoveryCompanys(SystemUser systemUser);
    /**
     * 寻车助手---案件管理列表
     *
     * @param systemUser 用户信息
     * @param vo 参数信息
     * @return
     */
    ResponseEntity<RestResponse> getCaseRecordList(SystemUser systemUser, CaseRecordListParamVo vo);
    /**
     * 寻车助手---案件详情
     *
     * @param taskId 任务id
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    ResponseEntity<RestResponse> getCaseRecordDetail(String taskId , String recoveryCompanyId);
    /**
     * 催单推送
     *
     * @param plate 车牌号
     * @param reminderContent 催单内容
     * @return
     */
    ResponseEntity<RestResponse> reminderPush(SystemUser user ,String plate, String reminderContent);

}
