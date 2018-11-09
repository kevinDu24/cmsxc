package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.data.appuser.dao.CaseRecordDao;
import cn.com.leadu.cmsxc.data.appuser.repository.CaseRecordRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.CaseRecord;
import cn.com.leadu.cmsxc.pojo.appuser.vo.*;
import cn.com.leadu.cmsxc.pojo.assistant.vo.CaseRecordListParamVo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/4/13.
 *
 * 案件记录表
 */
@Component
public class CaseRecordRepositoryImpl extends AbstractBaseRepository<CaseRecordDao,CaseRecord> implements CaseRecordRepository {
    /**
     * 登录案件记录表
     *
     * @param caseRecord
     */
    public void insertOne(CaseRecord caseRecord){
        super.insert(caseRecord);
    }
    /**
     * (业务员)根据价格区间，gps有无，线索有无，违章有无等条件，搜索案件信息
     *
     * @param caseRecordParamVo 画面信息
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    public List<CaseRecordListVo> selectCaseRecordListBySalesMan(CaseRecordParamVo caseRecordParamVo, int page, int size){
        return baseDao.selectCaseRecordListBySalesMan(caseRecordParamVo, page, size);
    }
    /**
     * (内勤人员)根据价格区间，gps有无，线索有无，违章有无等条件，搜索案件信息
     *
     * @param caseRecordParamVo 画面信息
     * @param recoveryCompanyId 收车公司id
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    public List<CaseRecordListVo> selectCaseRecordListByManager(CaseRecordParamVo caseRecordParamVo,String recoveryCompanyId, int page, int size){
        return baseDao.selectCaseRecordListByManager(caseRecordParamVo,recoveryCompanyId, page, size);
    }
    /**
     * (业务员)根据价格区间，gps有无，线索有无，违章有无等条件，搜索案件信息数量
     *
     * @param caseRecordParamVo 画面信息
     * @return
     */
    public Integer selectCaseRecordCountBySalesMan(CaseRecordParamVo caseRecordParamVo){
        return baseDao.selectCaseRecordCountBySalesMan(caseRecordParamVo);
    }
    /**
     * (内勤人员)根据价格区间，gps有无，线索有无，违章有无等条件，搜索案件信息数量
     *
     * @param caseRecordParamVo 画面信息
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    public Integer selectCaseRecordCountByManager(CaseRecordParamVo caseRecordParamVo,String recoveryCompanyId){
        return baseDao.selectCaseRecordCountByManager(caseRecordParamVo,recoveryCompanyId);
    }

    /**
     * 查看案件记录详情
     *
     * @param taskId 任务id
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    public List<CaseRecordVo> selectCaseRecordDetails(String taskId, String recoveryCompanyId){
        return baseDao.selectCaseRecordDetail(taskId, recoveryCompanyId );
    }
    /**
     * 寻车助手--案件管理列表
     *
     * @param vo 画面信息
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    public List<cn.com.leadu.cmsxc.pojo.assistant.vo.CaseRecordListVo> selectCaseRecordListByLeaseAuditor(CaseRecordListParamVo vo, int page, int size){
        return baseDao.selectCaseRecordListByLeaseAuditor(vo, page, size);
    }
    /**
     * 查看案件记录详情
     *
     * @param taskId 任务id
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    public List<cn.com.leadu.cmsxc.pojo.assistant.vo.CaseRecordVo> selectCaseRecordDetail(String taskId, String recoveryCompanyId){
        return baseDao.selectCaseRecordDetailByTaskIdAndRecoveryCompanyId(taskId, recoveryCompanyId);
    }

    /**
     * 根据委托公司id获取合作的收车公司id
     *
     * @param leaseId 委托公司id
     * @return
     */
    public List<String> selectRecoveryCompanyId(String leaseId){
        return baseDao.selectRecoveryCompanyId(leaseId);
    }
    /**
     * 根据工单id与收车公司id，获取板车使用起点地址
     *
     * @param taskId 工单任务id
     * @param recoveryCompanyId 收车公司id
     * @return
     */
    public List<StartLonLatVo> selectStartAddressInfo(String taskId, String recoveryCompanyId){
        return baseDao.selectStartAddressInfo(taskId, recoveryCompanyId);
    }


}
