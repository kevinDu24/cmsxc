package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.appuser.dao.VehiclePhotoPathDao;
import cn.com.leadu.cmsxc.data.appuser.repository.VehiclePhotoPathRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.VehiclePhotoPath;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 附件关联表（照片路径保存）
 */
@Component
public class VehiclePhotoPathRepositoryImpl extends AbstractBaseRepository<VehiclePhotoPathDao,VehiclePhotoPath> implements VehiclePhotoPathRepository {
    /**
     * 登录附件关联表
     *
     * @param vehiclePhotoPath
     */
    @Override
    public void insertOne(VehiclePhotoPath vehiclePhotoPath){
        super.insert(vehiclePhotoPath);
    }
    /**
     * 批量登录附件关联表
     *
     * @param vehiclePhotoPaths
     */
    @Override
    public void insertMore(List<VehiclePhotoPath> vehiclePhotoPaths){
        super.insertListByMapper(vehiclePhotoPaths);
    }

    /**
     * 删除信息
     *
     * @param id 主键
     */
    public void delete(Object id){
        super.delete(id);
    }


    /**
     * 根据图片UUID删除
     *
     * @param photoUuid
     */
   public void deleteByVehiclePhotoUUID(String photoUuid){
        if(StringUtil.isNotNull(photoUuid)) {
            Example example = new Example(VehiclePhotoPath.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("photoUuid", photoUuid);
            baseDao.deleteByExample(example);
        }
    }

    /**
     * 根据图片UUID取得全部图片
     *
     * @param photoUuid
     */
    @Override
    public List<VehiclePhotoPath> selectByVehiclePhotoUUID(String photoUuid) {
        if(StringUtil.isNotNull(photoUuid)) {
            Example example = new Example(VehiclePhotoPath.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("photoUuid", photoUuid);
            return super.selectListByExample(example);
        }
        return new ArrayList<>();
    }
}
