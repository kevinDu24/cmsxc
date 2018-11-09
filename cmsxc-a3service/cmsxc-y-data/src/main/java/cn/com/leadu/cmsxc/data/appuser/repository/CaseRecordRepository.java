package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryGroup;
import cn.com.leadu.cmsxc.pojo.appuser.entity.CaseRecord;
import cn.com.leadu.cmsxc.pojo.appuser.vo.*;
import cn.com.leadu.cmsxc.pojo.assistant.vo.CaseRecordListParamVo;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/13.
 *
 * 案件记录表
 */
public interface CaseRecordRepository {
    /**
     * 登录案件记录表
     *
     * @param caseRecord
     */
    void insertOne(CaseRecord caseRecord);
    /**
     * （业务员）根据价格区间，gps有无，线索有无，违章有无等条件，搜索案件信息
     *
     * @param vehicleTaskVo 画面信息
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    List<CaseRecordListVo> selectCaseRecordListBySalesMan(CaseRecordParamVo vehicleTaskVo, int page, int size);
    /**
     * (内勤人员)根据价格区间，gps有无，线索有无，违章有无等条件，搜索案件信息数量
     *
     * @param vehicleTaskVo 画面信息
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    Integer selectCaseRecordCountByManager(CaseRecordParamVo vehicleTaskVo,String recoveryCompanyId);
    /**
     * 根据价格区间，gps有无，线索有无，违章有无等条件，搜索案件信息数量
     *
     * @param vehicleTaskVo 画面信息
     * @return
     */
   Integer selectCaseRecordCountBySalesMan(CaseRecordParamVo vehicleTaskVo);
    /**
     * (内勤人员)根据价格区间，gps有无，线索有无，违章有无等条件，搜索案件信息
     *
     * @param vehicleTaskVo 画面信息
     * @param recoveryCompanyId 收车公司id
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    List<CaseRecordListVo> selectCaseRecordListByManager(CaseRecordParamVo vehicleTaskVo,String recoveryCompanyId, int page, int size);

    /**
     * 查看案件记录详情
     *
     * @param taskId 任务id
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    List<CaseRecordVo> selectCaseRecordDetails(String taskId, String recoveryCompanyId);

    /**
     * 寻车助手--案件管理列表
     *
     * @param vo 画面信息
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    List<cn.com.leadu.cmsxc.pojo.assistant.vo.CaseRecordListVo> selectCaseRecordListByLeaseAuditor(CaseRecordListParamVo vo, int page, int size);
    /**
     * 查看案件记录详情
     *
     * @param taskId 任务id
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    List<cn.com.leadu.cmsxc.pojo.assistant.vo.CaseRecordVo> selectCaseRecordDetail(String taskId, String recoveryCompanyId);
    /**
     * 根据委托公司id获取合作的收车公司id
     *
     * @param leaseId 委托公司id
     * @return
     */
    List<String> selectRecoveryCompanyId(String leaseId);

    /**
     * 根据工单id与收车公司id，获取板车使用起点地址
     *
     * @param taskId 工单任务id
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    List<StartLonLatVo> selectStartAddressInfo(String taskId, String recoveryCompanyId);

}
