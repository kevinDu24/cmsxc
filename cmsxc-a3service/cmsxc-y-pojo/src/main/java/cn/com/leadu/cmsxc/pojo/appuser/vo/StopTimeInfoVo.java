package cn.com.leadu.cmsxc.pojo.appuser.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by qiaohao on 2017/4/24.
 */
@Data
public class StopTimeInfoVo implements Serializable {

    private Date stopStartTime; //停车开始时间

    private Date stopEndTime; //停车结束时间

}
