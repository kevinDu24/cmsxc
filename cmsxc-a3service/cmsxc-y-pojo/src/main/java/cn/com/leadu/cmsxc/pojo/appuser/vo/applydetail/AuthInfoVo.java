package cn.com.leadu.cmsxc.pojo.appuser.vo.applydetail;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanzhenxia on 2018/1/18.
 *
 * 申请详情——授权信息Vo
 */
@Data
public class AuthInfoVo {

    private int type; // 操作类型

    private ApplyAuthVo applyAuth; //申请授权

    private CancelApplyVo cancelApply; //取消申请

    private RefuseAuthVo refuseAuth; //拒绝授权

    private ObtainAuthVo obtainAuth; //获得授权

    private AuthDelayVo authDelay; //授权延期

    private AuthExpiryVo authExpiry; //授权失效

    private FinishTaskVo finishTask; //工单完成

    private LeaseCancelTaskVo leaseCancelTask; //委托方取消工单

}
