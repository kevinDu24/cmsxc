package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.data.appuser.dao.ParkingInfoDao;
import cn.com.leadu.cmsxc.data.appuser.repository.ParkingInfoRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.ParkingInfo;
import cn.com.leadu.cmsxc.pojo.appuser.vo.ParkingInfoSubVo;
import cn.com.leadu.cmsxc.pojo.assistant.vo.*;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 停车场信息RepositoryImpl
 */
@Component
public class ParkingInfoRepositoryImpl extends AbstractBaseRepository<ParkingInfoDao,ParkingInfo> implements ParkingInfoRepository {
    /**
     * 通过主键获取工单
     * @param id
     * @return
     */
    public ParkingInfo selectByPrimaryKey(String id){
        return super.selectByPrimaryKey(id);
    }

    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    @Override
    public List<ParkingInfo> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }

    /**
     * 根据条件获取唯一数据
     * @param example
     * @return
     */
    @Override
    public ParkingInfo selectOneByExample(Example example){
        return super.selectOneByExample(example);
    }

    /**
     * 登录表
     * @param parkingInfo
     */
    public void insertOne(ParkingInfo parkingInfo){
        super.insert(parkingInfo);
    }

    /**
     * 根据主键更新表
     * @param parkingInfo
     */
    @Override
    public void updateByPrimaryKey(ParkingInfo parkingInfo) {
        super.updateByPrimaryKey(parkingInfo);
    }

    /**
     * 批量插入数据
     * @param parkingInfos
     */
    public void insertMore(List<ParkingInfo> parkingInfos){
        super.insertListByMapper(parkingInfos);
    }

    /**
     * 获取停车场公司列表
     * @return
     */
    public List<ParkingCompanyListVo> selectCompanyList(String leaseId){
        return baseDao.selectCompanyList(leaseId);
    }

    /**
     * 获取停车场公司列表
     * @return
     */
    public List<ParkingListVo> selectParkingList(String parkingAdminId){
        return baseDao.selectParkingList(parkingAdminId);
    }

    /**
     * 获取停车场信息
     * @return
     */
    public EditParkingInfoVo selectParkingInfo(String id){
        return baseDao.selectParkingInfo(id);
    }

    /**
     * 筛选停车场
     * @return
     */
    public List<ParkingListVo> selectParkingResult(String leaseId, String province,String state){
        return baseDao.selectParkingResult(leaseId,province,state);
    }

    /**
     * 根据委托公司id获取停车场
     * @return
     */
    public List<ParkingDistanceVo> selectParkingbyLeaseId(String leaseId){
        return baseDao.selectParkingByLeaseId(leaseId);
    }

    /**
     * 根据停车场id获取停车场相关信息
     * @return
     */
    public ParkingInfoSubVo selectParkingById(String leaseId){
        return baseDao.selectParkingById(leaseId);
    }


}
