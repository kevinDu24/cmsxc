package cn.com.leadu.cmsxc.system.service;

import org.springframework.stereotype.Service;

/**
 * Created by yuanzhenxia on 2018/1/15.
 *
 * 短信获取验证码
 */
@Service
public interface MessageService {

    /**
     * 发送短信
     *
     * @param phoneNum 手机号码
     * @param insiderPhone 内勤人员手机号
     * @param plate 车牌号
     * @param outTimeDate 授权失效时间
     * @param projectName 工程名称
     * @param serviceName 服务名称
     * @param classFunctionName 方法路径
     * @return
     */
    String sendMessage(String phoneNum, String insiderPhone, String plate,String outTimeDate, String projectName, String serviceName, String classFunctionName)throws Exception;

}
