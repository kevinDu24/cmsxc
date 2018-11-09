package cn.com.leadu.cmsxc.external.service;

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
     * 发送短信
     *
     * @param userId 手机号码
     * @param content 短信内容
     * @param projectName 工程名称
     * @param serviceName 服务名称
     * @param classFunctionName 方法路径
     * @return
     */
    String sendMessage(String userId,String content,String projectName,String serviceName,String classFunctionName) throws Exception;

}
