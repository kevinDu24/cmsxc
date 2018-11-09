package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appuser.entity.VehiclePhotoPath;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/19.
 *
 * 附件关联表（照片路径保存）
 */
public interface VehiclePhotoPathRepository {
    /**
     * 登录附件关联表
     *
     * @param vehiclePhotoPath
     */
    void insertOne(VehiclePhotoPath vehiclePhotoPath);
    /**
     * 批量登录附件关联表
     *
     * @param vehiclePhotoPath
     */
    void insertMore(List<VehiclePhotoPath> vehiclePhotoPath);

    /**
     * 删除附件关联表信息
     *
     * @param id 主键
     */
    void delete(Object id);

    /**
     * 根据图片UUID删除
     *
     * @param photoUuid
     */
    void deleteByVehiclePhotoUUID(String photoUuid);

    /**
     * 根据图片UUID取得全部图片
     *
     * @param photoUuid
     */
    List<VehiclePhotoPath> selectByVehiclePhotoUUID(String photoUuid);
}
