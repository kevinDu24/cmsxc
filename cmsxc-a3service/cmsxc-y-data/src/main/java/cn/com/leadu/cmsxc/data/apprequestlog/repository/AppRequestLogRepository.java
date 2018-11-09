package cn.com.leadu.cmsxc.data.apprequestlog.repository;

import cn.com.leadu.cmsxc.pojo.apprequestlog.entity.AppRequestLog;

/**
 * @author qiaomengnan
 * @ClassName: AppRequestLogRepository
 * @Description:
 * @date 2018/1/30
 */
public interface AppRequestLogRepository {

    /**
     * @Title:
     * @Description:   录入app访问日志
     * @param appRequestLog
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/30 03:12:44
     */
    void insertOne(AppRequestLog appRequestLog);

}
