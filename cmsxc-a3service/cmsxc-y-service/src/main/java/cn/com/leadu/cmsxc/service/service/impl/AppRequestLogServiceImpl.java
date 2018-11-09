package cn.com.leadu.cmsxc.service.service.impl;

import cn.com.leadu.cmsxc.data.apprequestlog.repository.AppRequestLogRepository;
import cn.com.leadu.cmsxc.extend.service.LogService;
import cn.com.leadu.cmsxc.extend.util.UserInfoUtil;
import cn.com.leadu.cmsxc.extend.vo.AppRequestLogTmpVo;
import cn.com.leadu.cmsxc.pojo.apprequestlog.entity.AppRequestLog;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qiaomengnan
 * @ClassName: LogServiceImpl
 * @Description: 日志service实现类
 * @date 2018/1/30
 */
@Service
public class AppRequestLogServiceImpl implements LogService {

    @Autowired
    private AppRequestLogRepository appRequestLogRepository;

    /**
     * @Title:
     * @Description: 保存日志请求记录
     * @param appRequestLogTmpVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/30 01:29:08
     */
    @Override
    public void saveLog(AppRequestLogTmpVo appRequestLogTmpVo) {
        AppRequestLog appRequestLog = new AppRequestLog();
        BeanUtils.copyProperties(appRequestLogTmpVo, appRequestLog);
        Object user = UserInfoUtil.userInfoUtil.getUser(SystemUser.class);
        if(user != null) {
            SystemUser systemUser = (SystemUser) user;
            appRequestLog.setUserPhone(systemUser.getUserPhone());
            appRequestLog.setCreator(systemUser.getUserId());
            appRequestLog.setUpdater(systemUser.getUserId());
        }
        appRequestLogRepository.insertOne(appRequestLog);
    }

}
