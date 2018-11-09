package cn.com.leadu.cmsxc.appuser.validator.sysuser.vo;

import cn.com.leadu.cmsxc.common.entity.PageQuery;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * Created by yuanzhenxia on 2018/1/16.
 *
 * 提供线索用
 */
@Data
public class ClueInformationVo extends PageQuery {

    private String plate;// 车牌号
    private String appAddr;// 手机定位地址
    private String appLon;// 手机定位经度
    private String appLat;// 手机定位纬度
}
