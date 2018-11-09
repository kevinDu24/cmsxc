package cn.com.leadu.cmsxc.extend.service;

import cn.com.leadu.cmsxc.extend.vo.AppRequestLogTmpVo;

/**
 * @author qiaomengnan
 * @ClassName: LogService
 * @Description: 日志记录service
 * @date 2018/1/30
 */
public interface LogService {

    void saveLog(AppRequestLogTmpVo appRequestLogTmpVo);

}
