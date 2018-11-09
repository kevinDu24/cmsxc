package cn.com.leadu.cmsxc.system.service.impl;

import cn.com.leadu.cmsxc.common.util.ArrayUtil;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.data.system.repository.SysResourceRepository;
import cn.com.leadu.cmsxc.data.system.repository.SysRoleRepository;
import cn.com.leadu.cmsxc.pojo.system.entity.SysResource;
import cn.com.leadu.cmsxc.pojo.system.entity.SysUser;
import cn.com.leadu.cmsxc.pojo.system.vo.SysResourceVo;
import cn.com.leadu.cmsxc.system.service.SysResourceService;
import cn.com.leadu.cmsxc.system.util.constant.enums.SysResourceEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qiaomengnan
 * @ClassName: SysResourceServiceImpl
 * @Description:
 * @date 2018/1/14
 */
@Service
public class SysResourceServiceImpl implements SysResourceService {

    @Autowired
    private SysResourceRepository sysResourceRepository;

    @Autowired
    private SysRoleRepository sysRoleRepository;

    /**
     * @Title:
     * @Description: 根据用户id返回拥有的菜单
     * @param userId
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 12:48:19
     */
    public List<SysResource> findSysResByUser(String userId){
        List<SysResource> sysResources = null;
        if(StringUtil.isNotNull(userId)) {
            List<String> roleIds = sysRoleRepository.selectSysRoleIdsBySysUserId(userId);
            if (ArrayUtil.isNotNullAndLengthNotZero(roleIds))
                sysResources = sysResourceRepository.selectSysResMenuBySysRoleId(roleIds, SysResourceEnum.MENU.getType());
        }
        return sysResources;
    }

    /**
     * @Title:
     * @Description:  查询所有菜单列表, 根据sort资源排序
     * @param
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/14 03:58:27
     */
    public List<SysResourceVo> findSysResAll(){
        Example example = new Example(SysResource.class);
        example.setOrderByClause(" sort asc ");
        List<SysResource> sysResources = sysResourceRepository.selectListByExample(example);
        List<SysResourceVo> sysResourceVos = new ArrayList<>();
        for(SysResource sysResource : sysResources){
            if(StringUtil.isNull(sysResource.getParentId())){
                SysResourceVo tmpVo = new SysResourceVo();
                BeanUtils.copyProperties(sysResource,tmpVo);
                sysResourceVos.add(tmpVo);
            }
        }
        for(SysResourceVo sysResourceVo : sysResourceVos){
            for(SysResource sysResource : sysResources){
                if(StringUtil.isNotNull(sysResource.getParentId()) && sysResource.getParentId().equals(sysResourceVo.getId())){
                    SysResourceVo tmpVo = new SysResourceVo();
                    BeanUtils.copyProperties(sysResource,tmpVo);
                    sysResourceVo.getChildren().add(tmpVo);
                }
            }
        }
        return sysResourceVos;
    }

    /**
     * @Title:
     * @Description: 分页查询菜单 并按录入时间倒序
     * @param sysResourceVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 04:31:12
     */
    public PageInfoExtend<SysResource> findSysUserByPage(SysResourceVo sysResourceVo){
        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtil.isNotNull(sysResourceVo.getDescription()))
            criteria.andEqualTo("description",sysResourceVo.getDescription());
        example.setOrderByClause(" create_time desc ");
        PageInfoExtend<SysResource> pageInfo = sysResourceRepository.selectListByExamplePageInfo(example,sysResourceVo.getPageQuery());
        return pageInfo;
    }

    /**
     * @Title:
     * @Description: 根据角色id获取角色拥有的资源
     * @param sysRoleId
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 05:15:08
     */
    public List<SysResource> findSysResBySysRoleId(String sysRoleId){
        List<String> roleIds = new ArrayList();
        roleIds.add(sysRoleId);
        List<SysResource> sysResources = sysResourceRepository.selectSysResMenuBySysRoleId(roleIds, SysResourceEnum.MENU.getType());
        return sysResources;
    }

}
