package cn.com.leadu.cmsxc.system.service;

/**
 * Created by wangxue on 2018/2/7.
 * 定时分析申请任务是否失效处理的service接口
 */
public interface AuthorizationSchedulerService {

    /**
     * 分析申请任务是否失效处理
     * @return
     * */
    String applyOutTimeHandler();
    /**
     * 授权2天后，没有完成收车的，授权失效
     * @return
     * */
    String authorizationOutTimeHandler();
}
