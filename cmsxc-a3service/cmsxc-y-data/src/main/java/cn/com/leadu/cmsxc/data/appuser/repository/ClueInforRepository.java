package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.pojo.appuser.entity.ClueInfo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.ClueVo;
import cn.com.leadu.cmsxc.pojo.system.vo.ClueListParamVo;
import cn.com.leadu.cmsxc.pojo.system.vo.ClueListVo;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 我的线索信息
 */
public interface ClueInforRepository {
    /**
     * 根据条件批量获取数据
     *
     * @param example
     * @return
     */
    List<ClueInfo> selectByExampleList(Example example);
    /**
     * @Title:
     * @Description: 分页查询
     * @param example,pageQuery
     * @return
     * @throws
     * @author zcHu
     * @date 2018/01/29
     */
    PageInfoExtend selectListByExamplePageInfo(Example example, PageQuery pageQuery);
    /**
     * 登录我的线索信息
     *
     * @param clueInfo
     */
    void insertOne(ClueInfo clueInfo);
    /**
     * 批量登录我的线索信息
     *
     * @param clueInfos
     */
    void insertMore(List<ClueInfo> clueInfos);

    /**
     * 根据用户id查询命中线索
     *
     * @param userId 用户id
     * @param targetFlag 是否命中flag（0：未命中，1：已命中）
     * @param page 当前页数
     * @param size 每页个数
     * @return
     */
    List<ClueVo> selectByUserIdOnTarget(String userId, String targetFlag,  int page, int size);

    /**
     * 根据用户id查询非命中线索
     *
     * @param userId 用户id
     * @param targetFlag 是否命中flag（0：未命中，1：已命中）
     * @param page 当前页数
     * @param size 每页个数
     * @return
     */
    List<ClueVo> selectByUserIdOutOfTarget(String userId, String targetFlag,  int page, int size);

    /**
     * 根据用户id查找已命中未查看的线索数量（同一车牌号统计一条）
     *
     * @param userId 用户id
     * @param targetFlag 命中flag
     * @param checkFlag 查看flag
     * @return
     */
    int selectCountOfNotCheck(String userId, String targetFlag,String checkFlag );

    /**
     * 根据主键更新表
     * @param clueInfo
     */
    void updateByPrimaryKey(ClueInfo clueInfo);

    /**
     * 根据车牌号和命中flag查找用户id
     *
     * @param targetFlag 命中flag
     * @param plate 车牌号
     * @return
     */
    List<String> selectByPlateAndTargetFlag(String plate, String targetFlag);
    /**
     * 分页查询线索扫描列表
     *
     * @param clueListParamVo 查询条件
     * @param role 用户角色
     * @param leaseId 委托公司id
     * @return
     */
    PageInfoExtend<ClueListVo> findClueInfoListByPage(ClueListParamVo clueListParamVo, String role, String leaseId , PageQuery pageQuery);
    /**
     * 查询所有线索扫描列表 --- 报表导出用
     *
     * @param clueListParamVo 查询条件
     * @param role 用户角色
     * @param leaseId 委托公司id
     * @return
     */
     List<ClueListVo> findClueInfoList(ClueListParamVo clueListParamVo, String role, String leaseId);
}
