package cn.com.leadu.cmsxc.external.util.constant;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 邮件身份验证器
 * Created by huzongcheng on 2018/3/13.
 */
public class MailAuthenticator extends Authenticator {
    /** 用户账号 */
    private String userName = null;
    /** 用户口令 */
    private String password = null;

    /**
     * @param userName
     * @param password
     */
    public MailAuthenticator(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    /**
     * 身份验证
     * @return
     */
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }

}
