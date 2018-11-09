package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import cn.com.leadu.cmsxc.data.appuser.dao.ClueInfoDao;
import cn.com.leadu.cmsxc.data.appuser.repository.ClueInforRepository;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.ClueInfo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.ClueVo;
import cn.com.leadu.cmsxc.pojo.system.vo.ClueListParamVo;
import cn.com.leadu.cmsxc.pojo.system.vo.ClueListVo;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 我的线索信息
 */
@Component
public class ClueInforRepositoryImpl extends AbstractBaseRepository<ClueInfoDao,ClueInfo> implements ClueInforRepository {
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    @Override
    public List<ClueInfo> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }
    /**
     * @Title:
     * @Description: 分页查询
     * @param example,pageQuery
     * @return
     * @throws
     * @author zcHu
     * @date 2018/01/29
     */
    public PageInfoExtend<ClueInfo> selectListByExamplePageInfo(Example example, PageQuery pageQuery){
        return super.selectListByExamplePageInfo(example,pageQuery);
    }
    /**
     * 登录我的线索信息
     * @param clueInfo
     */
    public void insertOne(ClueInfo clueInfo){
        super.insert(clueInfo);
    }
    /**
     * 批量登录我的线索信息
     * @param clueInfos
     */
    public void insertMore(List<ClueInfo> clueInfos){
        super.insertListByMapper(clueInfos);
    }
    /**
     * 根据用户id查询命中线索
     *
     * @param userId 用户id
     * @param targetFlag 是否命中flag（0：未命中，1：已命中）
     * @param page 当前页数
     * @param size 每页个数
     * @return
     */
    public List<ClueVo> selectByUserIdOnTarget(String userId, String targetFlag,  int page, int size){
       return baseDao.selectByUserIdOnTarget(userId, targetFlag, page, size);
    }
    /**
     * 根据用户id查询非命中线索
     *
     * @param userId 用户id
     * @param targetFlag 是否命中flag（0：未命中，1：已命中）
     * @param page 当前页数
     * @param size 每页个数
     * @return
     */
    public List<ClueVo> selectByUserIdOutOfTarget(String userId, String targetFlag,  int page, int size){
        return baseDao.selectByUserIdOutOfTarget(userId, targetFlag, page, size);
    }
    /**
     * 根据用户id查找已命中未查看的线索数量（同一车牌号统计一条）
     *
     * @param userId 用户id
     * @param targetFlag 命中flag
     * @param checkFlag 查看flag
     * @return
     */
    public int selectCountOfNotCheck(String userId, String targetFlag,String checkFlag ){
        return baseDao.selectCountOfNotCheck(userId,targetFlag,checkFlag );
    }
    /**
     * 根据主键更新表
     * @param clueInfo
     */
    @Override
    public void updateByPrimaryKey(ClueInfo clueInfo) {
        super.updateByPrimaryKey(clueInfo);
    }

    /**
     * 根据车牌号和命中flag查找用户id
     *
     * @param targetFlag 命中flag
     * @param plate 车牌号
     * @return
     */
    public List<String> selectByPlateAndTargetFlag(String plate, String targetFlag){
        return baseDao.selectByPlateAndTargetFlag(plate, targetFlag);
    }
    /**
     * 分页查询线索扫描列表
     *
     * @param clueListParamVo 查询条件
     * @param role 用户角色
     * @param leaseId 委托公司id
     * @return
             */
    @Override
    public PageInfoExtend<ClueListVo> findClueInfoListByPage(ClueListParamVo clueListParamVo, String role, String leaseId , PageQuery pageQuery){
        PageInfo<ClueListVo> pageInfo = PageHelper.startPage(pageQuery.getCurrenPage(),pageQuery.getPageSize())
                .doSelectPageInfo(new ISelect() {
                    @Override
                    public void doSelect() {
                        baseDao.findClueInfoListByPage(clueListParamVo,role,leaseId);
                    }
                });
        PageInfoExtend<ClueListVo> pageInfoExtend = new PageInfoExtend<>();
        pageInfoExtend.setDraw(pageQuery.getDraw());
        pageInfoExtend.setData(pageInfo.getList());
        pageInfoExtend.setRecordsTotal(pageInfo.getTotal());
        pageInfoExtend.setRecordsFiltered(pageInfo.getTotal());
        return pageInfoExtend;
    }
    /**
     * 查询所有线索扫描列表 --- 报表导出用
     *
     * @param clueListParamVo 查询条件
     * @param role 用户角色
     * @param leaseId 委托公司id
     * @return
     */
    @Override
    public List<ClueListVo> findClueInfoList(ClueListParamVo clueListParamVo, String role, String leaseId){
        return baseDao.findClueInfoListByPage(clueListParamVo,role,leaseId);
    }
}
