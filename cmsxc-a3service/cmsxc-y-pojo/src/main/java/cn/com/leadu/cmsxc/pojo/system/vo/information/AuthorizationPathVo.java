package cn.com.leadu.cmsxc.pojo.system.vo.information;

import lombok.Data;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/2/5.
 *
 * 获取申请附件用vo
 */
@Data
public class AuthorizationPathVo {
    private String vedioPath;//视频路径
    private List<String> photoPathList;// 照片路径集合

}
