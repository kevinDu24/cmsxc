package cn.com.leadu.cmsxc.data.apprequestlog.repository.impl;

import cn.com.leadu.cmsxc.data.apprequestlog.dao.AppRequestLogDao;
import cn.com.leadu.cmsxc.data.apprequestlog.repository.AppRequestLogRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.apprequestlog.entity.AppRequestLog;
import org.springframework.stereotype.Component;

/**
 * @author qiaomengnan
 * @ClassName: AppRequestLogRepositoryImpl
 * @Description: app访问日志Repository
 * @date 2018/1/30
 */
@Component
public class AppRequestLogRepositoryImpl extends AbstractBaseRepository<AppRequestLogDao,AppRequestLog> implements AppRequestLogRepository {

    /**
     * @Title:
     * @Description:   录入app访问日志
     * @param appRequestLog
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/30 03:12:44
     */
    public void insertOne(AppRequestLog appRequestLog){
        super.insert(appRequestLog);
    }

}
