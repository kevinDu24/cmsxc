package cn.com.leadu.cmsxc.webclient.system.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 导出excel service
 * Created by qiaohao on 2017/3/7.
 */
@Service
public interface ExcelService {

    public void exportList(String title, List dataset, OutputStream out)throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException;
    public void exportList(String title, List dataset, Class clazz, OutputStream out) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException;
}
