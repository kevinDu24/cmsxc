package cn.com.leadu.cmsxc.data.appuser.dao;

import cn.com.leadu.cmsxc.data.base.config.BaseDao;
import cn.com.leadu.cmsxc.pojo.appuser.entity.ClueInfo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.ClueVo;
import cn.com.leadu.cmsxc.pojo.system.vo.ClueListParamVo;
import cn.com.leadu.cmsxc.pojo.system.vo.ClueListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 我的线索信息
 */
public interface ClueInfoDao extends BaseDao<ClueInfo> {
    /**
     * 根据用户id查询命中线索
     *
     * @param userId 用户id
     * @param targetFlag 是否命中flag
     * @param page 当前页
     * @param size 每页数目
     * @return
     */
    List<ClueVo> selectByUserIdOnTarget(@Param("userId") String userId, @Param("targetFlag") String targetFlag,
                                             @Param("page") int page, @Param("size") int size);

    /**
     * 根据用户id查询非命中线索
     *
     * @param userId 用户id
     * @param targetFlag 是否命中flag
     * @param page 当前页
     * @param size 每页数目
     * @return
     */
    List<ClueVo> selectByUserIdOutOfTarget(@Param("userId") String userId, @Param("targetFlag") String targetFlag,
                                        @Param("page") int page, @Param("size") int size);

    /**
     * 根据用户id查找已命中未查看的线索数量（同一车牌号统计一条）
     *
     * @param userId 用户id
     * @param targetFlag 命中flag
     * @param checkFlag 查看flag
     * @return
     */
    int selectCountOfNotCheck(@Param("userId") String userId, @Param("targetFlag") String targetFlag,@Param("checkFlag") String checkFlag);

    /**
     * 根据车牌号和命中flag查找用户id
     *
     * @param targetFlag 命中flag
     * @param plate 车牌号
     * @return
     */
    List<String> selectByPlateAndTargetFlag( @Param("plate") String plate,@Param("targetFlag") String targetFlag);
    /**
     * 分页查询线索扫描列表
     *
     * @param clueListParamVo 查询条件
     * @param role 用户角色
     * @param leaseId 委托公司id
     * @return
     */
    List<ClueListVo> findClueInfoListByPage(@Param("clueListParamVo") ClueListParamVo clueListParamVo, @Param("role") String role, @Param("leaseId") String leaseId);

}
