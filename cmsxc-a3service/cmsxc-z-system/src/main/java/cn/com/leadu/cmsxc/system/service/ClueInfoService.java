package cn.com.leadu.cmsxc.system.service;

import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import cn.com.leadu.cmsxc.pojo.system.vo.ClueListParamVo;
import cn.com.leadu.cmsxc.pojo.system.vo.ClueListVo;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/5/25.
 *
 * 线索扫描列表
 */
public interface ClueInfoService {
    /**
     * 分页查询线索扫描列表
     *
     * @param clueListParamVo 查询条件
     * @param systemUser 用户信息
     * @return
     */
    PageInfoExtend<ClueListVo> findClueInfoListByPage(ClueListParamVo clueListParamVo, SystemUser systemUser);
    /**
     * 查询所有线索扫描列表 --- 报表导出用
     *
     * @param clueListParamVo 查询条件
     * @param systemUser 用户信息
     * @return
     */
    List<ClueListVo> findClueInfoList(ClueListParamVo clueListParamVo, SystemUser systemUser);
}
