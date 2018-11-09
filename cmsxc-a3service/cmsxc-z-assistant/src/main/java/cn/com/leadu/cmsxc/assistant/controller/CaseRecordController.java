package cn.com.leadu.cmsxc.assistant.controller;

import cn.com.leadu.cmsxc.assistant.service.CaseRecordService;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appuser.vo.ReminderVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.CaseRecordListParamVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yuanzhenxia on 2018/5/7.
 *
 * 寻车助手--案件列表
 */
@RestController
@RequestMapping("caserecord")
public class CaseRecordController {
    private static final Logger logger = LoggerFactory.getLogger(CaseRecordController.class);
    @Autowired
    private CaseRecordService caseRecordService;
    /**
     * 根据委托公司id获取合作收车公司信息
     *
     * @param systemUser 用户信息
     * @return
     */
    @RequestMapping(value = "/getRecoveryCompanys", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getRecoveryCompanys(@AuthUserInfo SystemUser systemUser) {
        try{
            return caseRecordService.getRecoveryCompanys(systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取合作收车公司信息error",ex);
            throw new CmsServiceException("获取合作收车公司信息失败");
        }
    }
    /**
     * 寻车助手---案件管理列表
     *
     * @param systemUser 用户信息
     * @param vo 参数信息
     * @return
     */
    @RequestMapping(value = "/getCaseRecordList", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> getCaseRecordList(@AuthUserInfo SystemUser systemUser, @RequestBody CaseRecordListParamVo vo) {
        try{
            return caseRecordService.getCaseRecordList(systemUser,vo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取案件管理列表error",ex);
            throw new CmsServiceException("获取案件管理列表失败");
        }
    }
    /**
     * 寻车助手---案件详情
     *
     * @param systemUser 用户信息
     * @param taskId 任务id
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    @RequestMapping(value = "/getCaseRecordDetail", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getCaseRecordDetail(@AuthUserInfo SystemUser systemUser, String taskId,String recoveryCompanyId) {
        try{
            return caseRecordService.getCaseRecordDetail(taskId,recoveryCompanyId);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取案件详情error",ex);
            throw new CmsServiceException("获取案件详情失败");
        }
    }
    /**
     * 催单推送
     *
     * @param systemUser 用户信息
     * @param reminderContent 催单内容
     * @param plate 车牌号
     * @return
     */
    @RequestMapping(value = "/reminderPush", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> reminderPush(@AuthUserInfo SystemUser systemUser, String plate,String reminderContent) {
        try{
            return caseRecordService.reminderPush(systemUser,plate,reminderContent);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("催单推送error",ex);
            throw new CmsServiceException("催单推送失败");
        }
    }

}
