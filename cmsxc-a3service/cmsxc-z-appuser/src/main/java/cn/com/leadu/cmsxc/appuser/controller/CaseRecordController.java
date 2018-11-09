package cn.com.leadu.cmsxc.appuser.controller;

import cn.com.leadu.cmsxc.appuser.service.CaseRecordService;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.appuser.vo.CaseRecordParamVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.CaseRecordVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.ReminderVo;
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
 * Created by yuanzhenxia on 2018/4/13.
 *
 * 案件管理controller
 */
@RestController
@RequestMapping("caserecord")
public class CaseRecordController {
    private static final Logger logger = LoggerFactory.getLogger(CaseRecordController.class);
    @Autowired
    private CaseRecordService caseRecordService;
    /**
     * 获取案件记录数量(业务员或内勤或老板)
     *
     * @param systemUser 用户信息
     * @param vo 参数信息
     * @return
     */
    @RequestMapping(value = "/getCaseRecordCount", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> getCaseRecordCount(@AuthUserInfo SystemUser systemUser, @RequestBody CaseRecordParamVo vo) {
        try{
            return caseRecordService.getCaseRecordCount(systemUser, vo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("获取案件记录数量error",ex);
            throw new CmsServiceException("获取案件记录数量失败");
        }
    }
    /**
     * （业务员）案件列表
     *
     * @param systemUser 用户信息
     * @param vo 参数信息
     * @return
     */
    @RequestMapping(value = "/getCaseRecordListBySalesMan", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> getCaseRecordList(@AuthUserInfo SystemUser systemUser, @RequestBody CaseRecordParamVo vo) {
        try{
            return caseRecordService.getCaseRecordListBySalesMan(systemUser, vo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("案件列表error",ex);
            throw new CmsServiceException("案件列表失败");
        }
    }
    /**
     * （内勤人员）案件列表
     *
     * @param systemUser 用户信息
     * @param vo 参数信息
     * @return
     */
    @RequestMapping(value = "/getCaseRecordListByManager", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> getCaseRecordListByManager(@AuthUserInfo SystemUser systemUser, @RequestBody CaseRecordParamVo vo) {
        try{
            return caseRecordService.getCaseRecordListByManager(systemUser, vo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("案件列表error",ex);
            throw new CmsServiceException("案件列表失败");
        }
    }
    /**
     * 添加案件记录
     *
     * @param systemUser 用户信息
     * @param vo 参数信息
     * @return
     */
    @RequestMapping(value = "/createCaseRecord", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> createCaseRecord(@AuthUserInfo SystemUser systemUser, @RequestBody CaseRecordVo vo) {
        try{
            return caseRecordService.createCaseRecord(systemUser, vo);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("添加案件记录error",ex);
            throw new CmsServiceException("添加案件记录失败");
        }
    }
    /**
     * 催单推送
     *
     * @param systemUser 用户信息
     * @param vo 参数信息
     * @return
     */
    @RequestMapping(value = "/reminderPush", method = RequestMethod.POST)
    public ResponseEntity<RestResponse> reminderPush(@AuthUserInfo SystemUser systemUser, @RequestBody ReminderVo vo) {
        try{
            return caseRecordService.reminderPush(vo.getGroupId(), vo.getPlate(),systemUser, vo.getReminderContent(),vo.getAuthorizationUserId());
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("催单推送error",ex);
            throw new CmsServiceException("催单推送失败");
        }
    }
    /**
     * 查看案件记录详情
     *
     * @param systemUser 用户信息
     * @param taskId 任务id
     * @param groupId 分组id
     * @return
     */
    @RequestMapping(value = "/getCaseRecordDetail", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getCaseRecordDetail(@AuthUserInfo SystemUser systemUser, String taskId, String groupId,String authorizationUserId) {
        try{
            return caseRecordService.getCaseRecordDetail(taskId, systemUser);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("查看案件记录详情error",ex);
            throw new CmsServiceException("查看案件记录详情失败");
        }
    }
    /**
     * 查看案件相关附件信息
     *
     * @param systemUser 用户信息
     * @param taskId 任务id
     * @return
     */
    @RequestMapping(value = "/getAttachmentFile", method = RequestMethod.GET)
    public ResponseEntity<RestResponse> getAttachmentFile(@AuthUserInfo SystemUser systemUser, String taskId) {
        try{
            return caseRecordService.getAttachmentFile(taskId);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("查看案件相关附件信息error",ex);
            throw new CmsServiceException("查看案件相关附件信息失败");
        }
    }
}
