package cn.com.leadu.cmsxc.system.service.impl;


import cn.com.leadu.cmsxc.common.util.ArrayUtil;
import cn.com.leadu.cmsxc.common.util.RandomUtil;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.appbusiness.repository.RecoveryCompanyRepository;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.RecoveryCompany;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryCompanyVo;
import cn.com.leadu.cmsxc.pojo.appbusiness.vo.RecoveryUserListVo;
import cn.com.leadu.cmsxc.system.service.RecoveryUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/2/1.
 *
 * 收车公司用户管理实现类
 */
@Service
public class RecoveryUserServiceImpl implements RecoveryUserService{
    @Autowired
    private RecoveryCompanyRepository recoveryCompanyRepository;
    /**
     * 收车公司用户注册
     *
     * @param  recoveryCompanyVo 收车公司用户注册信息
     * @return
     */
    @Transactional
    public ResponseEntity<RestResponse> register(RecoveryCompanyVo recoveryCompanyVo){
        //根据收车公司全称判断该收车公司是否存在
        Example exam = new Example(RecoveryCompany.class);
        Example.Criteria criter = exam.createCriteria();
        criter.orEqualTo("recoveryFullName", recoveryCompanyVo.getFullName());
        RecoveryCompany recoveryCompany = recoveryCompanyRepository.selectOneByExample(exam);
        if (recoveryCompany != null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该收车公司已存在"),
                    HttpStatus.OK);
        }
        // 如果收车公司不存在，将注册信息登录到收车公司表中
        RecoveryCompany newRecoveryCompany = new RecoveryCompany();
        // 收车公司简称
        newRecoveryCompany.setRecoveryShortName(recoveryCompanyVo.getShortName());
        // 收车公司全称
        newRecoveryCompany.setRecoveryFullName(recoveryCompanyVo.getFullName());
        // 联系人姓名
        newRecoveryCompany.setContactName(recoveryCompanyVo.getContactName());
        // 联系人电话
        newRecoveryCompany.setContactPhone(recoveryCompanyVo.getContactPhone());
        // 联系人地址
        newRecoveryCompany.setContactAddress(recoveryCompanyVo.getContactAddress());
        // 联系人邮箱
        newRecoveryCompany.setContactEmail(recoveryCompanyVo.getContactEmail());
        //设定注册码
        buildRegisterCode(newRecoveryCompany);
        newRecoveryCompany.setCreateTime(new Date());
        newRecoveryCompany.setUpdateTime(new Date());
        recoveryCompanyRepository.insertOne(newRecoveryCompany);
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"","注册成功"),
                HttpStatus.OK);
    }

    /**
     * 生成收车公司注册码
     * @param newRecoveryCompany
     */
    public void buildRegisterCode(RecoveryCompany newRecoveryCompany) {
        String managerRegisterCode = null;
        String salesmanRegisterCode = null;
        List<String> codes = new ArrayList();
        //取出所有收车公司注册码
        codes = recoveryCompanyRepository.selectRegisterCode();
        //循环给主管和业务员注册码赋值
        do
        {
            String code = RandomUtil.getRegisterCode(); //生成随机数
            if(!codes.contains(code)){
                if(managerRegisterCode == null){
                    managerRegisterCode = code;
                    codes.add(code);
                } else if(salesmanRegisterCode == null){
                    salesmanRegisterCode = code;
                    codes.add(code);
                }
            }
        }
        while (managerRegisterCode == null || salesmanRegisterCode == null);// 直到赋值成功，结束循环

        // 主管注册码
        newRecoveryCompany.setManagerRegisterCode(managerRegisterCode);
        // 业务员注册码
        newRecoveryCompany.setSalesmanRegisterCode(salesmanRegisterCode);
    }


    /**
     * 根据主管/业务员注册码获取收车公司信息，
     *
     * @param registerCode 注册码
     * @return
     */
    public RecoveryCompany findByRegisterCode(String registerCode){
        if(StringUtil.isNotNull(registerCode)) {
            Example example = new Example(RecoveryCompany.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("managerRegisterCode", registerCode);
            criteria.orEqualTo("salesmanRegisterCode", registerCode);
            return recoveryCompanyRepository.selectOneByExample(example);
        }
        return null;
    }

    /**
     * 根据收车公司简称或全称，分页获取收车公司信息
     *
     * @param recoveryUserListVo 画面信息
     * @return
     */
    public ResponseEntity<RestResponse> findRecoveryUserByPage(RecoveryUserListVo recoveryUserListVo){
        PageInfoExtend<RecoveryCompany> pageInfo = recoveryCompanyRepository.selectRecoveryCompany(recoveryUserListVo, recoveryUserListVo.getPageQuery());
        return new ResponseEntity<RestResponse>(
                RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,pageInfo,""),
                HttpStatus.OK);

    }

}
