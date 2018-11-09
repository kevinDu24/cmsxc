package cn.com.leadu.cmsxc.external.util.constant;

import cn.com.leadu.cmsxc.common.constant.enums.MailTypeEnum;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.external.config.MailProperties;
import cn.com.leadu.cmsxc.pojo.appuser.entity.AuthMailRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
/**
 * Created by LEO on 16/10/13.
 */
@Component
public class MailUtils {

    @Autowired
    private MailProperties mailProperties;

    public void sendAuthMail(AuthMailRecord authMailRecord, String sendFlag) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String uploadDate = sdf.format(new Date());
        // 邮件主题
        String subject;
        if(MailTypeEnum.CANCEL.getCode().equals(sendFlag)){
            subject = "撤销委托说明_"
                    + authMailRecord.getName() + "_" + authMailRecord.getPlate() + "_" + uploadDate;
        } else if(MailTypeEnum.PUSH.getCode().equals(sendFlag)){
            subject = "您有新的派单任务【" + authMailRecord.getPlate() + "】,请及时处理_" + uploadDate;
        } else {
            return;
        }
        //1. 用于存放 SMTP 服务器地址等参数
        Properties properties = new Properties();
        // 主机地址
        properties.setProperty("mail.smtp.host", mailProperties.getHost());
        // 邮件协议
        properties.setProperty("mail.transport.protocol", mailProperties.getProtocol());
        // 认证
        properties.setProperty("mail.smtp.auth", mailProperties.getAuth());
        // 端口
        properties.setProperty("mail.smtp.port", mailProperties.getPort());

        // 2. 创建session
        Session session = Session.getDefaultInstance(
                properties, new MailAuthenticator(mailProperties.getAddress(), mailProperties.getPwd()));
        // 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        session.setDebug(true);

        // 3. 创建邮件
        // 创建邮件对象
        MimeMessage message = new MimeMessage(session);

        // 邮件的标题
        message.setSubject(subject);
        // 邮件发送日期
        message.setSentDate(new Date());
        // 指明邮件的发件人
        message.setFrom(new InternetAddress(mailProperties.getAddress()));

        // 指明邮件的收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(authMailRecord.getEmail()));

        // 指明邮件的抄送人  2018/9/10去除
//        message.setRecipient(Message.RecipientType.CC, new InternetAddress(mailProperties.getCc()));

        // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
        Multipart multipart = new MimeMultipart();

        // 添加邮件正文
        BodyPart contentBodyPart = new MimeBodyPart();
        String result;
        if(MailTypeEnum.CANCEL.getCode().equals(sendFlag)){
            result = buildCancelContent(authMailRecord);
        } else if(MailTypeEnum.PUSH.getCode().equals(sendFlag)){
            result = buildPushContent(authMailRecord);
        } else {
            return;
        }
        contentBodyPart.setContent(result, "text/html;charset=UTF-8");
        multipart.addBodyPart(contentBodyPart);

        // 添加附件
        // 文件存放路径
        String url = authMailRecord.getUrl();
        if (url != null && !"".equals(url)) {
            BodyPart attachmentBodyPart = new MimeBodyPart();
            // 根据附件url获取文件,
            attachmentBodyPart.setDataHandler(new DataHandler(new URL(url)));
            //MimeUtility.encodeWord可以避免文件名乱码
            attachmentBodyPart.setFileName(MimeUtility.encodeWord("委托授权书.pdf"));
            multipart.addBodyPart(attachmentBodyPart);
        }

        // 邮件的文本内容
        message.setContent(multipart);

        // 4. 发送邮件,Transport每次发送成功程序帮忙关闭
        Transport.send(message, message.getAllRecipients());
    }

    private String buildCancelContent(AuthMailRecord authMailRecord){
        // 邮件内容
        String result =
                "<html lang=\"zh-CN\" data-ng-app=\"app\">" +
                        "<head>" +
                        "<style type=\"text/css\" rel=\"stylesheet\">body { -webkit-overflow-scrolling: touch} " +
                        ".newsWrapper{padding: 10px;line-height: 28px; background: #fff;}" +
                        ".newsWrapper p{font-size: 16px;}" +
                        ".newsWrapper img{text-align: center}" +
                        ".newsWrapper h1,.newsWrapper h2,.newsWrapper h3,.newsWrapper h4,.newsWrapper h5,.newsWrapper h6{line-height: 24px;}" +
                        ".newsWrapper hr{border: 1px dashed #ccc;}" +
                        "</style>" +
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\" />" +
                        "<link rel=\"stylesheet\" href=\"http://wx.xftm.com/css/app.min.css\" type=\"text/css\" />" +
                        "</head>" +
                        "<body><div class=\"newsWrapper\">" +
                        "<div><p>致：</p></div> " +
                        "<div>" +
                        "<p>&nbsp;&nbsp;&nbsp;&nbsp;因业务需要，我公司现撤销向贵公司发出的《催收服务任务单》项下委托事项。</p>" +
                        "</div> " +
                        "<div><p>&nbsp;&nbsp;&nbsp;&nbsp;请贵公司收到本通知函之日起，停止对上述《催收服务任务单》所涉车辆进行相关收车安排和动作!</p></div> " +
                        "<div><p>&nbsp;&nbsp;&nbsp;&nbsp;车辆信息为： CPHM  &nbsp;&nbsp; 车架号为：  CJHM   &nbsp;&nbsp; 颜色为：  CLYS  &nbsp;&nbsp; 车型为：CLXH。</p></div> " +
                        "<div><p>&nbsp;&nbsp;&nbsp;&nbsp;否则，因该控制或收回车辆的行为造成的损失或后果，均由贵公司承担，与我公司无关。</p></div> " +
                        "<div><p align=\"right\">WTGSMC</p></div> " +
                        "</div></body></html>";
        result = writeContent(result, authMailRecord);
        return result;
    }

    private String buildPushContent(AuthMailRecord authMailRecord){
        // 邮件内容
        String result =
                "<html lang=\"zh-CN\" data-ng-app=\"app\">" +
                        "<head>" +
                        "<style type=\"text/css\" rel=\"stylesheet\">body { -webkit-overflow-scrolling: touch} " +
                        ".newsWrapper{padding: 10px;line-height: 28px; background: #fff;}" +
                        ".newsWrapper p{font-size: 16px;}" +
                        ".newsWrapper img{text-align: center}" +
                        ".newsWrapper h1,.newsWrapper h2,.newsWrapper h3,.newsWrapper h4,.newsWrapper h5,.newsWrapper h6{line-height: 24px;}" +
                        ".newsWrapper hr{border: 1px dashed #ccc;}" +
                        "</style>" +
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\" />" +
                        "<link rel=\"stylesheet\" href=\"http://wx.xftm.com/css/app.min.css\" type=\"text/css\" />" +
                        "</head>" +
                        "<body><div class=\"newsWrapper\">" +
                        "<div><p>致：</p></div> " +
                        "<div>" +
                        "<p>&nbsp;&nbsp;&nbsp;&nbsp;鉴于我公司租赁客户 KHXM （姓名）债务违约，根据合同条约，现正式发出催收服务任务单：</p>" +
                        "</div> " +
                        "<div><p>&nbsp;&nbsp;&nbsp;&nbsp;车辆信息为： CPHM  &nbsp;&nbsp; 车架号为：  CJHM   &nbsp;&nbsp; 颜色为：  CLYS  &nbsp;&nbsp; 车型为：CLXH。</p></div> " +
                        "<div><p>&nbsp;&nbsp;&nbsp;&nbsp;请向该客户合法催收并确保租赁车辆中财物的安全和完整性。</p></div> " +
                        "<div><p>&nbsp;&nbsp;&nbsp;&nbsp;详请查看附件</p></div> " +
                        "<div><p align=\"right\">WTGSMC</p></div> " +
                        "</div></body></html>";
        result = writeContent(result, authMailRecord);
        return result;
    }




    /**
     * 替换邮件模板内容
     * @param content
     * @param authMailRecord
     * @return
     */
    private String writeContent(String content,AuthMailRecord authMailRecord){
        content = content.replace("KHXM",StringUtil.isNotNull(authMailRecord.getName()) ? authMailRecord.getName() : "-");
        content = content.replace("CPHM",StringUtil.isNotNull(authMailRecord.getPlate()) ? authMailRecord.getPlate() : "-");
        content = content.replace("CJHM",StringUtil.isNotNull(authMailRecord.getVehicleIdentifyNum()) ? authMailRecord.getVehicleIdentifyNum() : "-");
        content = content.replace("CLYS",StringUtil.isNotNull(authMailRecord.getVehicleColor()) ? authMailRecord.getVehicleColor() : "-");
        content = content.replace("CLXH",StringUtil.isNotNull(authMailRecord.getVehicleType()) ? authMailRecord.getVehicleType() : "-");
        content = content.replace("WTGSMC",StringUtil.isNotNull(authMailRecord.getLeaseCompanyName()) ? authMailRecord.getLeaseCompanyName() : "");
        return content;
    }
}
