package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.data.appuser.dao.VehicleAuthorizationDao;
import cn.com.leadu.cmsxc.data.appuser.repository.VehicleAuthorizationRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.AuthorizationVo;
import cn.com.leadu.cmsxc.pojo.appuser.entity.VehicleAuthorization;
import cn.com.leadu.cmsxc.pojo.appuser.vo.AuthorizationListVo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.ViewMaterialVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.ApprovalListVo;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 单车授权
 */
@Component
public class VehicleAuthorizationRepositoryImpl  extends AbstractBaseRepository<VehicleAuthorizationDao,VehicleAuthorization> implements VehicleAuthorizationRepository {
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    @Override
    public List<VehicleAuthorization> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }
    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    @Override
    public VehicleAuthorization selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }
    /**
     * 登录单车授权表信息
     * @param vehicleAuthorization
     */
    @Override
    public void insertOne(VehicleAuthorization vehicleAuthorization){
        super.insert(vehicleAuthorization);
    }

    /**
     * 通过主键获取
     * @param id
     * @return
     */
    public VehicleAuthorization selectByPrimaryKey(String id){
        return super.selectByPrimaryKey(id);
    }

    /**
     * 分页查询指定用户指定授权状态的授权列表
     *
     * @param userId 用户id
     * @param authorizationStatus 授权状态
     * @param page 当前页
     * @param size 每页个数
     * @return
     */
    public List<AuthorizationListVo> selectByUserIdAndAuthorizationstatus(String userId, String authorizationStatus,String nowDate, int page, int size){
        return baseDao.selectByUserIdAndAuthorizationstatus(userId, authorizationStatus,nowDate, page, size);

    }
    /**
     * 资料预览
     *
     * @param authorizationId 授权id
     * @return
     */
    public List<ViewMaterialVo> selectByAuthorizationId( String authorizationId){
        return baseDao.selectByAuthorizationId(authorizationId);
    }
    /**
     * 根据用户id和任务id获取用户最新申请信息
     *
     * @param taskId 任务id
     * @param userId 用户id
     * @return
     */
    public ViewMaterialVo selectByUserIdAndTaskId( String userId, String taskId){
        return baseDao.selectByUserIdAndTaskId(userId, taskId);
    }
    /**
     * 根据主键更新表
     *
     * @param vehicleAuthorization
     */
    @Override
    public void updateByPrimaryKeySelective(VehicleAuthorization vehicleAuthorization) {
        super.updateByPrimaryKeySelective(vehicleAuthorization);
    }
    /**
     * 根据授权状态获取授权列表数据
     *
     * @param authorizationVo 查询条件
     * @param page 当前页
     * @param size 每页条数
     * @return
     */
    public List<cn.com.leadu.cmsxc.pojo.appbusiness.vo.AuthorizationListVo>  selectByAuthorizationstatus(AuthorizationVo authorizationVo, int flag, int page, int size, String leaseId){
        return baseDao.selectByAuthorizationstatus(authorizationVo,flag, page, size, leaseId);
    }
    /**
     * 委托公司id获取该委托公司下的待审批数量
     *
     * @param leaseId 委托公司id
     * @return
     */
    public Long selectCount(String leaseId,String userRole,List<String> provinces){
        return baseDao.selectCount1(leaseId,userRole,provinces);
    }
    /**
     * 分页获取审批列表（app用）
     * @param status 授权
     * @param condition 检索条件
     * @param leaseId 委托公司id
     * @param page
     * @param size
     * @return
     */
    public List<ApprovalListVo> selectApprovalList(String userRole,List<String> provinces, String status,
                                                   String condition,
                                                   String leaseId,
                                                   int flag,
                                                   int page,
                                                   int size){
        return baseDao.selectApprovalList(userRole,provinces,status,condition,leaseId,flag,page,size);
    }
}
