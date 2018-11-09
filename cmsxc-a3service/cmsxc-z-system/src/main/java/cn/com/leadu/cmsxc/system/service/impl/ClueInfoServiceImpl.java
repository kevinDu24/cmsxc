package cn.com.leadu.cmsxc.system.service.impl;
import cn.com.leadu.cmsxc.data.appuser.repository.ClueInforRepository;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import cn.com.leadu.cmsxc.pojo.system.vo.ClueListParamVo;
import cn.com.leadu.cmsxc.pojo.system.vo.ClueListVo;
import cn.com.leadu.cmsxc.system.service.ClueInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/5/25.
 *
 * 线索扫描列表
 */
@Component
public class ClueInfoServiceImpl implements ClueInfoService{
    @Autowired
    private ClueInforRepository clueInfoRepository;
    /**
     * 分页查询线索扫描列表
     *
     * @param clueListParamVo 查询条件
     * @param systemUser 用户信息
     * @return
     */
   public PageInfoExtend<ClueListVo> findClueInfoListByPage(ClueListParamVo clueListParamVo, SystemUser systemUser){
       PageInfoExtend<ClueListVo> clueListVo = clueInfoRepository.findClueInfoListByPage(clueListParamVo,systemUser.getUserRole(),systemUser.getLeaseId(),clueListParamVo.getPageQuery());
       return clueListVo;
   }
    /**
     * 查询所有线索扫描列表 --- 报表导出用
     *
     * @param clueListParamVo 查询条件
     * @param systemUser 用户信息
     * @return
     */
    public List<ClueListVo> findClueInfoList(ClueListParamVo clueListParamVo, SystemUser systemUser){
        List<ClueListVo> clueListVo = clueInfoRepository.findClueInfoList(clueListParamVo,systemUser.getUserRole(),systemUser.getLeaseId());
        return clueListVo;
    }
}
