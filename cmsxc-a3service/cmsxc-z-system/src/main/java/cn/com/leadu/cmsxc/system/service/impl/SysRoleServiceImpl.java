package cn.com.leadu.cmsxc.system.service.impl;

import cn.com.leadu.cmsxc.common.util.ArrayUtil;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.data.system.repository.SysRoleRepository;
import cn.com.leadu.cmsxc.data.system.repository.SysRoleResourceRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.system.entity.SysRole;
import cn.com.leadu.cmsxc.pojo.system.entity.SysRoleResource;
import cn.com.leadu.cmsxc.pojo.system.vo.SysResourceVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SysRoleDeleteVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SysRoleVo;
import cn.com.leadu.cmsxc.system.service.SysRoleService;
import cn.com.leadu.cmsxc.system.validator.sysrole.vo.SysRoleModifyVo;
import cn.com.leadu.cmsxc.system.validator.sysrole.vo.SysRoleSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qiaomengnan
 * @ClassName: SysRoleServiceImpl
 * @Description: 角色业务层
 * @date 2018/1/12
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Autowired
    private SysRoleResourceRepository sysRoleResourceRepository;


    /**
     * @Title:
     * @Description: 保存角色
     * @param sysRoleSaveVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 10:17:25
     */
    @Transactional
    public ResponseEntity<RestResponse> saveSysRole(SysRoleSaveVo sysRoleSaveVo){
        if(sysRoleRepository.selectByPrimaryKey(sysRoleSaveVo.getId()) != null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","角色ID已存在"),
                    HttpStatus.OK);
        }
        String id = sysRoleRepository.insertOne(sysRoleSaveVo.getSyRole());
        if(ArrayUtil.isNotNullAndLengthNotZero(sysRoleSaveVo.getResources())) {
            List<SysRoleResource> sysRoleResources = new ArrayList<>();
            for(SysResourceVo sysResourceVo : sysRoleSaveVo.getResources()){
                SysRoleResource sysRoleResource = new SysRoleResource();
                sysRoleResource.setResourceId(sysResourceVo.getId());
                sysRoleResource.setRoleId(id);
                sysRoleResources.add(sysRoleResource);
            }
            sysRoleResourceRepository.insertListByMapper(sysRoleResources);
        }
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","新增角色成功"),
                HttpStatus.OK);
    }

    /**
     * @Title:
     * @Description: 根据角色名分页查询角色,按照新增时间分页
     * @param sysRoleVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 04:31:12
     */
    public PageInfoExtend<SysRole> findSysRoleByPage(SysRoleVo sysRoleVo){
        Example example = new Example(SysRole.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtil.isNotNull(sysRoleVo.getRoleName()))
            criteria.andEqualTo("roleName",sysRoleVo.getRoleName());
        example.setOrderByClause(" create_time desc ");
        return sysRoleRepository.selectListByExamplePageInfo(example,sysRoleVo.getPageQuery());
    }


    /**
     * @Title:
     * @Description: 修改角色
     * @param sysRoleModifyVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:50:35
     */
    @Transactional
    public void modifySysRole(SysRoleModifyVo sysRoleModifyVo){
        if(ArrayUtil.isNotNullAndLengthNotZero(sysRoleModifyVo.getResources())) {
            sysRoleResourceRepository.deleteByRoleId(sysRoleModifyVo.getId());
            List<SysRoleResource> sysRoleResources = new ArrayList<>();
            for(SysResourceVo sysResourceVo : sysRoleModifyVo.getResources()){
                SysRoleResource sysRoleResource = new SysRoleResource();
                sysRoleResource.setResourceId(sysResourceVo.getId());
                sysRoleResource.setRoleId(sysRoleModifyVo.getId());
                sysRoleResources.add(sysRoleResource);
            }
            sysRoleResourceRepository.insertListByMapper(sysRoleResources);
        }
    }

    /**
     * @Title:
     * @Description:  通过id删除角色
     * @param sysRoleDeleteVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 05:55:21
     */
    @Transactional
    public void deleteSysRole(SysRoleDeleteVo sysRoleDeleteVo){
        if(sysRoleDeleteVo.getRowsIds() != null && !sysRoleDeleteVo.getRowsIds().isEmpty()){
            for(String id : sysRoleDeleteVo.getRowsIds()){
                sysRoleRepository.delete(id);
                sysRoleResourceRepository.deleteByRoleId(id);
            }
        }
    }

    /**
     * @Title:
     * @Description:  根据id获取角色
     * @param id
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/11 06:32:58
     */
    public SysRole findSysRoleById(String id){
        return sysRoleRepository.selectByPrimaryKey(id);
    }

}
