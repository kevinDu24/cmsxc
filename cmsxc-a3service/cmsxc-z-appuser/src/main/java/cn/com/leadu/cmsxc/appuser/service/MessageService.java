package cn.com.leadu.cmsxc.appuser.service;

import cn.com.leadu.cmsxc.extend.response.RestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by yuanzhenxia on 2018/1/15.
 *
 * 短信获取验证码
 */
@Service
public interface MessageService {
    /**
     * 获取验证码
     *
     * @param phoneNum 手机号码
     * @param projectName 工程名称
     * @param serviceName 服务名称
     * @param classFunctionName 方法路径
     * @return
     */
    ResponseEntity<RestResponse> sendSingleMt(String phoneNum, String projectName, String serviceName, String classFunctionName);

    /**
     * 发送短信
     *
     * @param phoneNum 手机号码
     * @param plate 车牌号
     * @param projectName 工程名称
     * @param serviceName 服务名称
     * @param classFunctionName 方法路径
     * @return
     */
    String sendMessage(String phoneNum, String plate,String projectName,String serviceName,String classFunctionName)throws Exception;

    /**
     * 根据手机号和短信内容发送短信
     *
     * @param phoneNum 手机号码
     * @param messageContent 短信内容
     * @param projectName 工程名称
     * @param serviceName 服务名称
     * @param classFunctionName 方法路径
     * @return
     */
    String sendContentMessage(String phoneNum, String messageContent,String projectName,String serviceName,String classFunctionName)throws Exception;
    /**
     * 找回密码时获取验证码，验证码3分钟有效
     *
     * @param phoneNum 手机号码
     * @param projectName 工程名称
     * @param serviceName 服务名称
     * @param classFunctionName 方法路径
     * @return
     */
    ResponseEntity<RestResponse> sendMt(String phoneNum, String projectName, String serviceName, String classFunctionName);

}
