package cn.com.leadu.cmsxc.extend.config;

import cn.com.leadu.cmsxc.extend.util.UserInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author qiaomengnan
 * @ClassName: UserInfoCommandLineRunner
 * @Description: 启动时注入对当前用户操作的类
 * @date 2018/1/7
 */

@Component
public class UserInfoCommandLineRunner implements CommandLineRunner {

    @Autowired
    private UserInfoUtil userInfoUtil;

    @Override
    public void run(String... strings) throws Exception {
        UserInfoUtil.userInfoUtil = userInfoUtil;
    }
}
