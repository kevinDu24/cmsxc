package cn.com.leadu.cmsxc.data.system.dao;

import cn.com.leadu.cmsxc.data.base.config.BaseDao;
import cn.com.leadu.cmsxc.pojo.appuser.vo.ScanInfoVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.UserRoleListVo;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserSearchListVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserSearchVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SystemUserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * Created by yuanzhenxia on 2018/1/15.
 *
 * 用户信息
 */
public interface SystemUserDao extends BaseDao<SystemUser> {

    /**
     * 分页查询指定用户列表
     *
     * @param systemUserVo 查询条件
     * @return
     */
    List<SystemUserVo> findSysUserByPage(@Param("systemUserVo") SystemUserVo systemUserVo, @Param("role") String role,
                                         @Param("recoveryCompanyId") String recoveryCompanyId, @Param("leaseId") String leaseId);
    /**
     * 分页查询指定用户列表
     *
     * @param sysUserSearchVo 查询条件
     * @return
     */
    List<SysUserSearchListVo> findSysUserListByPage(@Param("sysUserSearchVo") SysUserSearchVo sysUserSearchVo, @Param("role") String role,@Param("leaseId") String leaseId);

    /**
     * 查询角色列表
     *
     * @param leaseId 委托公司id
     * @return
     */
    List<UserRoleListVo> selectUserList(@Param("leaseId") String leaseId);

    /**
     * 根据委托公司id及工单所在省份，获取审核人员信息
     *
     * @param leaseId 委托公司id
     * @param taskId 任务id
     * @return
     */
    List<SystemUser> selectLeaseAdminUserList(@Param("leaseId") String leaseId,@Param("taskId") String taskId);

    /**
     * 根据业务员用户名获取其手机号、姓名、所在收车公司
     *
     * @param userId 用户名
     * @return
     */
    ScanInfoVo selectInfoByUserId(@Param("userId") String userId);
}
