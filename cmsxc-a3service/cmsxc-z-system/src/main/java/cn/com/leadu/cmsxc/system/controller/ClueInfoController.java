package cn.com.leadu.cmsxc.system.controller;

import cn.com.leadu.cmsxc.extend.annotation.AuthUserInfo;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import cn.com.leadu.cmsxc.pojo.system.vo.ClueListParamVo;
import cn.com.leadu.cmsxc.pojo.system.vo.ClueListVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SystemUserVo;
import cn.com.leadu.cmsxc.system.service.ClueInfoService;
import cn.com.leadu.cmsxc.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/5/25.
 *
 * 线索扫描列表
 */
@RestController
@RequestMapping("clueinfo")
public class ClueInfoController {
    @Autowired
    private ClueInfoService clueInfoService;

    /**
     *  线索扫描列表
     *
     * @param clueListParamVo 画面参数
     * @param sysUser 用户信息
     * @return
     */
    @RequestMapping(value = "findClueInfoListByPage" ,method = RequestMethod.GET)
    public ResponseEntity<RestResponse> findClueInfoListByPage(ClueListParamVo clueListParamVo, @AuthUserInfo SystemUser sysUser){
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genSuccessResponse(clueInfoService.findClueInfoListByPage(clueListParamVo, sysUser)),
                HttpStatus.OK);
    }
    /**
     *  线索扫描列表 --- 报表导出用
     *
     * @param clueListParamVo 画面参数
     * @param sysUser 用户信息
     * @return
     */
    @RequestMapping(value = "findClueInfoList" ,method = RequestMethod.GET)
    public List<ClueListVo> findClueInfoList(ClueListParamVo clueListParamVo, @AuthUserInfo SystemUser sysUser){
        return clueInfoService.findClueInfoList(clueListParamVo, sysUser);
    }
}
